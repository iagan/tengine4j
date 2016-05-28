package org.ithinking.tengine;

import java.io.File;

public class XString {

	private static char[] value = null;

	public XString(String s) {
		value = s.toCharArray();
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public int indexOf(String text, String key) {
		return this.indexOf(text, 0, text.length(), key, false);
	}

	public int indexOf(String text, String key, boolean ignoreCase) {
		return this.indexOf(text, 0, text.length(), key, ignoreCase);
	}

	public int indexOf(String text, int start, String key, boolean ignoreCase) {
		return this
				.indexOf(text, start, text.length() - start, key, ignoreCase);
	}

	public int indexOf(String text, int start, int count, String key,
			boolean ignoreCase) {
		int ufirst = ignoreCase ? Character.toUpperCase(key.charAt(0)) : key
				.charAt(0);
		int lfirst = ignoreCase ? Character.toLowerCase((char) ufirst) : ufirst;
		int keylen = key.length();
		for (int i = start, ch, max = start + count; i < max; i++) {
			if (max - i < keylen) {
				return -1;
			}

			ch = text.charAt(i);
			if (ch == ufirst || ch == lfirst) {
				if (text.regionMatches(ignoreCase, i, key, 0, keylen)) {
					return i;
				}
			}
		}
		return -1;
	}

	public static String defVal(String v1, String v2) {
		if (v1 != null && !v1.trim().isEmpty()) {
			return v1.trim();
		}
		if (v2 != null && !v2.trim().isEmpty()) {
			return v2.trim();
		}
		return v2;
	}

	public static String defVal(String v1, String v2, String v3) {
		if (v1 != null && !v1.trim().isEmpty()) {
			return v1.trim();
		}
		if (v2 != null && !v2.trim().isEmpty()) {
			return v2.trim();
		}
		if (v3 != null && !v3.trim().isEmpty()) {
			return v3.trim();
		}
		return v3;
	}

	public static String makePath(String parent, String file) {
		int length = parent.length();
		int totalLength = length + file.length();
		char ch;
		int count = 0;
		String target = parent;
		StringBuilder sb = new StringBuilder(parent.length() + file.length());
		for (int i = 0; i < totalLength; i++) {
			if (i >= length) {
				ch = file.charAt(i - length);
			} else {
				ch = target.charAt(i);
			}

			if (ch == '/' || ch == '\\') {
				if (count == 0) {
					sb.append(File.separator);
				}
				count++;
				continue;
			}
			count = 0;
			sb.append(ch);
		}
		return sb.toString();
	}

	public static boolean isBlank(String src){
		return src == null || src.trim().isEmpty();
	}

	public static boolean isNotBlank(String src){
		return !isBlank(src);
	}
}
