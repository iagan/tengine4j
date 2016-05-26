package org.ithinking.tengine.html;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Context;

public class TextByte extends AbstractRender {
	private int mark = 0;
	private byte[] values = new byte[20];
	private Charset charset;

	public TextByte(String charset) {
		this.charset = Charset.forName(charset == null
				|| charset.trim().isEmpty() ? "UTF_8" : charset);
	}

	public TextByte append(String text, int start, int end) {
		byte[] source = text.substring(start, end + 1).getBytes(charset);
		int count = source.length;
		if (count > values.length - mark) {
			ensureCapacity(values.length + count + 20);
		}
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
