package org.ithinking.tengine;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Loader;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateEngine;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.LoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import java.util.Locale;

/**
 * @author fuchujian
 */
//  org.springframework.web.servlet.view.AbstractCachingViewResolver
public class TengineViewResolver extends WebApplicationObjectSupport implements ViewResolver, org.springframework.core.Ordered {
    private static final Logger logger = LoggerFactory.getLogger(TengineViewResolver.class);
    //private static final Logger vrlogger = LoggerFactory.getLogger(ThymeleafViewResolver.class);

    public static final String REDIRECT_URL_PREFIX = "redirect:";
    public static final String FORWARD_URL_PREFIX = "forward:";
    private boolean redirectContextRelative = true;
    private boolean redirectHttp10Compatible = true;
    private TemplateEngine manager = null;
    private String docBasePath;
    private String suffix;
    private boolean isDynamicRemoteHost = false;

    private Configuration conf;


    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
        String ctxPath = servletContext.getRealPath(servletContext.getContextPath());
        conf = Configuration.getWebConfiguration();
        logger.info("[InitServletContext-1] prefix={},suffix={}, charset={}, ctxPath={}", conf.getViewPrefix(), conf.getViewSuffix(), conf.getViewCharset(), ctxPath);
        conf.setViewCharset(XString.defVal(conf.getViewCharset(), "UTF-8"));
        conf.setViewPrefix(XString.defVal(conf.getViewPrefix(), "/tpl"));
        conf.setViewSuffix(XString.defVal(conf.getViewSuffix(), ".html"));
        conf.setWebContextPath(ctxPath);
        //
        docBasePath = conf.getDocBase();
        suffix = conf.getViewSuffix();
        isDynamicRemoteHost = conf.isDynamicRemoteHost();
        logger.info("[InitServletContext-2] prefix={},suffix={}, charset={}, docBasePath={}, isDynamicRemoteUrl={}",
                conf.getViewPrefix(), suffix, conf.getViewCharset(), docBasePath, isDynamicRemoteHost);
        //
        Loader loader = LoaderFactory.createLoader(conf);
        logger.info("[InitServletContext] loader:{}", loader);
        HtmlParser parser = new HtmlParser();
        manager = new TemplateEngine(loader, conf, parser);
    }


    /**
     * @param viewName /index
     * @param locale
     * @return
     * @throws Exception
     */
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (!viewName.endsWith(suffix) && !"/error".equals(viewName) && !viewName.endsWith(".xsl.xml")) {
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
        if (this.isDynamicRemoteHost) {
            return this.resolveRemoteView(viewName, locale);
        } else {
            return resolveLocalView(viewName, locale);
        }
    }

    private View resolveRemoteView(String viewName, Locale locale) {
        return new RemoteTengineView(null, manager, locale, viewName);
    }

    private View resolveLocalView(String viewName, Locale locale) {
        Template template = manager.getTemplate(viewName, locale);
        View view = null;
        if (template != null) {
            if (template.getBindingView() == null) {
                template.setBindingView(new TengineView(template, manager, locale, viewName));
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
