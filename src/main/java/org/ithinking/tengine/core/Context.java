package org.ithinking.tengine.core;

import java.io.IOException;
import java.util.Map;

public interface Context {

    Context add(String key, Object value);

    Context add(Map<String, ?> items);

    Object get(String key);

    Map<String, Object> values();

    Template loadTemplate(String path);

    Context writeHeader(String name, String value);

    Context write(String s);

    Context write(String s, int start, int len);

    Context write(char[] values, int start, int len);

    Context write(byte[] bytes) throws IOException;

    Context write(byte[] bytes, int start, int len);
}
