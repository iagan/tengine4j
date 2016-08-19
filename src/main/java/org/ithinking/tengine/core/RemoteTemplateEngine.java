package org.ithinking.tengine.core;

import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.ProxyLoader;

/**
 * 远程视图引擎
 *
 * @author agan
 * @date 2016-08-20
 */
public class RemoteTemplateEngine extends TemplateEngine {
    public RemoteTemplateEngine(Loader loader, Configuration conf, HtmlParser parser) {
        super(loader, conf, parser);
    }

    public RemoteTemplateEngine(Configuration conf, ProxyLoader proxyLoader) {
        this(proxyLoader, conf, new HtmlParser());
    }
}
