package org.ithinking.tengine.html;

import java.util.Arrays;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Context;

public class Text extends AbstractRender {

	private int mark = 0;
	private char[] values = new char[20];

	public Text() {
	}

	public Text append(String text, int start, int end) {
		int count = end - start + 1;
		if (count > values.length - mark) {
			ensureCapacity(values.length + count + 20);
		}
		text.getChars(start, end + 1, values, mark);
		mark += count;
		return this;
	}

	/**
	 * 扩容
	 * 
	 * @jdk 1.6
	 * @param len
	 */
	private void ensureCapacity(int newCapacity) {
		values = Arrays.copyOf(values, newCapacity);
	}
	
	@Override
	public void render(Context context) {
		context.write(values, 0, mark);
	}
}
