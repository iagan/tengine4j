package org.ithinking.tengine.html.parser;

import org.ithinking.tengine.core.DIRECTIVE;
import org.ithinking.tengine.expr.ExpressionFactory;
import org.ithinking.tengine.html.Attr;
import org.ithinking.tengine.html.FragAttr;
import org.ithinking.tengine.html.Tag;
import org.ithinking.tengine.html.URLParam;

public class TagParser {
	private static final int EOF = -1;
	private String text;
	private int start = 0;
	private int end;
	//
	private int cursor = -1;
	private int mark = -1;
	private String tagName;
	private int features = 0;
	private boolean existsExpression = false;

	// ignore,if,repeat,loop-if

	public TagParser() {
		this("", 0, 0);
	}

	public TagParser(String text) {
		this(text, 0, text.length() - 1);
	}

	public TagParser(String text, int start, int end) {
		this.setTag(text, start, end);
	}

	public TagParser reset() {
		this.text = "";
		this.start = 0;
		this.end = -1;
		this.cursor = -1;
		this.mark = -1;
		this.tagName = null;
		this.features = 0;
		return this;
	}

	public TagParser setTag(String text) {
		return this.setTag(text, 0, text.length() - 1);
	}

	public TagParser setTag(String text, int start) {
		return this.setTag(text, 0, text.length() - 1);
	}

	public TagParser setTag(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
		this.cursor = start;
		this.mark = cursor + 1;
		return this;
	}

	public TagParser setFeatures(int features) {
		this.features = features;
		return this;
	}

	private int current() {
		return cursor <= end ? text.charAt(cursor) : EOF;
	}

	private int next() {
		return cursor < end ? text.charAt(++cursor) : EOF;
	}

	private TagParser inc() {
		cursor++;
		return this;
	}

	private boolean ifnext(char what) {
		cursor++;
		if (cursor <= end && text.charAt(cursor) == what) {
			return true;
		} else {
			cursor--;
		}
		return false;
	}

	private TagParser mark() {
		this.mark = this.cursor;
		return this;
	}

	private TagParser markNext() {
		this.mark = this.cursor + 1;
		return this;
	}

	/**
	 * 以空白字符,'=','/','>'为Token分隔字符
	 * 
	 * <div selected id="testid" class = "cl" selected style="00" >
	 * 
	 * @return
	 */
	private TagParser nextToken() {
		int ch;
		while ((ch = next()) != EOF) {
			if (ch <= ' ' || ch == '=' || ch == '/' || ch == '>') {
				break;
			} else if (ch == '"' || ch == '\'') {
				this.skipString((char) ch);
				break;
			}
		}
		if (ch == EOF) {
			throw new RuntimeException("标签语法错误");
		}
		return this;
	}

	private boolean existsExpressionAndReset() {
		boolean old = this.existsExpression;
		this.existsExpression = false;
		return old;
	}

	/**
	 * <div selected id="testid" class = "cl" selected style="00" >
	 * 
	 * @return
	 */
	public Tag parse() {
		String tagName = this.markNext().nextToken().substr();
		Tag tag = new Tag(tagName, this.features);
		while (hasNextAttr()) {
			tag.addAttr(readAsAttr());
		}
		return tag;
	}

	/**
	 * 是否存在下一个标签属性 判断规则：下一个的第一个非空白字符不为'/','>'
	 * 
	 * @return
	 */
	private boolean hasNextAttr() {
		int ch = this.skipIfSpace().current();
		// 为了容错,不使用Character.isLetter(ch)判断
		return (ch != '/' && ch != '>' && ch != EOF);
	}

	/**
	 * 读取并创建Attr <br>
	 * 注意：先调用hasNextAttr(), 然后才调用nextAttr()
	 * 
	 * @return
	 */
	private Attr readAsAttr() {
		// next-token-end-char: ' ', '=', '/', '>'
		String declareName = this.mark().nextToken().substr().trim();
		// name [tg-repeat-user; data-tg-repeat-user; tg:repeat-user]
		String directiveName = getDirectiveName(declareName);
		String directiveParam = getDirectiveParam(directiveName, declareName);
		Attr attr = createAttr(directiveName);
		attr.setParam(directiveParam);
		if (hasAttrValue()) {
			readAttrValue(attr);
		}
		return attr;
	}

	/**
	 * 根据指令名称创建指令
	 * 
	 * @param directiveName
	 */
	private Attr createAttr(String directiveName) {
		Attr attr = null;
		if (directiveName.equalsIgnoreCase(DIRECTIVE.FRAG.getDirectiveName())) {
			attr = new FragAttr(directiveName);
			attr.setType(DIRECTIVE.FRAG);
		} else {
			attr = new Attr(directiveName);
			attr.setType(DIRECTIVE.ANY_ATTR);
			for (DIRECTIVE directive : DIRECTIVE.values()) {
				if (directiveName.equalsIgnoreCase(directive.name())) {
					attr.setType(directive);
					break;
				}
			}
		}
		return attr;
	}

	/**
	 * 获取指令参数
	 * 
	 * @param directiveName
	 * @param declareName
	 * @return
	 */
	private String getDirectiveParam(String directiveName, String declareName) {
		int len1 = declareName.length(), len2 = directiveName.length();
		if (len1 > len2) {
			int i = declareName.indexOf(directiveName);
			if (i != -1 && (len1 - i) > len2) {
				if (declareName.charAt(i + len2) == '-') {
					String param = declareName.substring(i + len2 + 1);
					return this.toHumpName(param);
				}
			}
		}

		return null;
	}

	/**
	 * 获取指令名称
	 * 
	 * @param declareName
	 * @return
	 */
	private String getDirectiveName(String declareName) {
		int pos = -1;
		if (declareName.startsWith("data-tg-")) {
			pos = "data-tg-".length();
		} else if (declareName.startsWith("tg-")) {
			pos = "tg-".length();
		} else if (declareName.startsWith("tg:")) {
			pos = "tg:".length();
		}

		String dname;
		char ch;
		if (pos != -1) {
			declareName = declareName.substring(pos);
			for (DIRECTIVE directive : DIRECTIVE.values()) {
				dname = directive.getDirectiveName();
				if (declareName.regionMatches(false, 0, dname, 0, dname.length())) {
					if (declareName.length() == dname.length()) {
						return declareName;
					} else {
						ch = declareName.charAt(dname.length());
						if (ch == '-' || ch <= ' ' || ch == '\'' || ch == '"' || ch == '=') {
							declareName = declareName.substring(0, dname.length());
							return declareName;
						}
					}
				}
			}
			// throw new RuntimeException("未知指令: " + declareName);
		}
		return declareName;
	}

	/**
	 * 转换为驼峰命名
	 * 
	 * @param name
	 * @return
	 */
	private String toHumpName(String name) {
		int i = 0;
		while ((i = name.indexOf("-")) != -1) {
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
		}
		return name;
	}

	/**
	 * 是否存在属性值<br>
	 * 
	 * @return
	 */
	private boolean hasAttrValue() {
		// "="号之前可能存在空白字符,故先尝试跳过空白字符
		int ch = this.skipIfSpace().current();
		return (ch == '=');
	}

	public String substr() {
		return text.substring(mark, cursor);
	}

	/**
	 * 读取属性值，<br>
	 * 注：先调用hasAttrValue(); <br>
	 * <div selected id="id34" class = "cls" />
	 * 
	 * @return
	 */
	private void readAttrValue(Attr attr) {
		String value = null;
		int eqIndex = this.cursor;
		boolean hasExpression = false;
		// 跳过"="号右边的空白字符，并取第一个非空白字符
		int ch = this.inc().skipIfSpace().mark().current();
		if (ch == '\"' || ch == '\'') {
			this.skipStringAndExpr((char) ch);
			if (!hasExpression) { // 属性值是否存在表达式
				hasExpression = this.existsExpressionAndReset();
			}
			value = text.substring(mark + 1, cursor);
			attr.setStrChar(Character.toString((char) ch));
			next(); // 跳过字符
		} else {
			// 容错处理
			if (eqIndex + 1 == cursor) {
				// 紧接"="号后边的为非空白字符
				value = this.nextToken().substr();
			} else {
				// 紧接"="号后边的是空白字符
				value = "";
			}
		}
		attr.setValue(value);
		// 存在表达式或纯文本
		attr.setValueType(hasExpression ? 1 : 0);

		if (attr instanceof FragAttr) {
			parseFragUrl((FragAttr) attr);
		}

	}

	/**
	 * 解析片段URL
	 * subtpl?id=122&name=agan&email=${admin.email}&userList=${userList}#numList
	 * 
	 * @param attr
	 */
	private void parseFragUrl(FragAttr attr) {
		String url = attr.getValue();
		if (url != null) {
			int i = getAndSetTplPath(url, attr); // 返回 '?' 或 '#' 或 最后字符的位置
			int ch = url.charAt(i);
			if (ch == '?') { // 读取参数
				while ((i = getAndSetParam(attr, url, i + 1)) != -1 && (ch = url.charAt(i)) != '#')
					;
			}

			if (ch == '#') { // 读取片段
				setFragmentId(attr, url, i + 1);
			}
		}

	}

	private void setFragmentId(FragAttr attr, String url, int start) {
		String fragName = url.substring(start).trim();
		if (fragName.startsWith("${") && fragName.endsWith("}")) {
			attr.setFragid(ExpressionFactory.createExpression(fragName.substring(2, fragName.length() - 1)));
		} else {
			int i = fragName.indexOf("${");
			if (i != -1 && fragName.indexOf("}", i + 1) != -1) {
				attr.setFragid(ExpressionFactory.createCompositeExpression(fragName));
			} else {
				attr.setFragid(ExpressionFactory.createStringExpression(fragName));
			}
		}
	}

	private int getAndSetParam(FragAttr attr, String url, int start) {
		String name = null;
		String value = null;
		boolean hasExpr = false, scanNameCompleted = false;
		int i = start;
		for (int len = url.length(), ch; i < len; i++) {
			ch = url.charAt(i);
			if (ch == '=') {
				name = url.substring(start, i).trim();
				start = i + 1;
				scanNameCompleted = true;
			} else if (ch == '$') {
				if (!scanNameCompleted) {
					throw new RuntimeException("参数名称不允许出现表达式!");
				}
				if (i + 1 < len) {
					if (url.charAt(i + 1) == '{') { // 跳过表达式
						i += 2;
						while (i < len) {
							ch = url.charAt(i++);
							if (ch == '\'' || ch == '"') { // 跳过字符串
								i = url.indexOf(ch, i + 1);
								if (i == -1) {
									throw new RuntimeException("字符串定义错误，未发现结束界定符！");
								}
							} else if (ch == '}') {
								hasExpr = true;
								i--; // 让i指向 '}' 字符的位置(外层for的i++抵消)
								break;
							}
						}
						if (ch != '}') {
							throw new RuntimeException("表达式错误，未结束！");
						}
					}
				}
			} else if (ch == '&' || ch == '#') {
				value = url.substring(start, i).trim();
				break;
			}
		}

		if (scanNameCompleted) {
			URLParam param = null;
			if (hasExpr) {
				if (value.startsWith("${") && value.charAt(value.length() - 1) == '}') {
					param = new URLParam(name,
							ExpressionFactory.createExpression(value.substring(2, value.length() - 1)));
				} else {
					param = new URLParam(name, ExpressionFactory.createCompositeExpression(value));
				}
			} else {
				param = new URLParam(name, ExpressionFactory.createStringExpression(value));
			}
			attr.addParam(param);
			return i;
		} else {
			return -1;
		}
	}

	private int getAndSetTplPath(String url, FragAttr attr) {
		boolean hasExpr = false;
		String path = null;
		int i = 0;
		for (int len = url.length(); i < len; i++) {
			char ch = url.charAt(i);
			if (ch == '?' || ch == '#') {
				path = url.substring(0, i).trim();
				break;
			} else if (ch == '$') { // 跳过表达式
				if (i + 1 < len) {
					if (url.charAt(i + 1) == '{') {
						i = i + 2;
						while (i < len) {
							ch = url.charAt(i++);
							if (ch == '\'' || ch == '"') { // 跳过字符串
								i = url.indexOf(ch, i + 1);
								if (i == -1) {
									throw new RuntimeException("字符串未正确定义，缺少结束界定符!");
								}
							} else if (ch == '}') {
								hasExpr = true;
								i--; // 让i指向 '}' 字符的位置(外层for的i++抵消)
								break;
							}
						}
						if (ch != '}') {
							throw new RuntimeException("表达式未正确结束");
						}
					}
				}
			}
		}
		if (path == null) {
			path = url.trim();
			i = url.length() - 1;
		}

		if (hasExpr) {
			if (path.startsWith("${")) {
				attr.setPath(ExpressionFactory.createExpression(path.substring(2, path.length() - 1)));
			} else {
				attr.setPath(ExpressionFactory.createCompositeExpression(path));
			}
		} else {
			attr.setPath(ExpressionFactory.createStringExpression(path));
		}

		// 如果未找到，则认为整个字符串是页面路径,不带参数或片段
		return i;
	}

	private TagParser skipIfSpace() {
		int ch = current();
		if (ch <= ' ') {
			while (next() <= ' ')
				;
		}
		return this;
	}

	private TagParser skipString(char delimiter) {
		int ch;
		while ((ch = next()) != delimiter && ch != EOF)
			;
		if (ch != delimiter) {
			throw new RuntimeException("语法错误: 字符串未正确结尾");
		}
		return this;
	}

	private TagParser skipExpr() {
		int ch;
		while ((ch = next()) != EOF) {
			if (ch == '"' || ch == '\'') {
				skipString((char) ch);
			} else if (ch == '}') {
				break;
			}
		}
		if (ch != '}') {
			throw new RuntimeException("语法错误: 表达式未结尾");
		}
		return this;
	}

	private TagParser skipStringAndExpr(char delimiter) {
		int ch;
		while ((ch = next()) != EOF) {
			if (ch == '$') {
				if (ifnext('{')) {
					skipExpr();
					existsExpression = true;
				}
			} else if (ch == delimiter) {
				break;
			}
		}

		if (ch != delimiter) {
			throw new RuntimeException("语法错误: 字符串未正确结尾");
		}
		return this;
	}

	public String getTagName() {
		return tagName;
	}

	@Override
	public String toString() {
		return text.substring(start, end + 1);
	}
}
