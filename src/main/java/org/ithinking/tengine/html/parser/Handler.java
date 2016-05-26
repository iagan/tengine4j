package org.ithinking.tengine.html.parser;

import org.ithinking.tengine.html.Document;

public interface Handler {

	public void start();

	public void pi(String text, int start, int end);

	public void docType(String text, int start, int end);

	public void comment(String text, int start, int end);

	public void cdata(String text, int start, int end);

	public void startTag(String text, int start, int end, int features);

	public void endTag(String text, int start, int end, int features);

	public void plainText(String text, int start, int end);

	public void end();
	
	public Document getRootBlock();

}
