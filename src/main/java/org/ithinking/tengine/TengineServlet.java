package org.ithinking.tengine;

import org.ithinking.tengine.core.*;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.ClasspathLoader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author agan
 */
public class TengineServlet implements Servlet{

    private TemplateManager manager = null;

    private ServletConfig servletConfig;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        Configuration conf = new Configuration();
        Loader loader = new ClasspathLoader(conf);
        HtmlParser parser = new HtmlParser();
        manager = new TemplateManager(loader, conf, parser);
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
        Context context = new HttpServletRequestContext(manager, request, response);
        String path = request.getServletPath();
        Template template = context.loadTemplate(path);
        if(template != null){
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
