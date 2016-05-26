package org.ithinking.tengine;

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
}
