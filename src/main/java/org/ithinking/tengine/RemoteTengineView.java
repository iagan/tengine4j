package org.ithinking.tengine;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateEngine;
import org.ithinking.tengine.loader.RemoteDynamicHostLoader;

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
    protected Configuration conf;

    public RemoteTengineView(Template template, TemplateEngine engine, Locale locale, String viewName) {
        super(template, engine, locale, viewName);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RemoteDynamicHostLoader.setRemoteIp(WEB.getRemoteIP(request));
        this.template = engine.getTemplate(viewName, locale);
        Context context = new HttpServletRequestContext(engine, request, response, charset);
        context.putAll(model);
        if (template != null) {
            template.render(context);
        }
    }
}
