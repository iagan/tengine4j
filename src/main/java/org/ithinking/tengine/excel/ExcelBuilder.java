package org.ithinking.tengine.excel;

import java.io.OutputStream;

/**
 * ExcelBuilder
 *
 * @author agan
 * @date 2016-10-02
 */
public class ExcelBuilder {

    private OutputStream os;

    public void builder(OutputStream os, String tplText) throws Exception {
        this.os = os;
        ExcelParser reader = new ExcelParser();
        WorkbookDef workbookDef = reader.parse(tplText);
        ExcelContext context = new ExcelContext(null);
        context.setOs(os);
        workbookDef.create(context);
    }
}
