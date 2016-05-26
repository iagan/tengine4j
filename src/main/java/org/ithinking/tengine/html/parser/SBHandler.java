package org.ithinking.tengine.html.parser;

import org.ithinking.tengine.html.Document;

public class SBHandler implements Handler {

	private StringBuilder tmp = new StringBuilder();

	public void start() {

	}

	public void pi(String text, int start, int end) {
		tmp.append(text, start, end + 1);
	}

	public void docType(String text, int start, int end) {
		tmp.append(text, start, end + 1);
	}

	public void comment(String text, int start, int end) {
		tmp.append(text, start, end + 1);
	}

	public void cdata(String text, int start, int end) {
		tmp.append(text, start, end + 1);
	}

	public void startTag(String text, int start, int end, int features) {
		tmp.append(text, start, end + 1);
	}

	public void endTag(String text,int start, int end, int features) {
		tmp.append(text, start, end + 1);
	}

	public void plainText(String text,int start, int end) {
		tmp.append(text, start, end + 1);
	}

	public void end() {
		System.out.println(tmp.toString());
	}

	public Document getRootBlock() {
		return null;
	}
}
