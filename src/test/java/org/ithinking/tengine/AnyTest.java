package org.ithinking.tengine;

import org.junit.Test;

public class AnyTest {

	@Test
	public void test() {
		String name = "user";
		name = this.toHumpName(name);
		System.out.println(name);
	}

	private String toHumpName(String name) {
		int i = 0;
		while ((i = name.indexOf("-")) != -1) {
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
		}
		return name;
	}
}
