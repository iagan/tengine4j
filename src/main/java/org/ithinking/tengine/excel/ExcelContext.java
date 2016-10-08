package org.ithinking.tengine.excel;

import jxl.write.WritableWorkbook;

import java.io.OutputStream;

/**
 * Excel上下文
 *
 * @author agan
 * @date 2016-10-08
 */
public class ExcelContext {

    private OutputStream os;

    private WritableWorkbook workbook;

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public WritableWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(WritableWorkbook workbook) {
        this.workbook = workbook;
    }
}
