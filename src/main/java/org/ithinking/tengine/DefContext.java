package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateManager;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefContext implements Context {

	private Map<String, Object> values = new HashMap<String, Object>();
	private Charset charset = Charset.forName("UTF-8");
	private TemplateManager manager;
	private Locale locale;
	public DefContext(TemplateManager manager){
		this.manager = manager;
	}
	
	public DefContext(){
		this(null);
	}

	@Override
	public Context add(String key, Object value) {
		values.put(key, value);
		return this;
	}

	@Override
	public Object get(String key) {
		return values.get(key);
	}

	@Override
	public Map<String, Object> values() {
		return values;
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
	public Context write(char[] values, int start, int len){
		System.out.print(String.copyValueOf(values, start, start + len));
		return this;
	}

	@Override
	public Context write(byte[] bytes){
		write(bytes, 0, bytes.length);
		return this;
	}
	
	@Override
	public Context write(byte[] bytes, int start, int len){
		String text = new String(bytes, start, start + len, charset);
		System.out.print(text);
		return this;
	}

	@Override
	public Context writeHeader(String name, String value){
		System.out.println(name + ": " + value);
		return this;
	}

	@Override
	public Template loadTemplate(String path) {
		return manager.getTemplate(path, locale);
	}
}
