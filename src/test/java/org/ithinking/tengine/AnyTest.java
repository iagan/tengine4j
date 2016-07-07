package org.ithinking.tengine;

import org.junit.Test;

import java.io.File;

public class AnyTest {

	@Test
	public void test() {
		String name = "user";
		name = this.toHumpName(name);
		System.out.println(name);
	}

    @Test
    public void testJavaString(){
        String s = "skfjskdjf";
        //String s2 = new String(s, 0, 2);
    }

	@Test
	public void testReplaceAll(){
		String parent = "d:\\doc/777//9889\\909090\\\\989898////7777//5555\\\\\\\\";
		String file = "//doc\\\\index.html";
		String path = makePath(parent, file);
		System.out.println(path);
	}

	public String makePath(String parent, String file) {
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

	private String toHumpName(String name) {
		int i = 0;
		while ((i = name.indexOf("-")) != -1) {
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
		}
		return name;
	}
}
