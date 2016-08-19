package org.ithinking.tengine;

import org.ithinking.tengine.core.*;
import org.ithinking.tengine.loader.ProxyLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * 远程获取视图
 *
 * @author agan
 * @date 2016-08-19
 */
public class RemoteTengineView extends TengineView {

    protected String viewName = null;
    protected Configuration conf;

    public RemoteTengineView(Template template, TemplateEngine engine, Locale locale) {
        super(template, engine, locale);
    }

    public RemoteTengineView(Locale locale, String viewName, Configuration conf) {
        this(null, null, locale);
        this.viewName = viewName;
        this.conf = conf;
    }


    private String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.trim().isEmpty()) {
            ip = request.getHeader("X-real-ip");
        } else {
            int i = ip.indexOf(",");
            ip = i > 0 ? ip.substring(0, i) : ip;
        }

        if (ip == null || ip.trim().isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.engine = new RemoteTemplateEngine(conf, new ProxyLoader(getRemoteIP(request), charset));
        this.template = engine.getTemplate(viewName, locale);
        Context context = new HttpServletRequestContext(engine, request, response, charset);
        context.putAll(model);
        if (template != null) {
            template.render(context);
        }
    }
}
