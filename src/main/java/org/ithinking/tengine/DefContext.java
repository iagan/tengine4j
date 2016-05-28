package org.ithinking.tengine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateManager;

public class DefContext implements Context {

	private Map<String, Object> values = new HashMap<String, Object>();
	private Charset charset = Charset.forName("UTF-8");
	private TemplateManager manager;
	
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
	public Context write(String s, int start, int len) throws IOException {
		System.out.print(s.substring(start, len));
		return this;
	}
	
	@Override
	public Context write(char[] values, int start, int len) throws IOException{
		System.out.print(String.copyValueOf(values, start, len));
		return this;
	}

	@Override
	public Context write(byte[] bytes) throws IOException{
		write(bytes, 0, bytes.length);
		return this;
	}
	
	@Override
	public Context write(byte[] bytes, int start, int len) throws IOException{
		String text = new String(bytes, start, len, charset);
		write(text);
		return this;
	}

	@Override
	public Context writeHeader(String name, String value) throws IOException{
		System.out.println(name + ": " + value);
		return this;
	}

	@Override
	public Template loadTemplate(String path) {
		return manager.getTemplate(path);
	}
}
