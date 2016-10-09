package org.ithinking.tengine.excel;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-10-09
 */
public class ExcelHelper {

    public static WritableSheet createSheet(WritableWorkbook wwb, SheetDef sheetDef, int index) {

        String name = sheetDef.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "sheet";
        }

        WritableSheet sheet = wwb.createSheet(name, index);

        /**
         CellView navCellView = new CellView();
         navCellView.setAutosize(true); //设置自动大小
         navCellView.setSize(12);
         // sheet.mergeCells(0,0,navTitle.length-1,0);
         //设置col显示样式
         sheet.setColumnView(0, navCellView);
         **/


        Integer rowHeight = sheetDef.getRowHeight();
        if (rowHeight != null) {
            sheet.getSettings().setDefaultRowHeight(rowHeight);
        }

        Integer colWidth = sheetDef.getColWidth();
        if (colWidth != null) {
            sheet.getSettings().setDefaultColumnWidth(colWidth);
        }
        return sheet;
    }
}
