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
	public void testReplaceAll(){
		String s = "d:\\doc/777//9889\\909090\\\\989898////7777//";
		String replacement = File.separator.equals("\\") ? "\\\\" :  File.separator;
		s = s.replaceAll("\\\\+|/+", replacement);
		System.out.println("pathSeparator: " + File.separator);
		System.out.println(s);
	}

	private String toHumpName(String name) {
		int i = 0;
		while ((i = name.indexOf("-")) != -1) {
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
		}
		return name;
	}
}
