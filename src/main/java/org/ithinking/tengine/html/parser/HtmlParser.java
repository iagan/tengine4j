package org.ithinking.tengine.html.parser;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Parser;
import org.ithinking.tengine.html.Document;

public class HtmlParser implements Parser<Document> {

    public Document parse(String text, Configuration conf) {
        // new DebugHandler()
        Document doc = null;
        if (XString.isNotBlank(text)) {
            Handler handler = new HtmlHandler();
            HtmlScanner scanner = new HtmlScanner(text);
            scanner.setHandler(handler);
            scanner.start();
            doc = handler.getRootBlock();
            if (doc != null) {
                doc.init(conf);
            }
        }
        return doc;
    }
}
