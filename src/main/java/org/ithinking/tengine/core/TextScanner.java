package org.ithinking.tengine.core;

public abstract class TextScanner {

	protected String text;
	protected int cursor = -1;
	protected int mark = 0;
	protected int len = 0;
	//
	protected int lineNumber = 0;
	protected int columnNumber = 0;
	protected int lastColumnNumber = 0;

	public static final int EOF = -1;

	public TextScanner(String text) {
		this.text = text;
		this.setText(text, text.length());
	}

	public int curr() {
		return text.charAt(cursor);
	}

	public TextScanner mark() {
		this.mark = cursor;
		return this;
	}

	public int after() {
		int i = this.cursor + 1;
		return i < this.len ? text.charAt(i) : EOF;
	}

	public int before() {
		return text.charAt(cursor - 1);
	}

	public boolean isafter(int afterChar) {
		return after() == afterChar;
	}

	public int next() {
		return cursor + 1 < len ? locate(text.charAt(++cursor)) : EOF;
	}

	public String sub() {
		return text.substring(mark, cursor < 0 ? 0 : cursor);
	}

	public TextScanner unnext() {
		// 根据当前字符决定是否要回退行列号
		unlocate(text.charAt(cursor));
		cursor--;
		return this;
	}

	public boolean isnext(int nextChar) {
		int nch = next();
		if (nch == nextChar) {
			return true;
		} else {
			unnext();
			return false;
		}
	}

	private int locate(int ch) {
		// window: \r\n Linux: \n
		if (ch == '\n') {
			lineNumber++;
			lastColumnNumber = columnNumber;
			columnNumber = 0;
		} else {
			columnNumber++;
		}
		return ch;
	}

	private int unlocate(int ch) {
		// window: \r\n Linux: \n
		if (ch == '\n') {
			lineNumber--;
			columnNumber = lastColumnNumber;
		} else {
			columnNumber--;
		}
		return ch;
	}

	private TextScanner setText(String text, int len) {
		this.text = text;
		this.cursor = -1;
		this.len = len;
		return this;
	}

	/**
	 * 跳过空白字符 (注: cursor指向最后一个空白字符)
	 * 
	 * @return
	 */
	public TextScanner skipWhitespace() {
		while (isNextWhitespace())
			;
		return this;
	}

	/**
	 * 下一个字符是否为空白字符
	 * 
	 * @return
	 */
	public boolean isNextWhitespace() {
		int ch = next();
		if (ch == EOF) {
			return false;
		} else if (ch <= ' ') {
			return true;
		} else {
			return false;
		}
	}

	public TextScanner skipComment() {
		return this;
	};
	
	public String readToken(){
		return null;
	}
}
