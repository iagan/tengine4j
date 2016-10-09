package org.ithinking.tengine.excel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.OutputStream;
import java.util.List;

/**
 * ExcelBuilder
 *
 * @author agan
 * @date 2016-10-02
 */
public class ExcelBuilder {

    private OutputStream os;

    public void builder(OutputStream os, String tplText) throws Exception {
        this.os = os;
        ExcelParser reader = new ExcelParser();
        WorkbookDef workbookDef = reader.parse(tplText);

        ExcelContext context = new ExcelContext(null);
        context.setOs(os);

        workbookDef.create(context);





        WritableWorkbook writableWorkbook = createWritableWorkbook(workbookDef);
        List<SheetDef> sheetDefs = workbookDef.getSheetDefs();
        for (int i = 0, iLen = sheetDefs.size(); i < iLen; i++) {
            SheetDef sheetDef = sheetDefs.get(i);
            WritableSheet writableSheet = ExcelHelper.createSheet(writableWorkbook, sheetDef, i);
            //
            List<RowDef> rowDefs = sheetDef.getRowDefs();
            for (int j = 0, row = 0, jLen = rowDefs.size(); j < jLen; j++) {
                RowDef rowDef = rowDefs.get(j);
                if (rowDef.getIndex() != null) {
                    row += rowDef.getIndex();

                }
                this.formatRow(writableSheet, row, rowDef);
                //
                List<CellDef> cellDefs = rowDef.getCellDefs();
                for (int k = 0, col = 0, kLen = cellDefs.size(); k < kLen; k++) {
                    CellDef cellDef = cellDefs.get(k);
                    if (cellDef.getIndex() != null) {
                        col += cellDef.getIndex();
                    }
                    createCell(writableSheet, col, row, cellDef);
                    col++;
                }
                row++;
            }
        }

        writableWorkbook.write();
        writableWorkbook.close();
    }

    public WritableWorkbook createWritableWorkbook(WorkbookDef workbookDef) throws Exception {
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        return wwb;
    }



    public void formatRow(WritableSheet sheet, int row, RowDef rowDef) throws Exception {
        //设置行高
        if (rowDef.getHeight() != null) {
            sheet.setRowView(row, rowDef.getHeight(), false);
        }
    }

    public Label createCell(WritableSheet sheet, int col, int row, CellDef cellDef) throws Exception {
        String title = cellDef.getText();

        /**
         // 定义格式
         WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

         WritableCellFormat wcfTitle = new WritableCellFormat(wf2);
         //象牙白
         wcfTitle.setBackground(jxl.format.Colour.IVORY);
         //BorderLineStyle边框
         wcfTitle.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
         // wcfTitle.setVerticalAlignment(VerticalAlignment.CENTRE); //设置垂直对齐
         //设置垂直对齐
         wcfTitle.setAlignment(Alignment.CENTRE);
         Label label = new Label(col, row, title, wcfTitle);
         **/
        Label label = new Label(col, row, title);
        sheet.addCell(label);
        return label;
    }
}
