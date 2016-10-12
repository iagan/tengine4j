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

    private Integer width;

    @Override
    protected int getOffset(ExcelContext context) {
        return context.getCurrentCol() + this.getIndex();
    }

    @Override
    protected void createOne(ExcelContext context, Object dataOne, int offset) {
        try {
            Label label = createCell(context);
            if (this.getWidth() != null) {
                context.getCurrentSheet().setColumnView(offset, this.getWidth());
            }
            context.getCurrentSheet().addCell(label);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public Label createCell(ExcelContext context) throws Exception {
        Object val = this.getTextValue(context);

        WritableFont font = null;
        WritableCellFormat format = null;

        if (this.getStyle() != null) {
            font = this.getStyle().getFont();
            format = this.getStyle().getFormat();
        }

        Label label;
        if (format != null) {
            label = new Label(context.getCurrentCol(), context.getCurrentRow(), val.toString(), format);
        } else {
            label = new Label(context.getCurrentCol(), context.getCurrentRow(), val.toString());
        }
        return label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
