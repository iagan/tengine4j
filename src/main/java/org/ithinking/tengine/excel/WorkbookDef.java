package org.ithinking.tengine.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Workbook 模板定义
 *
 * @author agan
 * @date 2016-10-02
 */
public class WorkbookDef {

    private List<SheetDef> sheetDefs;


    public List<SheetDef> getSheetDefs() {
        return sheetDefs;
    }

    public void setSheetDefs(List<SheetDef> sheetDefs) {
        this.sheetDefs = sheetDefs;
    }

    public void add(SheetDef sheetDef) {
        if (sheetDefs == null) {
            sheetDefs = new ArrayList<>();
        }
        sheetDefs.add(sheetDef);
    }
}
