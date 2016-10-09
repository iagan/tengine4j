package org.ithinking.tengine.excel;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Workbook 模板定义
 *
 * @author agan
 * @date 2016-10-02
 */
public class WorkbookDef extends NodeDef {

    private List<SheetDef> sheetDefs;


    public void add(SheetDef sheetDef) {
        if (sheetDefs == null) {
            sheetDefs = new ArrayList<>();
        }
        sheetDefs.add(sheetDef);
    }

    @Override
    public void create(ExcelContext context) {
        WritableWorkbook wwb = null;
        try {
            wwb = Workbook.createWorkbook(context.getOs());
            context.setWorkbook(wwb);
            createSheets(context);
            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(wwb);
        }
    }

    @Override
    protected void createOne(ExcelContext context, Object dataOne, int offset) {

    }

    private void createSheets(ExcelContext context) {
        if (sheetDefs != null && !sheetDefs.isEmpty()) {
            for (SheetDef sheetDef : sheetDefs) {
                sheetDef.create(context);
            }
        }
    }

    private void close(WritableWorkbook wwb) {
        if (wwb != null) {
            try {
                wwb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public List<SheetDef> getSheetDefs() {
        return sheetDefs;
    }

    public void setSheetDefs(List<SheetDef> sheetDefs) {
        this.sheetDefs = sheetDefs;
    }
}
