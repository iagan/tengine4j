package org.ithinking.tengine;

import org.ithinking.tengine.core.*;
import org.ithinking.tengine.loader.RemoteLoader;

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


    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.engine = new RemoteTemplateEngine(conf, new RemoteLoader(WEB.getRemoteIP(request), charset));
        this.template = engine.getTemplate(viewName, locale);
        Context context = new HttpServletRequestContext(engine, request, response, charset);
        context.putAll(model);
        if (template != null) {
            template.render(context);
        }
    }
}
