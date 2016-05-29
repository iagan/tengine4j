package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateEngine;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;

public class DefContext extends HashMap<String, Object> implements Context {


    private Charset charset = Charset.forName("UTF-8");
    private TemplateEngine engine;
    private Locale locale;

    public DefContext(TemplateEngine engine) {
        super();
        this.engine = engine;
    }

    public DefContext(TemplateEngine engine, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.engine = engine;
    }


    public DefContext(TemplateEngine engine, int initialCapacity) {
        super(initialCapacity);
        this.engine = engine;
    }

    @Override
    public Context add(String key, Object value){
        this.put(key, value);
        return this;
    }

    @Override
    public Context write(String s) {
        System.out.print(s);
        return this;
    }

    @Override
    public Context write(String s, int start, int len) {
        System.out.print(s.substring(start, start + len));
        return this;
    }

    @Override
    public Context write(char[] values, int start, int len) {
        System.out.print(String.copyValueOf(values, start, start + len));
        return this;
    }

    @Override
    public Context write(byte[] bytes) {
        write(bytes, 0, bytes.length);
        return this;
    }

    @Override
    public Context write(byte[] bytes, int start, int len) {
        String text = new String(bytes, start, start + len, charset);
        System.out.print(text);
        return this;
    }

    @Override
    public Context writeHeader(String name, String value) {
        System.out.println(name + ": " + value);
        return this;
    }

    @Override
    public Template loadTemplate(String path) {
        return engine.getTemplate(path, locale);
    }
}
