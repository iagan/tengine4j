package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateEngine;
import org.ithinking.tengine.excel.ExcelContext;
import org.ithinking.tengine.excel.ExcelParser;
import org.ithinking.tengine.excel.WorkbookDef;
import org.ithinking.tengine.html.Tag;
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
    protected String excelViewName;

    public TengineView(Template template, TemplateEngine engine, Locale locale, String viewName) {
        this.engine = engine;
        this.template = template;
        this.locale = locale;
        this.viewName = viewName;
        // eg: 123.xls
        int i = this.viewName.lastIndexOf(".xls");
        if (i != -1 && i != this.viewName.length() - 4) {
            this.excelViewName = this.viewName.substring(0, i + 4);
        }
    }

    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.viewName.indexOf(".xls.xml") != -1) {
            this.renderExcel(model, request, response);
        } else {
            this.renderHtml(model, request, response);
        }
    }

    public void renderHtml(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
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

    public void renderExcel(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //
        ExcelParser excelParser = new ExcelParser();
        WorkbookDef workbookDef = excelParser.parse(this.template.getResource().getText());
        ExcelContext context = new ExcelContext(engine, request, response, charset);
        context.putAll(model);
        context.setOs(response.getOutputStream());

        // 设置输出
        String fileName = workbookDef.getName(context, excelViewName);
        if (XString.isBlank(fileName)) {
            fileName = XString.isBlank(excelViewName) ? System.currentTimeMillis() + ".xls" : fileName;
        }
        fileName = new String((fileName).getBytes("gb2312"), "iso8859-1") + ".xls";
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("utf-8");
        //
        workbookDef.create(context);
    }
}
