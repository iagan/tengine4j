package org.ithinking.tengine;

import org.ithinking.tengine.core.*;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.LoaderFactory;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import java.util.Locale;

/**
 * @author fuchujian
 */
//  org.springframework.web.servlet.view.AbstractCachingViewResolver
public class TengineViewResolver extends WebApplicationObjectSupport implements ViewResolver, org.springframework.core.Ordered {

    // org.slf4j.LoggerFactory
    //private static final Logger vrlogger = LoggerFactory.getLogger(ThymeleafViewResolver.class);

    public static final String REDIRECT_URL_PREFIX = "redirect:";
    public static final String FORWARD_URL_PREFIX = "forward:";
    private boolean redirectContextRelative = true;
    private boolean redirectHttp10Compatible = true;

    private TemplateEngine manager = null;

    private String prefix;
    private String suffix;
    private String charset;
    private String docBase;
    private boolean remoteView = false;
    private Configuration conf;

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

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

    public boolean isRemoteView() {
        return remoteView;
    }

    public void setRemoteView(boolean remoteView) {
        this.remoteView = remoteView;
    }

    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
        conf = Configuration.newConfiguration();
        //
        this.docBase = XString.defVal(this.docBase, servletContext.getRealPath(servletContext.getContextPath()));
        this.charset = XString.defVal(this.charset, conf.getViewCharset(), "UTF-8");
        this.prefix = XString.defVal(this.prefix, conf.getViewPrefix(), "classpath:");
        this.suffix = XString.defVal(this.suffix, conf.getViewSuffix(), ".html");
        //
        conf.setViewDocBase(docBase);
        conf.setViewCharset(this.charset);
        conf.setViewPrefix(this.prefix);
        conf.setViewSuffix(this.suffix);
        //
        Loader loader = LoaderFactory.createLoader(conf);
        HtmlParser parser = new HtmlParser();
        if (!this.isRemoteView()) {
            // 非远程视图，即本地视图
            manager = new TemplateEngine(loader, conf, parser);
        }
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
            //vrlogger.trace("[TENGINE4J] View \"{}\" cannot be handled by ThymeleafViewResolver. Passing on to the next resolver in the chain.", viewName);
            return null;
        } else if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            //vrlogger.trace("[TENGINE4J] View \"{}\" is a redirect, and will not be handled directly by ThymeleafViewResolver.", viewName);
            final String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            final RedirectView view = new RedirectView(redirectUrl, redirectContextRelative, redirectHttp10Compatible);
            view.setApplicationContext(getApplicationContext());
            return view;
        } else if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            //vrlogger.trace("[TENGINE4J] View \"{}\" is a forward, and will not be handled directly by ThymeleafViewResolver.", viewName);
            final String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            return new InternalResourceView(forwardUrl);
        }

        if (this.isRemoteView()) {
            return this.resolveRemoteView(viewName, locale);
        } else {
            return resolveLocalView(viewName, locale);
        }
    }

    private View resolveRemoteView(String viewName, Locale locale) {
        return new RemoteTengineView(locale, viewName, conf);
    }

    private View resolveLocalView(String viewName, Locale locale) {
        Template template = manager.getTemplate(viewName, locale);
        View view = null;
        if (template != null) {
            if (template.getBindingView() == null) {
                template.setBindingView(new TengineView(template, manager, locale));
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
