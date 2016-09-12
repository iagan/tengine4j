package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateEngine;
import org.ithinking.tengine.html.Tag;
import org.ithinking.tengine.html.parser.HtmlScanner;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * @author agan
 */
public class TengineView implements View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
    protected TemplateEngine engine = null;
    protected Template template = null;
    protected String charset = null;
    protected String viewName = null;
    protected Locale locale;

    public TengineView(Template template, TemplateEngine engine, Locale locale, String viewName) {
        this.engine = engine;
        this.template = template;
        this.locale = locale;
        this.viewName = viewName;
    }

    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Context context = new HttpServletRequestContext(engine, request, response, charset);
        context.putAll(model);
        //
        String fragId = request.getParameter("_fragId");
        if (XString.isNotBlank(fragId)) {
            Tag tag = template.getDocument().getFragment(fragId);
            if (tag != null) {
                tag.render(context);
            }
        } else if (template != null) {
            template.render(context);
        }
    }
}
