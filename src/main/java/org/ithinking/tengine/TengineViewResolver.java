package org.ithinking.tengine;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Loader;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateManager;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.ClasspathLoader;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;
import java.util.Locale;

/**
 * @author fuchujian
 */
//  org.springframework.web.servlet.view.AbstractCachingViewResolver
public class TengineViewResolver extends WebApplicationObjectSupport implements ViewResolver, org.springframework.core.Ordered {

    private TemplateManager manager = null;

    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
        Configuration conf = new Configuration();
        Loader loader = new ClasspathLoader(conf);
        HtmlParser parser = new HtmlParser();
        manager = new TemplateManager(loader, conf, parser);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        Template template = manager.getTemplate(viewName);
        if (template != null) {
            return new TengineView(template, manager);
        }
        return null;
    }
}
