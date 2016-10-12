package org.ithinking.tengine.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Row 模板定义
 *
 * @author agan
 * @date 2016-10-02
 */
public class RowDef extends NodeDef {
    // 行高
    private Integer height;

    private List<CellDef> cellDefs;


    public void add(CellDef cellDef) {
        if (cellDefs == null) {
            cellDefs = new ArrayList<>();
        }
        cellDefs.add(cellDef);
    }

    /**
     * 没开始一个行，都要设置行号偏移
     *
     * @param context
     */
    @Override
    protected void startDef(ExcelContext context) {
        context.setCurrentRow(context.getCurrentRow() + getIndex());
        context.setCurrentCol(0);
    }

    @Override
    protected void endDef(ExcelContext context) {

    }


    @Override
    protected void createOne(ExcelContext context, Object dataOne) {
        applyStyle(context);
        // 每行都要重置列下标，从0开始计数
        context.setCurrentCol(0);
        if (cellDefs != null && !cellDefs.isEmpty()) {
            for (CellDef cellDef : cellDefs) {
                cellDef.create(context);
                context.incrementCol();
            }
        }
        context.incrementRow();
    }


    private void applyStyle(ExcelContext context) {
        try {
            if (this.getHeight() != null) {
                context.getCurrentSheet().setRowView(context.getCurrentRow(), this.getHeight(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<CellDef> getCellDefs() {
        return cellDefs;
    }

    public void setCellDefs(List<CellDef> cellDefs) {
        this.cellDefs = cellDefs;
    }

    public int getDefCellCount() {
        return this.cellDefs == null ? 0 : cellDefs.size();
    }
}
