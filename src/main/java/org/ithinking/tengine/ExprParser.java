package org.ithinking.tengine;

public class ExprParser {

	public static void parseText(String text, Handler handler) {
		parseText(text, 0, text.length() - 1, handler);
	}

	public static void parseText(String text, int start, Handler handler) {
		parseText(text, start, text.length() - 1, handler);
	}

	public static void parseText(String text, int start, int end, Handler handler) {
		int ch = -1, xch = -1;
		int expStart = -1;
		int lastPos = start;
		int charCount = 0;
		for (int i = start; i < end + 1; i++) {
			ch = text.charAt(i);
			if (ch == '$' && i < end && text.charAt(i + 1) == '{') { // 新的表达式开始
				if (expStart != -1) {
					throw new RuntimeException("表达式不允许嵌套");
				}
				expStart = i;
				//
				if (charCount > 0) {
					handler.plainText(text, lastPos, charCount);
					charCount = 0;
				}
			} else if (expStart != -1 && (ch == '\'' || ch == '"')) { // 表达式中的字符串
				while (i < end && (xch = text.charAt(++i)) != ch)
					;
				if (xch != ch) {
					throw new RuntimeException("表达式解析错误,字符串为正确定义");
				}

			} else if (expStart != -1 && ch == '}') { // 表达式结束
				handler.expression(text, expStart + 2, i - (expStart + 2));
				expStart = -1;
				charCount = 0;
				lastPos = i + 1; // 表达式结束符'}'的下一个位置
			} else if (expStart == -1) {
				charCount++;
			}
		}
		if (expStart != -1) {
			throw new RuntimeException("表达式解析错误，未正常结尾");
		}
		if (charCount > 0) {
			handler.plainText(text, lastPos, charCount);
		}
	}

	public static interface Handler {
		public void plainText(String text, int start, int count);

		public void expression(String text, int start, int count);
	}
}
