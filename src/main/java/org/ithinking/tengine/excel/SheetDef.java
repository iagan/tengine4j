package org.ithinking.tengine.excel;

import jxl.write.WritableSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Sheet 模板订单
 *
 * @author agan
 * @date 2016-10-02
 */
public class SheetDef extends NodeDef {
    // 默认行高
    private Integer rowHeight;
    // 默认列宽
    private Integer colWidth;

    private String name;


    private List<RowDef> rowDefs;

    public void add(RowDef rowDef) {
        if (rowDefs == null) {
            rowDefs = new ArrayList<>();
        }
        rowDefs.add(rowDef);
    }

    /**
     * 每一个Sheet,行号和列号都要重置
     *
     * @param context
     */
    @Override
    protected void startDef(ExcelContext context) {
        context.setCurrentRow(0);
        context.setCurrentCol(0);
    }

    @Override
    protected void endDef(ExcelContext context) {
        context.setCurrentRow(0);
        context.setCurrentCol(0);
    }

    @Override
    public void createOne(ExcelContext context, Object dataOne) {
        WritableSheet currentSheet = ExcelHelper.createSheet(context.getWorkbook(), this, this.getIndex());
        context.setCurrentSheet(currentSheet);
        // 每个sheet都要重置行下标,从0开始计数
        if (rowDefs != null && !rowDefs.isEmpty()) {
            for (RowDef rowDef : rowDefs) {
                rowDef.create(context);
            }
        }
    }

    public Integer getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
    }

    public Integer getColWidth() {
        return colWidth;
    }

    public void setColWidth(Integer colWidth) {
        this.colWidth = colWidth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RowDef> getRowDefs() {
        return rowDefs;
    }

    public void setRowDefs(List<RowDef> rowDefs) {
        this.rowDefs = rowDefs;
    }

    public int getDefRowCount() {
        return this.rowDefs == null ? 0 : this.rowDefs.size();
    }

}
