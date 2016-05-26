package org.ithinking.tengine.html.parser;

import org.ithinking.tengine.core.Parser;
import org.ithinking.tengine.html.Document;

public class HtmlParser implements Parser<Document> {

	public Document parse(String text) {
		// new DebugHandler()
		Handler handler = new HtmlHandler();
		HtmlScanner scanner = new HtmlScanner(text);
		scanner.setHandler(handler);
		scanner.start();
		Document doc = handler.getRootBlock();
		return doc;
	}

}
