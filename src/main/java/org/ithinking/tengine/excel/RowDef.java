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

    private Integer index;

    private List<CellDef> cellDefs;


    public void add(CellDef cellDef) {
        if (cellDefs == null) {
            cellDefs = new ArrayList<>();
        }
        cellDefs.add(cellDef);
    }

    @Override
    protected void createOne(ExcelContext context, Object dataOne, int offset) {
        applyStyle(context);
        // 每行都要重置列下标，从0开始计数
        int col = 0;
        if (cellDefs != null && !cellDefs.isEmpty()) {
            for (CellDef cellDef : cellDefs) {
                context.setCurrentCol(col++);
                cellDef.create(context);
            }
        }
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<CellDef> getCellDefs() {
        return cellDefs;
    }

    public void setCellDefs(List<CellDef> cellDefs) {
        this.cellDefs = cellDefs;
    }
}
