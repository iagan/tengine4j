package org.ithinking.tengine.excel;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.ithinking.tengine.DefContext;
import org.ithinking.tengine.HttpServletRequestContext;
import org.ithinking.tengine.core.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Excel上下文
 *
 * @author agan
 * @date 2016-10-08
 */
public class ExcelContext extends HttpServletRequestContext {

    public ExcelContext(TemplateEngine engine,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        String charset) {
        super(engine, request, response, charset);
    }

    public ExcelContext(TemplateEngine engine) {
        super(engine, null, null, null);
    }

    private OutputStream os;

    private WritableWorkbook workbook;

    private WritableSheet currentSheet;

    private int currentRow = 0;
    private int currentCol = 0;

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

    public WritableSheet getCurrentSheet() {
        return currentSheet;
    }

    public void setCurrentSheet(WritableSheet currentSheet) {
        this.currentSheet = currentSheet;
    }

    public Integer getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public Integer getCurrentCol() {
        return currentCol;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public void incrementRow() {
        currentRow++;
    }

    public int getAndIncrementRow() {
        return currentRow++;
    }

    public void incrementCol() {
        currentCol++;
    }

    public int getAndIncrementCol() {
        return currentCol++;
    }
}
