package org.ithinking.tengine.html.parser;

import static java.lang.Character.isLetter;

import java.util.ArrayList;
import java.util.List;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.TAG;
import org.ithinking.tengine.core.TextScanner;

public class HtmlScanner extends TextScanner {
	// 0x1A 或者 \0 ???
	private static final int EOF = -1;
	//
	private int textFromIndex = 0;

	//
	private Tag curtag = new Tag();
	private TagStack tagStack = new TagStack();
	private Handler handler;
	private String[] textTags = new String[] { "script", "style", "pre", "code" };
	private String[] emptyTags = new String[] { "img", "input", "br", "hr", "link", "iframe", "area", "col", "frame",
			"base", "spacer", "basefont", "bgsound", "command", "embed", "keygen", "param", "source", "track" };

	public HtmlScanner(String text) {
		super(text);
	}

	public HtmlScanner setHandler(Handler handler) {
		this.handler = handler;
		return this;
	}

	private HtmlScanner markStartText() {
		this.textFromIndex = cursor + 1;
		return this;
	}

	private boolean isPI() {
		return text.regionMatches(cursor, TAG.SYMBOL_PI, 0, TAG.SYMBOL_PI.length());
	}

	private boolean isDoctype() {
		return text.regionMatches(false, cursor, TAG.SYMBOL_DOCTYPE, 0, TAG.SYMBOL_DOCTYPE.length());
	}

	private boolean isComment() {
		return text.regionMatches(cursor, TAG.SYMBOL_COMMENT, 0, TAG.SYMBOL_COMMENT.length());
	}

	private boolean isCDATA() {
		return text.regionMatches(false, cursor, TAG.SYMBOL_CDATA, 0, TAG.SYMBOL_CDATA.length());
	}

	private boolean isEndTag() {
		return text.regionMatches(cursor, TAG.SYMBOL_TAG_END, 0, TAG.SYMBOL_TAG_END.length());
	}

	private boolean isStartTag() {
		return curr() == '<' && isLetter(after());
	}

	public void start() {
        if(XString.isBlank(this.text)){
            return;
        }
		handler.start();
		int ch = next();
		if (ch != '\uFEFF') { // skip leading BOM
			unnext();
		}

		while ((ch = next()) != EOF) {
			if (ch == '<') {
				if (isPI()) {
					this.preReportText().reportPI().markStartText();
				} else if (isDoctype()) {
					this.preReportText().reportDoctype().markStartText();
				} else if (isComment()) {
					this.preReportText().reportComment().markStartText();
				} else if (isCDATA()) {
					this.preReportText().reportCDATA().markStartText();
				} else if (isStartTag()) {
					this.preReportText().reportStartTag().markStartText();
					if (curtag.isSelfClose()) {
						this.endTag(0, 1, TAG.F_SELF_CLOSE);
					} else {
						if (curtag.isTextTag()) {
							this.mark();
							this.skipTagContent(curtag.start + 1, curtag.nameidx, false).reportContent()
									.markStartText();
						} else if (curtag.isEmptyTag()) {
							// 不存在空白结束标签时，需要手动调用endTag
							if (!existsEndTag(curtag.start + 1, curtag.nameidx)) {
								this.endTag(0, 0, TAG.F_FIX_CLOSE);
							}
						}
					}
				} else if (isEndTag()) {
					this.preReportText().reportEndTag().markStartText();
				}
			} else if (ch == '$') {
				if (isafter('{')) {
					skipTo('}', true);
				}
			}
		}

		endDoc();

	}

	/**
	 * 是否存在结束标签
	 * 
	 * @param nameStart
	 * @param nameEnd
	 * @return
	 */
	private boolean existsEndTag(int nameStart, int nameEnd) {
		boolean exists = false;
		while (isNextWhitespace())
			;
		if (curr() == '<') {
			if (isafter('/')) {
				if (text.regionMatches(false, cursor + 2, text, nameStart, nameEnd - nameStart + 1)) {
					exists = true;
				}
			}
			unnext(); // 退回到 '<'的前一个位置
		}

		return exists;
	}

	/**
	 * 从指定位置开始截止字符串到当前位置
	 * 
	 *
	 * @return
	 */
	public String substrFromMark() {
		return text.substring(this.mark, cursor + 1);
	}

	/**
	 * 报告文本内容[textFromIndex, cursor - 1]
	 * 
	 * @return
	 */
	private HtmlScanner preReportText() {
		if (cursor > textFromIndex) {
			this.handler.plainText(text, textFromIndex, cursor - 1);
		}
		return this;
	}

	/**
	 * 报告文本标签内容[textFromIndex, cursor]
	 * 
	 * @return
	 */
	private HtmlScanner reportContent() {
		if (cursor > textFromIndex) {
			this.handler.plainText(text, textFromIndex, cursor);
		}
		return this;
	}

	/**
	 * 读取处理指令
	 * 
	 * @return
	 */
	private HtmlScanner reportPI() {
		this.handler.pi(text, skipTo('?', '>', true).mark, cursor);
		return this;
	}

	/**
	 * 读取文档声明
	 * 
	 * @return
	 */
	private HtmlScanner reportDoctype() {
		this.handler.docType(text, skipTo('>', true).mark, cursor);
		return this;
	}

	/**
	 * 读取注释段
	 * 
	 * @return
	 */
	private HtmlScanner reportComment() {
		this.handler.comment(text, skipTo('-', '-', '>', false).mark, cursor);
		return this;
	}

	/**
	 * 读取CDATA
	 * 
	 * @return
	 */
	private HtmlScanner reportCDATA() {
		this.handler.cdata(text, skipTo(']', ']', '>', false).mark, cursor);
		return this;
	}

	private String bugstr() {
		int end = this.cursor + 20 < this.len ? this.cursor + 20 : this.len;
		return text.substring(this.cursor, end);
	}

	/**
	 * 读取开始标签 <div id="" class="" disabled value=123 style = "color:red">
	 * 
	 * @return
	 */
	private HtmlScanner reportStartTag() {
		// this.handler.startTag(source, skipTo('>', true).mark, cursor);
		int ch, tagStart = this.cursor; // lastEQ = 0;
		int nameidx = 0;
		int features = 0; // 重置特性
		boolean isTagName = true, selfClose = false;
		while ((ch = next()) != EOF) {
			if ((ch == '\'' || ch == '"')) {
				if (skipStringAndScanExpr(ch)) { // 跳过字符串并检测是否存在表达式(???指令中写表达式怎么办???)
					features |= TAG.F_ATTR_EXPR;
				}
				ch = after();
				if (ch > ' ' && ch != '/' && ch != '>') {
					throw new RuntimeException("字符串结束的下一个字符应该是空白字符: " + bugstr());
				}
			} else if (ch <= ' ' || ch == '/' || ch == '>') {
				if (isTagName) { // 标签名
					nameidx = this.cursor - 1;
					features |= parseTagFlag(tagStart + 1, nameidx);
					isTagName = false;
				} else { // 属性名称
					if (isDirective(mark, cursor - 1)) {
						features |= TAG.F_DIRECTIVE;
					}
				}

				if (ch == '/') {
					ch = next();
					if (ch != '>') {
						throw new RuntimeException("标签有误，'/'之后应紧跟着'>'字符");
					} else {
						selfClose = true;
					}
				}

				if (ch == '>') {
					break;
				}

				// 跳过多余空格后继续
				skipWhitespace().mark().unnext();

			} else if (ch == '=') {
				// lastEQ = cursor;
				// 属性名称 <div id="1" class = "red">
				if (before() > ' ') { // 不是空白字符，说明该属性没有被解析过
					if (isDirective(mark, cursor - 1)) {
						features |= TAG.F_DIRECTIVE;
					}
				}
				// 跳过等号后面的空白字符
				skipWhitespace().mark().unnext();
			}
		}
		if (ch != '>') {
			throw new RuntimeException("Not find end-tag-char '>'.");
		}
		if (selfClose) {
			features |= TAG.F_SELF_CLOSE;
			if (before() != '/') {
				throw new RuntimeException("自关闭标签错误，'/'与'>'要紧接在一起.");
			}
		}
		curtag.start = tagStart;
		curtag.end = cursor;
		curtag.nameidx = nameidx;
		curtag.features = features;
		return startTag(curtag);
	}

	/**
	 * 报告结束标签
	 * 
	 * @return
	 */
	private HtmlScanner reportEndTag() {
		int start = this.skipTo('>', false).mark;
		return this.endTag(start, cursor, TAG.F_TAG_CLOSE);
	}

	/**
	 * 起始标签
	 * 
	 * @param tag
	 * @return
	 */
	private HtmlScanner startTag(Tag tag) {
		tagStack.push(tag);
		this.handler.startTag(text, tag.start, tag.end, tag.features);
		return this;
	}

	/**
	 * 结束标签
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private HtmlScanner endTag(int start, int end, int features) {
		Tag tag = tagStack.pop();
		if ((features & TAG.F_TAG_CLOSE) == TAG.F_TAG_CLOSE) {
			if (!text.regionMatches(false, start + 2, text, tag.start + 1, tag.nameidx - tag.start)) {
				throw new RuntimeException("标签嵌套错误!");
			}
		}
		this.handler.endTag(text, start, end, features | tag.features);
		return this;
	}

	/**
	 * 文档解析结束
	 * 
	 * @return
	 */
	private void endDoc() {
		if (!tagStack.isEmpty()) {
			throw new RuntimeException("文档解析有误,标签嵌套不正确.");
		}
		handler.end();
	}

	/**
	 * 解析标签
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private int parseTagFlag(int start, int end) {
		int features = 0;
		if (text.regionMatches(false, start, TAG.META, 0, TAG.META.length())) {
			features |= TAG.F_EMPTY_TAG | TAG.F_META;
		} else {
			for (String tag : textTags) {
				if (text.regionMatches(false, start, tag, 0, tag.length())) {
					features |= TAG.F_TEXT_TAG;
					break;
				}
			}

			for (String tag : emptyTags) {
				if (text.regionMatches(false, start, tag, 0, tag.length())) {
					// 没有嵌套内容
					features |= TAG.F_EMPTY_TAG;
					break;
				}
			}
		}

		return features;
	}

	private boolean isDirective(int start, int end) {
		return text.regionMatches(false, start, TAG.DIRECTIVE_PREFIX, 0, TAG.DIRECTIVE_PREFIX.length());
	}

	/**
	 * 跳到文本标签结束，停止在'<'的前一个字符 eg: <script> ..... </script>
	 * 
	 * @param nameStart
	 * @param nameEnd
	 * @param ignoreCase
	 * @return
	 */
	private HtmlScanner skipTagContent(int nameStart, int nameEnd, boolean ignoreCase) {
		int ch;
		boolean wellFormed = false;
		mark();
		while ((ch = next()) != EOF) {
			if (ch == '\'' || ch == '"') {
				skipString(ch);
			} else if (ch == '<') {
				if (isafter('/')) {
					if (text.regionMatches(ignoreCase, cursor + 2, text, nameStart, nameEnd - nameStart + 1)) {
						wellFormed = true;
						break;
					}
				}
			}
		}

		if (!wellFormed) {
			throw new RuntimeException("Not well-formed.");
		}
		unnext(); // 指向'<'前一个字符
		return this;
	}

	/**
	 * 
	 * 
	 * @param ch1
	 * @param ch2
	 * @return
	 */
	private HtmlScanner skipTo(int ch1, int ch2, int ch3, boolean skipString) {
		int ch;
		mark();
		while ((ch = next()) != EOF) {
			if (skipString && (ch == '\'' || ch == '"')) {
				skipString(ch);
			} else if (ch == ch1) {
				if (isnext(ch2)) {
					if (isnext(ch3)) {
						return this;
					}
					unnext();
				}
			}
		}
		throw new RuntimeException("Not well-formed.");
	}

	private HtmlScanner skipTo(int ch1, int ch2, boolean skipString) {
		int ch;
		mark();
		while ((ch = next()) != EOF) {
			if (skipString && (ch == '\'' || ch == '"')) {
				skipString(ch);
			} else if (ch == ch1) {
				if (isnext(ch2)) {
					return this;
				}
			}
		}
		throw new RuntimeException("Not well-formed.");
	}

	private HtmlScanner skipTo(int ch1, boolean skipString) {
		int ch;
		mark();
		while ((ch = next()) != EOF) {
			if (skipString && (ch == '\'' || ch == '"')) {
				skipString(ch);
			} else if (ch == ch1) {
				return this;
			}
		}
		throw new RuntimeException("Not well-formed.");
	}

	/**
	 * 跳过字符串
	 * 
	 * @param strChar
	 * @return
	 */
	private HtmlScanner skipString(int strChar) {
		int ch;
		while ((ch = next()) != EOF) {
			if (ch == strChar) {
				return this;
			}
		}
		throw new RuntimeException("字符串未正常结束，缺少" + ch);
	}

	private boolean skipStringAndScanExpr(int strChar) {
		boolean hasExpr = false;
		int ch;
		while ((ch = next()) != EOF) {
			if (ch == strChar) {
				return hasExpr;
			} else if (ch == '$') {
				if (isafter('{')) {
					this.skipTo('}', true);
				}
				hasExpr = true;
			}
		}
		throw new RuntimeException("字符串未正常结束，缺少" + ch);
	}

	public class Tag {
		private int features = 0;
		private int start;
		private int nameidx;
		private int end;

		public boolean isSelfClose() {
			return (features & TAG.F_SELF_CLOSE) == TAG.F_SELF_CLOSE;
		}

		public boolean isTextTag() {
			return (features & TAG.F_TEXT_TAG) == TAG.F_TEXT_TAG;
		}

		public boolean isEmptyTag() {
			return (features & TAG.F_EMPTY_TAG) == TAG.F_EMPTY_TAG;
		}
	}

	private class TagStack {
		private int deep = -1;
		private List<Tag> tags = new ArrayList<Tag>();

		public boolean isEmpty() {
			return deep == -1;
		}

		public Tag push(int start, int end, int nameidx, int features) {
			// 0.stack index
			deep++;
			// 1.复用Tag
			Tag tag = null;
			if (deep < tags.size()) {
				tag = tags.get(deep);
			} else {
				tag = new Tag();
				tags.add(tag);
			}
			// 2.复制属性
			tag.start = start;
			tag.end = end;
			tag.nameidx = nameidx;
			tag.features = features;
			//
			// print(tag, deep, 0);
			return tag;
		}

		public Tag pop() {
			Tag tag = tags.get(deep);
			// print(tag, deep, 1);
			deep--;
			return tag;
		}

		@SuppressWarnings("unused")
		private void print(Tag tag, int deep, int type) {
			System.out.println();
			for (int i = 0; i < deep; i++) {
				System.out.print("	");
			}
			if (type == 0) {
				System.out.print(text.substring(tag.start, tag.end + 1));
			} else {
				System.out.print("</" + text.substring(tag.start + 1, tag.nameidx + 1) + ">");
			}
		}

		public Tag push(Tag tag) {
			return push(tag.start, tag.end, tag.nameidx, tag.features);
		}
	}
}
