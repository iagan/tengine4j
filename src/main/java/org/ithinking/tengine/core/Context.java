package org.ithinking.tengine.core;

import java.io.IOException;
import java.util.Map;

public interface Context {

	public Context add(String key, Object value);
	
	public Object get(String key);
	
	public Map<String,Object> values();
	
	public Template loadTemplate(String path);
	
	public Context writeHeader(String name, String value);
	
	public Context write(String s);
	
	public Context write(String s, int start, int len);
	
	public Context write(char[] values, int start, int len);
	
	public Context write(byte[] bytes) throws IOException;
	
	public Context write(byte[] bytes, int start, int len);
}
