package org.ithinking.tengine.html;

import java.util.Arrays;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;

public class Text extends AbstractRender {

    private int mark = 0;
    private char[] values = new char[20];
    private byte[] bytes;


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

    protected void innerInit(Configuration conf) {
        try {
            bytes = new String(values, 0, mark).getBytes(conf.getViewOutCharsetOrDefault());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 扩容
     *
     * @param newCapacity
     * @jdk 1.6
     */
    private void ensureCapacity(int newCapacity) {
        values = Arrays.copyOf(values, newCapacity);
    }

    @Override
    public void render(Context context) {
        context.write(bytes);
    }
}
