package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateManager;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author agan
 */
public class TengineView implements View {

    private TemplateManager manager = null;
    private Template template = null;
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
    private String charset = null;

    public TengineView(Template template, TemplateManager manager) {
        this.manager = manager;
        this.template = template;
    }

    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Context context = new HttpServletRequestContext(manager, request, response, charset);
        context.add(model);
        if (template != null) {
            template.render(context);
        }
    }
}
