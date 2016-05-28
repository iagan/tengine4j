package org.ithinking.tengine;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Loader;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateManager;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.ClasspathLoader;
import org.ithinking.tengine.loader.FilepathLoader;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Locale;

/**
 * @author fuchujian
 */
//  org.springframework.web.servlet.view.AbstractCachingViewResolver
public class TengineViewResolver extends WebApplicationObjectSupport implements ViewResolver, org.springframework.core.Ordered {

    private TemplateManager manager = null;

    private String prefix;
    private String suffix;
    private String charset;
    private String contextRealPath;
    private String docRoot;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
        Configuration conf = new Configuration();


        this.contextRealPath = servletContext.getRealPath(servletContext.getContextPath());

        this.charset = XString.defVal(this.charset, conf.getViewCharset(), "UTF-8");
        this.prefix = XString.defVal(this.prefix, conf.getViewPrefix(), "classpath");
        this.suffix = XString.defVal(this.suffix, conf.getViewSuffix(), ".html");

        docRoot = XString.makePath(contextRealPath, prefix);
        if (!docRoot.endsWith(File.separator)) {
            docRoot += File.separator;
        }

        Loader loader = new ClasspathLoader(conf.getViewPrefix(), conf.getViewCharset());
        HtmlParser parser = new HtmlParser();

        if (prefix.toLowerCase().indexOf("classpath:") != 0) {
            loader = new FilepathLoader(docRoot, conf.getViewCharset());
        }

        manager = new TemplateManager(loader, conf, parser);
    }


    /**
     * @param viewName /index
     * @param locale
     * @return
     * @throws Exception
     */
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (!viewName.endsWith(suffix)) {
            viewName += suffix;
        }
        Template template = manager.getTemplate(viewName, locale);
        View view = null;
        if (template != null) {
            if (template.getBindingView() == null) {
                template.setBindingView(new TengineView(template, manager));
            }
            if (!(template.getBindingView() instanceof View)) {
                throw new RuntimeException("BindingView is not a spring View.");
            }
            view = (View) template.getBindingView();
        }
        return view;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
