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
        Loader loader = new ClasspathLoader(conf);
        HtmlParser parser = new HtmlParser();

        this.contextRealPath = servletContext.getRealPath(servletContext.getContextPath());

        this.charset = defVal(this.charset, conf.getViewCharset(), "UTF-8");
        this.prefix = defVal(this.prefix, conf.getViewPrefix(), "UTF-8");
        this.suffix = defVal(this.suffix, conf.getViewSuffix(), "UTF-8");

        docRoot = makePath(contextRealPath , prefix);
        if (!docRoot.endsWith(File.separator)) {
            docRoot += File.separator;
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
        String viewPath;
        if (viewName.endsWith(suffix)) {
            viewPath = makePath(docRoot, viewName);
        } else {
            viewPath = makePath(docRoot, viewName + suffix);
        }
        Template template = manager.getTemplate(viewPath);
        if (template != null) {
            return new TengineView(template, manager);
        }
        return null;
    }

    private String makePath(String parent, String file) {
        int length = parent.length();
        int totalLength = length + file.length();
        char ch;
        int count = 0;
        String target = parent;
        StringBuilder sb = new StringBuilder(parent.length() + file.length());
        for (int i = 0; i < totalLength; i++) {
            if (i >= length) {
                ch = file.charAt(i - length);
            } else {
                ch = target.charAt(i);
            }

            if (ch == '/' || ch == '\\') {
                if (count == 0) {
                    sb.append(File.separator);
                }
                count++;
                continue;
            }
            count = 0;
            sb.append(ch);
        }
        return sb.toString();
    }

    private String defVal(String v1, String v2, String v3) {
        if (v1 != null && !v1.trim().isEmpty()) {
            return v1.trim();
        }
        if (v2 != null && !v2.trim().isEmpty()) {
            return v2.trim();
        }
        if (v3 != null && !v3.trim().isEmpty()) {
            return v3.trim();
        }
        return "";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
