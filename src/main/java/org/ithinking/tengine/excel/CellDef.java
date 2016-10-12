package org.ithinking.tengine.excel;

import jxl.write.Label;
import jxl.write.WritableCellFormat;

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

    // 跨行数量(单元格合并)
    private Integer rowspan;

    // 跨列数量(单元格合并)
    private Integer colspan;


    @Override
    protected void startDef(ExcelContext context) {
        if (this.getIndex() != null) {
            context.setCurrentCol(this.getIndex());
        } else {
            context.setCurrentCol(context.getCurrentCol() + getOffset());
        }
    }

    @Override
    protected void endDef(ExcelContext context) {

    }

    @Override
    protected void createOne(ExcelContext context, Object dataOne, int index) {
        try {
            int col1 = context.getCurrentCol();
            int row1 = context.getCurrentRow();
            Label label = createCell(context);
            if (this.getWidth() != null) {
                context.getCurrentSheet().setColumnView(col1, this.getWidth());
            }
            context.getCurrentSheet().addCell(label);
            // 指向下一行
            context.incrementCol();


            // 合并单元格
            if (this.rowspan != null && this.rowspan > 1) {
                if (this.colspan != null && this.colspan > 1) {
                    context.getCurrentSheet().mergeCells(col1, row1, col1 + colspan - 1, row1 + rowspan - 1);
                    context.setCurrentRow(row1 + rowspan);
                    context.setCurrentCol(col1 + colspan);
                } else {
                    context.getCurrentSheet().mergeCells(col1, row1, col1, row1 + rowspan - 1);
                    context.setCurrentRow(row1 + rowspan);
                }
            } else if (this.colspan != null && this.colspan > 1) {
                context.getCurrentSheet().mergeCells(col1, row1, col1 + colspan - 1, row1);
                context.setCurrentCol(col1 + colspan);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public Label createCell(ExcelContext context) throws Exception {
        Object val = this.getTextValue(context);
        WritableCellFormat format = null;
        if (this.getStyle() != null) {
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

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }
}
