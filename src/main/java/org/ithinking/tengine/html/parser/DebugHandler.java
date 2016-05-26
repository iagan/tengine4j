package org.ithinking.tengine.html.parser;

import org.ithinking.tengine.core.TAG;
import org.ithinking.tengine.html.Document;

public class DebugHandler implements Handler {

	
	public void start() {
		println("=============start================");
	}

	public void pi(String text, int start, int end) {
		println("PI: " + text.substring(start, end + 1));
	}

	public void docType(String text, int start, int end) {
		println("DOCTYPE: " + text.substring(start, end + 1));
	}

	public void comment(String text, int start, int end) {
		println("COMMENT: " + text.substring(start, end + 1));
	}

	public void cdata(String text, int start, int end) {
		println("CDATA: " + text.substring(start, end + 1));
	}

	public void startTag(String text, int start, int end, int features) {
		println("START-TAG: " + text.substring(start, end + 1));
	}

	public void endTag(String text,int start, int end, int features) {
		if((features & TAG.F_FIX_CLOSE) == TAG.F_FIX_CLOSE){
			println("[FIX-CLOSE-TAG]");
		}else if((features & TAG.F_SELF_CLOSE) == TAG.F_SELF_CLOSE){
			println("[SELF-CLOSE-TAG]");
		}else{
			println("END-TAG: " + text.substring(start, end + 1));
		}
	}

	public void plainText(String text,int start, int end) {
		println("TEXT: " + text.substring(start, end + 1));
	}

	public void end() {
		println("=================end==================");
	}
	
	private void println(String text){
		//System.out.println(text);
	}

	public Document getRootBlock() {
		return null;
	}
}
