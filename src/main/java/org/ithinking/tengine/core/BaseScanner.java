package org.ithinking.tengine.core;

import static org.ithinking.tengine.sql.CharTypes.isFirstIdentifierChar;
import static org.ithinking.tengine.sql.CharTypes.isIdentifierChar;
import static org.ithinking.tengine.sql.TokenType.COMMA;
import static org.ithinking.tengine.sql.TokenType.EOF;
import static org.ithinking.tengine.sql.TokenType.ERROR;
import static org.ithinking.tengine.sql.TokenType.LITERAL_CHARS;
import static org.ithinking.tengine.sql.TokenType.LPAREN;
import static org.ithinking.tengine.sql.TokenType.RBRACKET;
import static org.ithinking.tengine.sql.TokenType.RPAREN;

import org.ithinking.tengine.exception.ParserException;
import org.ithinking.tengine.sql.CharTypes;
import org.ithinking.tengine.sql.Keywords;
import org.ithinking.tengine.sql.TokenType;

public class BaseScanner {

	protected final String text;
	protected int pos = -1;
	protected int mark = 0;
	protected char ch;
	protected int escapeChar = -1;
	protected String error = null;
	protected BaseToken token = new BaseToken();
	protected Keywords keywods = Keywords.DEFAULT_KEYWORDS;

	public BaseScanner(String text) {
		this.text = text;
	}

	protected boolean hasNext() {
		return pos + 1 < text.length();
	}

	protected Token nextToken() {
		scanToken();
		return token;
	}

	protected Token token() {
		return token;
	}

	/**
	 * 标记下一个字符的位置
	 */
	protected void markNext() {
		mark = pos + 1;
	}

	/**
	 * 如果还有下一个字符，则移动到下一个位置并返回true，否则停留在当前位置并返回false
	 * 
	 * @return 是否移动到一下字符
	 */
	protected final boolean nextChar() {
		if (pos + 1 < text.length()) {
			ch = text.charAt(++pos);
			return true;
		}
		return false;
	}

	/**
	 * 如果没有下一个字符则报错，否则移动到下一个位置并返回true
	 * 
	 * @return 是否移动到一下字符
	 */
	protected final boolean nextCharOrError() {
		if (nextChar()) {
			return true;
		}
		throw new RuntimeException(error);
	}

	/**
	 * 如果下一个字符是指定的字符，则移动下一个位置并返回true，否则停留在当前位置且返回false
	 * 
	 * @param nextChar
	 * @return 是否为指定字符
	 */
	protected final boolean ifnext(char nextChar) {
		if (nextChar()) {
			if (nextChar == ch) {
				return true;
			} else {
				unnext();
			}
		}
		return false;
	}

	protected final boolean ifnext(char nch1, char nch2) {
		if (nextChar()) {
			if (nch1 == ch || nch2 == ch) {
				return true;
			} else {
				unnext();
			}
		}
		return false;
	}

	protected void unnext() {
		pos--;
		ch = pos >= 0 ? text.charAt(pos) : '\0';
	}

	protected boolean isAfter(char ch) {
		int i = pos + 1;
		return i < text.length() ? text.charAt(pos + 1) == ch : false;
	}

	protected boolean isAfterAt(char ch, int offset) {
		int i = pos + offset;
		return i < text.length() ? text.charAt(i) == ch : false;
	}

	protected boolean isAfterOr(char ch1, char ch2) {
		int i = pos + 1;
		int len = text.length();
		if (i < len) {
			return (text.charAt(i) == ch1)
					|| ((++i) < len ? text.charAt(i) == ch2 : false);
		}
		return false;
	}

	protected boolean isBefore(char ch) {
		int i = pos - 1;
		return i >= 0 ? text.charAt(i) == ch : false;
	}

	protected boolean isFirstIdentifierAt(int position) {
		return position >= 0 && position <= text.length() ? isFirstIdentifierChar(text
				.charAt(position)) : false;
	}

	protected boolean isBeforeAt(char bch, int offset) {
		int i = pos - offset;
		return i >= 0 ? text.charAt(i) == bch : false;
	}

	protected boolean isEOF() {
		return pos >= text.length();
	}

	protected void assertChar(char ch1) {
		if (ch != ch1) {
			throw new RuntimeException("assertChar :" + ch + " != " + ch1);
		}
	}

	/**
	 * 扫描没有转移字符的字符串
	 */
	protected void scanString() {
		if (escapeChar == ch) { // 转移符与字符串定符相同
			scanSqlString();
		} else {
			scanStringWithEscape();
		}
	}

	/**
	 * 扫描带有转移字符的字符串
	 */
	protected void scanStringWithEscape() {
		int features = 0;
		int ech = escapeChar;
		int strch = ch;
		error = "unclosed.str.lit";
		while (nextCharOrError()) {
			if (ch == ech) {
				features = Feature.ESCAPE;
				nextChar();
			} else if (ch == strch) {
				break;
			}
		}
		token.set(LITERAL_CHARS, features);
	}

	/**
	 * 扫描SQL中的NCHAR字符串
	 */
	protected void scanNChars() {
		scanSqlString();
		token.type = TokenType.LITERAL_NCHARS;
	}

	public void scanAlias() {
		scanString();
		token.type = TokenType.LITERAL_ALIAS;
	}

	/**
	 * 扫描SQL字符串(两个单引号为转移字符一个单引号字符，不作字符串边界定义)
	 */
	public void scanSqlString() {
		int features = 0;
		char sch = ch;
		error = "unclosed.str.lit";
		while (nextCharOrError()) {
			if (ch == sch) {
				if (ifnext(sch)) {
					features = Feature.ESCAPE;
					continue;
				}
				break;
			}
		}
		token.set(LITERAL_CHARS, features);
	}

	/**
	 * 从当前位置开始，跳过所有空白字符并停留在最后一个空白字符的位置
	 * 
	 * @return 是否跳过空白字符
	 */
	protected void scanWhitespace() {
		if (ch <= ' ') {
			while (nextChar()) {
				if (ch > ' ') { // !Character.isWhitespace(ch)
					unnext(); // 回退到最后空白字符的位置
					break;
				}
			}
			token.set(TokenType.BLANK);
		}
	}

	/**
	 * 扫描标识符
	 * 
	 */
	protected void scanIdentifier() {
		if (!isFirstIdentifierChar(ch)) {
			throw new ParserException("illegal identifier");
		}

		while (nextChar()) {
			if (!isIdentifierChar(ch)) {
				unnext();
				break;
			}
		}
		int count = pos - mark + 1;
		TokenType tok = keywods.getKeyword(text, mark, count);
		if (tok == null) {
			tok = TokenType.IDENTIFIER;
		}

		token.set(tok, mark, count, 0);
	}

	/**
	 * 扫描注释
	 */
	protected void scanComment() {
		boolean hasComment = false;
		if ((ch == '/' && isAfter('/')) || (ch == '-' && isAfter('-'))) {

			nextChar();
			while (nextChar()) {
				if (ch == '\r') {
					ifnext('\n');
					break;
				} else if (ch == '\n') {
					break;
				}
			}
			token.set(TokenType.LINE_COMMENT, mark, pos - mark + 1);
		} else if (ch == '/' && isAfter('*')) {
			nextChar();
			while (nextChar()) {
				if (ch == '*' && ifnext('/')) {
					hasComment = true;
					break;
				}
			}
			if (!hasComment) {
				throw new ParserException("syntax error at end of input.");
			}
			token.set(TokenType.MULTI_LINE_COMMENT, mark, pos - mark + 1);

		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * 扫描数字
	 */
	protected void scanNumber() {
		boolean hasDot = false;
		boolean scientific = false;
		ifnext('-');
		while (nextChar()) {
			if (ch >= '0' && ch <= '9') {
				continue;
			} else if (ch == '.') {
				if (isAfter('.')) { // 连续两个点被看着字符串连接符
					unnext(); // 退回到最后数字位置,让两个点作为操作符
					break;
				}
				if (scientific || hasDot) { // 有'E'在点'.'之前也算错误
					throw new ParserException("syntax error(not a number).");
				}
				hasDot = true;
			} else if (ch == 'e' || ch == 'E') {
				ifnext('+', '-');
				if (scientific) {
					throw new ParserException("syntax error(not a number).");
				}
				scientific = true;
			} else {
				unnext(); // 回退到最后数字字符的位置
				break;
			}
		}

		if (hasDot) {
			token.set(TokenType.LITERAL_FLOAT);
		} else {
			token.set(TokenType.LITERAL_INT);
		}
	}

	/**
	 * 扫描HexaDecimal
	 */
	protected void scanHexaDecimal() {
		ifnext('-');
		while (nextChar()) {
			if (!CharTypes.isHex(ch)) {
				unnext();
				break;
			}
		}
		token.set(TokenType.LITERAL_HEX);
	}

	/**
	 * 扫描变量
	 */
	protected void scanVariable() {
		if (ch != '@' && ch != ':' && ch != '#' && ch != '$') {
			throw new ParserException("illegal variable");
		}

		if (nextChar()) {
			if (ch == '{') {
				while (nextChar()) {
					if (ch == '}') {
						break;
					} else if (ch == '"' || ch == '\'') {
						this.scanString();
					}
				}
				if (ch != '}') {
					throw new ParserException("syntax error");
				}
			} else {
				if (ch != '@') {
					unnext();
				}
				while (nextChar()) {
					if (!isIdentifierChar(ch)) {
						break;
					}
				}
				unnext(); // 回退到最后的字符
			}
		} else {
			throw new ParserException("illegal variable(EOF)");
		}

		token.set(TokenType.VARIANT);
	}

	/**
	 * 扫描非字符字符
	 * 
	 */
	protected void scanNonLetter() {
		switch (ch) {
		case ',':
		case '，':
			token.set(COMMA);
			break;
		case '(':
			token.set(LPAREN);
			break;
		case ')':
			token.set(RPAREN);
			break;
		case '[':
			token.set(TokenType.LBRACKET);
			break;
		case ']':
			token.set(RBRACKET);
			break;
		case '{':
			token.set(TokenType.LBRACE);
			break;
		case '}':
			token.set(TokenType.RBRACE);
			break;
		case ':':
			nextChar();
			if (ch == '=') {
				token.set(TokenType.COLONEQ);
			} else if (ch == ':') {
				token.set(TokenType.COLONCOLON);
			} else {
				if (isDigit(ch)) {
					scanVariable();
				} else {
					scanVariable();
				}
			}
			break;
		case '.':
			nextChar();
			if (isDigit(ch) && !isFirstIdentifierAt(pos - 2)) {
				unnext();
				scanNumber(); // TODO 这么扫描会漏掉了'.'
				break;
			} else if (ch == '.') {
				if (ifnext('.')) {
					token.set(TokenType.DOTDOTDOT);
				} else {
					token.set(TokenType.DOTDOT);
				}
			} else {
				unnext();
				token.set(TokenType.DOT);
			}
			break;
		case '\'':
			scanString();
			break;
		case '\"':
			scanAlias();
			break;
		case '*':
			token.set(TokenType.STAR);
			break;
		case '?':
			token.set(TokenType.QUES);
			break;
		case ';':
			token.set(TokenType.SEMI);
			break;
		case '`':
			throw new ParserException("TODO");
		case '@':
			scanVariable();
			break;
		case '-':
			if (isAfter('-')) {
				scanComment();
			} else {
				scanOperator();
			}
			break;
		case '/':
			if (isAfterOr('/', '*')) {
				scanComment();
			} else {
				token.set(TokenType.SLASH);
			}
			break;
		default:
			throw new ParserException("TODO");
		}
	}

	/**
	 * 扫描操作符
	 * 
	 */
	protected void scanOperator() {
		switch (ch) {
		case '+':
			token.set(TokenType.PLUS);
			break;
		case '-':
			token.set(TokenType.SUB);
			break;
		case '*':
			token.set(TokenType.STAR);
			break;
		case '/':
			token.set(TokenType.SLASH);
			break;
		case '&':
			if (ifnext('&')) {
				token.set(TokenType.AMPAMP);
			} else {
				token.set(TokenType.AMP);
			}
			break;
		case '|':
			if (ifnext('|')) {
				if (ifnext('/')) {
					token.set(TokenType.BARBARSLASH);
				} else {
					token.set(TokenType.BARBAR);
				}
			} else if (ifnext('/')) {
				token.set(TokenType.BARSLASH);
			} else {
				token.set(TokenType.BAR);
			}
			break;
		case '^':
			token.set(TokenType.CARET);
			break;
		case '%':
			token.set(TokenType.PERCENT);
			break;
		case '=':
			if (ifnext('=')) {
				token.set(TokenType.EQEQ);
			} else {
				token.set(TokenType.EQ);
			}
			break;
		case '>':
			if (ifnext('=')) {
				token.set(TokenType.GTEQ);
			} else if (ifnext('>')) {
				token.set(TokenType.GTGT);
			} else {
				token.set(TokenType.GT);
			}
			break;
		case '<':
			nextChar();
			if (ch == '=') {
				if (ifnext('>')) {
					token.set(TokenType.LTEQGT);
				} else {
					token.set(TokenType.LTEQ);
				}
			} else if (ch == '>') {
				token.set(TokenType.LTGT);
			} else if (ch == '<') {
				token.set(TokenType.LTLT);
			} else {
				unnext();
				token.set(TokenType.LT);
			}
			break;
		case '!':
			nextChar();
			if (ch == '=') {
				token.set(TokenType.BANGEQ);
			} else if (ch == '>') {
				token.set(TokenType.BANGGT);
			} else if (ch == '<') {
				token.set(TokenType.BANGLT);
			} else if (ch == '!') {
				token.set(TokenType.BANGBANG); // postsql
			} else {
				unnext();
				token.set(TokenType.BANG);
			}
			break;
		case '?':
			token.set(TokenType.QUES);
			break;
		case '~':
			token.set(TokenType.TILDE);
			break;
		default:
			throw new ParserException("TODO");
		}
	}

	/**
	 * 是否为操作符
	 * 
	 * @param ch
	 * @return
	 */
	protected boolean isOperator(char ch) {
		switch (ch) {
		case '!':
		case '%':
		case '&':
		case '*':
		case '+':
		case '-':
		case '<':
		case '=':
		case '>':
		case '^':
		case '|':
		case '~':
		case ';':
			return true;
		default:
			return false;
		}
	}

	protected boolean isNonLetter(char ch) {
		switch (ch) {
		case ',':
		case '，':
		case '(':
		case ')':
		case '[':
		case ']':
		case '{':
		case '}':
		case ':':
		case '.':
		case '\'':
		case '"':
		case '*':
		case '?':
		case ';':
		case '`':
		case '@':
		case '-':
		case '/':
			return true;
		default:
			return false;
		}
	}

	protected void lexerError(String key, Object... args) {
		token.set(ERROR);
		throw new RuntimeException(key);
	}

	protected final boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * 指定字符是否为空白字符
	 * 
	 * @param ch
	 * @return
	 */
	protected boolean isWhitespace(char ch) {
		return ch <= ' '; // Character.isWhitespace(ch)
	}

	/**
	 * 扫描下一个Token
	 */
	protected void scanToken() {
		markNext();
		if (nextChar()) {
			if (isWhitespace(ch)) {
				// 空白字符串
				scanWhitespace();
			} else if (ch == '$' && isAfter('{')) {
				// 变量
				scanVariable();
			} else if (isFirstIdentifierChar(ch)) {
				// SQL NCHAR字符串
				if (ch == 'N') {
					if (ifnext('\'')) {
						scanNChars();
						return;
					}
				}
				// 标识符
				scanIdentifier();
			} else if (ch == '0' && ifnext('x')) {
				// 十六进制
				nextChar();
				scanHexaDecimal();
			} else if (isDigit(ch)) {
				// 数字
				scanNumber();
			} else if (ch == '"' || ch == '\'') {
				// 字符串
				scanString();
			} else if (Character.isLetter(ch)) {
				// 标识符
				scanIdentifier();
			} else if (isNonLetter(ch)) {
				// 特殊字符
				scanNonLetter();
			} else if (isOperator(ch)) {
				// 操作符
				scanOperator();
			} else {
				lexerError("illegal.char", String.valueOf((int) ch));
			}
		} else {
			token.set(EOF);
		}

	}

	/**
	 * Token
	 * 
	 * @author agan
	 *
	 */
	protected class BaseToken implements Token {
		private TokenType type;
		private int start;
		private int count;
		private int features;

		public Token set(TokenType type) {
			this.type = type;
			this.start = mark;
			this.count = pos - mark + 1;
			this.features = 0;
			return this;
		}

		public Token set(TokenType type, int features) {
			this.type = type;
			this.start = mark;
			this.count = pos - mark + 1;
			this.features = features;
			return this;
		}

		public Token set(TokenType type, int start, int count) {
			this.type = type;
			this.start = start;
			this.count = count;
			this.features = 0;
			return this;
		}

		public Token set(TokenType type, int start, int count, int features) {
			this.type = type;
			this.start = start;
			this.count = count;
			this.features = features;
			return this;
		}

		public Token addFeatures(int features) {
			this.features |= features;
			return this;
		}

		public boolean hasFeature(int feature) {
			return (this.features & feature) == feature;
		}

		@Override
		public boolean isType(TokenType type) {
			return type != null && this.type == type;
		}

		@Override
		public TokenType getType() {
			return type;
		}

		@Override
		public String getValue() {
			return text.substring(start, start + count);
		}

		@Override
		public String toString() {
			return type.name() + "(" + start + ", " + count + ")["+getValue()+"]";
		}
	}
}
