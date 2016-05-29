package org.ithinking.tengine;

import org.ithinking.tengine.core.*;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.LoaderFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author agan
 */
public class TengineServlet implements Servlet {

    private TemplateEngine engine = null;

    private ServletConfig servletConfig;

    private Configuration conf;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        conf = Configuration.newConfiguration();

        String realRoot = config.getServletContext().getRealPath(config.getServletContext().getContextPath());
        String docBase = config.getInitParameter("tg.view.docBase");
        String prefix = config.getInitParameter("tg.view.prefix");
        String suffix = config.getInitParameter("tg.view.suffix");
        String charset = config.getInitParameter("tg.view.charset");

        if (XString.isNotBlank(docBase)) {
            conf.setViewDocBase(docBase);
        } else {
            conf.setViewDocBase(realRoot);
        }

        if (XString.isNotBlank(prefix)) {
            conf.setViewPrefix(prefix);
        }
        if (XString.isNotBlank(charset)) {
            conf.setViewCharset(charset);
        }

        if (XString.isNotBlank(suffix)) {
            conf.setViewSuffix(suffix);
        }

        Loader loader = LoaderFactory.createLoader(conf);
        HtmlParser parser = new HtmlParser();
        engine = new TemplateEngine(loader, conf, parser);
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        if (!(req instanceof HttpServletRequest)) {
            throw new ServletException("Unknown request:" + req);
        }
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        Context context = new HttpServletRequestContext(engine, request, response, conf.getViewCharset());
        String path = request.getServletPath();
        Template template = context.loadTemplate(path);
        if (template != null) {
            template.render(context);
        }
    }

    @Override
    public String getServletInfo() {
        return "TengineServlet";
    }

    @Override
    public void destroy() {

    }
}
