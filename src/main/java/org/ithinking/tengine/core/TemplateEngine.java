package org.ithinking.tengine.core;

import org.ithinking.tengine.html.Document;
import org.ithinking.tengine.html.parser.HtmlParser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateEngine {

    private static Map<String, Template> TEMPLATES = new HashMap<String, Template>();

    protected Loader loader;
    protected HtmlParser parser;
    protected Configuration conf;

    public TemplateEngine(Loader loader, Configuration conf, HtmlParser parser) {
        this.conf = conf;
        this.loader = loader;
        this.parser = parser;
    }


    public Template getTemplate(String id, Locale locale) {
        Template tpl = TEMPLATES.get(id);
        if (tpl != null && !tpl.isModified()) {
            return tpl;
        } else {
            Resource res = loader.load(id);
            if (res != null) {
                Document doc = parser.parse(res.getText(), conf);
                if (doc != null) {
                    tpl = new Template();
                    tpl.setResource(res);
                    tpl.setDocument(doc);
                    TEMPLATES.put(id, tpl);
                }
            }
        }
        return tpl;
    }
}
