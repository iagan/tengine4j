package org.ithinking.tengine.html.parser;

import java.util.LinkedList;
import java.util.List;

import org.ithinking.tengine.core.TAG;
import org.ithinking.tengine.ExprParser;
import org.ithinking.tengine.core.Renderable;
import org.ithinking.tengine.html.Document;
import org.ithinking.tengine.html.Expr;
import org.ithinking.tengine.html.Tag;
import org.ithinking.tengine.html.Text;

public class HtmlHandler implements Handler {
	private Document doc = new Document();
	private TagParser tagParser = new TagParser();
	private BlockStack blockStack = new BlockStack();
	private Text currentTextBlock = null;
	private TextHandler textHandler = new TextHandler();

	/**
	 * 处理开始
	 */
	@Override
	public void start() {
		blockStack.push(doc);
	}

	/**
	 * 文本处理： 发现表达式时，刷新缓冲的字符到，解析表达式完毕后，加入表达式
	 * 
	 * @param text
	 * @param start
	 * @param end
	 */
	public void parseText(String text, int start, int end) {
		ExprParser.parseText(text, start, end, textHandler);
	}

	private void addTagBlock(Tag tag) {
		// 0. 清空文本块以便开始新的文本块
		currentTextBlock = null;
		// 1. 新增标签
		blockStack.peek().addChild(tag);
		// 2.对标签块需要放入堆栈
		blockStack.push(tag);
	}

	private void addExpression(String source, int start, int count) {
		// 0. 清空文本块以便开始新的文本块
		currentTextBlock = null;
		// 1. 新增表达式
		Expr expBlock = new Expr(source.substring(start, start + count));
		blockStack.peek().addChild(expBlock);
	}

	private void addPlainText(String source, int start, int count) {
		if (currentTextBlock == null) {
			currentTextBlock = new Text();
			blockStack.peek().addChild(currentTextBlock);
		}
		currentTextBlock.append(source, start, start + count - 1);
	}

	@Override
	public void pi(String text, int start, int end) {
		parseText(text, start, end);
	}

	@Override
	public void docType(String text, int start, int end) {
		parseText(text, start, end);
	}

	@Override
	public void comment(String text, int start, int end) {
		parseText(text, start, end);
	}

	@Override
	public void cdata(String text, int start, int end) {
		parseText(text, start, end);
	}


	/**
	 * 起始标签处理 (1.需要解析指令属性 2.层级嵌套关系, 为了变量作用域以及循环结构)
	 */
	@Override
	public void startTag(String text, int start, int end, int features) {
		if ((features & TAG.F_DIRECTIVE) == TAG.F_DIRECTIVE) {
			Tag tag = tagParser.reset().setTag(text, start, end)
					.setFeatures(features).parse();
			tag.setDoc(doc);
			addTagBlock(tag);
		} else {
			this.parseText(text, start, end);
		}
	}

	/**
	 * 元素结束标签
	 */
	@Override
	public void endTag(String text, int start, int end, int features) {
		if ((features & TAG.F_DIRECTIVE) == TAG.F_DIRECTIVE) {
			Renderable block = blockStack.pop();
			if (block instanceof Tag) {
				((Tag) block).setFeatures(features);
			} else {
				throw new RuntimeException("解析错误，标签嵌套错误!");
			}
			// 每一个指令结束，都要开启新的文本块
			currentTextBlock = null;
		} else if ((features & TAG.F_TAG_CLOSE) == TAG.F_TAG_CLOSE) {
			this.addPlainText(text, start, end - start + 1);
		}
	}

	@Override
	public void plainText(String text, int start, int end) {
		parseText(text, start, end);
	}

	@Override
	public void end() {
		blockStack.pop();
		if (!blockStack.isEmpty()) {
			throw new RuntimeException("解析模板有误，请检查标签嵌套情况");
		}
		doc.init(null);
	}

	/**
	 * 标签栈
	 * 
	 * @author test
	 *
	 */
	private class BlockStack {
		int deep = -1;
		List<Renderable> tags = new LinkedList<Renderable>();

		public void push(Renderable tag) {
			tags.add(tag);
			deep++;
		}

		public Renderable pop() {
			return tags.remove(deep--);
		}

		public Renderable peek() {
			return deep >= 0 ? tags.get(deep) : null;
		}

		public boolean isEmpty() {
			return deep == -1 && tags.isEmpty();
		}
	}

	public Document getRootBlock() {
		return doc;
	}

	/**
	 * 文本处理器
	 * 
	 * @author test
	 *
	 */
	private class TextHandler implements ExprParser.Handler {
		@Override
		public void plainText(String text, int start, int count) {
			addPlainText(text, start, count);
		}

		@Override
		public void expression(String text, int start, int count) {
			addExpression(text, start, count);
		}
	}
}
