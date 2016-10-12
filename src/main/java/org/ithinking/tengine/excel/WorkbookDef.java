package org.ithinking.tengine.excel;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Workbook 模板定义
 *
 * @author agan
 * @date 2016-10-02
 */
public class WorkbookDef extends NodeDef {

    private Expression name;

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

    @Override
    protected int getOffset(ExcelContext context) {
        return 0;
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

    public String getName(ExcelContext context, String def) {
        if (name == null) {
            return def;
        }
        Object val = name.execute(context);
        return val == null ? def : val.toString();
    }

    public void setName(String name) {
        if (name == null) {
            this.name = null;
        } else {
            this.name = ExpressionFactory.createCompositeExpression(name);
        }
    }


    public List<SheetDef> getSheetDefs() {
        return sheetDefs;
    }

    public void setSheetDefs(List<SheetDef> sheetDefs) {
        this.sheetDefs = sheetDefs;
    }

    public int getDefSheetCount() {
        return this.sheetDefs == null ? 0 : this.sheetDefs.size();
    }
}
