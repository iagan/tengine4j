package org.ithinking.tengine.excel;

import jxl.write.WritableSheet;

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
        if (cellDefs != null && !cellDefs.isEmpty()) {
            for (CellDef cellDef : cellDefs) {
                cellDef.create(context);
            }
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
