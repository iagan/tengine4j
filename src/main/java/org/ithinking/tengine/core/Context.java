package org.ithinking.tengine.core;

import java.util.Map;

public interface Context extends Map<String,Object>{

    boolean checkPerm(String permCode);

    Context add(String key, Object value);

    Template loadTemplate(String path);

    Context writeHeader(String name, String value);

    Context write(String s);

    Context write(String s, int start, int len);

    Context write(char[] values, int start, int len);

    Context write(byte[] bytes);

    Context write(byte[] bytes, int start, int len);
}
