package org.ithinking.tengine.excel;

import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 * CellDef
 *
 * @author agan
 * @date 2016-10-02
 */
public class CellDef extends NodeDef {
    // 数据类型
    private String type;

    // 字体
    private WritableFont font;

    // 单元格格式化
    private WritableCellFormat format;


    @Override
    protected void createOne(ExcelContext context, Object dataOne, int offset) {
        try {
            Label label = createCell(context);
            context.getCurrentSheet().addCell(label);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public Label createCell(ExcelContext context) throws Exception {
        Object val = this.getTextValue(context);
        Label label = new Label(context.getCurrentCol(), context.getCurrentRow(), val.toString());
        return label;
    }

    private void applyStyle(ExcelContext context) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WritableFont getFont() {
        return font;
    }

    public void setFont(WritableFont font) {
        this.font = font;
    }

    public WritableCellFormat getFormat() {
        return format;
    }

    public void setFormat(WritableCellFormat format) {
        this.format = format;
    }


}
