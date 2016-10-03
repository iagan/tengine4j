package org.ithinking.tengine.excel;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.ithinking.tengine.exception.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Excel解析器
 *
 * @author agan
 * @date 2016-10-02
 */
public class ExcelParser {

    public WorkbookDef parse(String text) {
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            ByteInputStream bis = new ByteInputStream();
            bis.setBuf(text.getBytes("utf-8"));
            Document doc = builder.parse(bis);


            /**
             * workbook
             */
            Element workbook = doc.getDocumentElement();
            if (!"Workbook".equalsIgnoreCase(workbook.getTagName())) {
                throw new ParserException("根元素必须为Workbook");
            }

            // WritableWorkbook wwb = excelBuilder.createWritableWorkbook(workbook);
            WorkbookDef workbookDef = this.createWorkbookDef(workbook);

            /**
             * 1. Sheet遍历
             */
            NodeList sheetElmList = workbook.getChildNodes();
            for (int i = 0, len = sheetElmList.getLength(); i < len; i++) {
                Node node = sheetElmList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element sheetElm = (Element) (node);
                if (!"Sheet".equalsIgnoreCase(sheetElm.getTagName())) {
                    throw new ParserException("需要Sheet元素，并非" + sheetElm.getTagName());
                }

                SheetDef sheetDef = this.createSheetDef(sheetElm);
                workbookDef.add(sheetDef);

                /**
                 * 2. Row遍历
                 */
                NodeList rowElmList = sheetElm.getChildNodes();
                for (int j = 0, jLen = rowElmList.getLength(); j < jLen; j++) {
                    node = rowElmList.item(j);
                    if (node.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    Element rowElm = (Element) (node);
                    if (!"Row".equalsIgnoreCase(rowElm.getTagName())) {
                        throw new ParserException("需要Row元素，并非" + rowElm.getTagName());
                    }

                    RowDef rowDef = this.createRowDef(rowElm);
                    sheetDef.add(rowDef);


                    /**
                     * 3. 遍历Cell
                     */
                    NodeList cellElmList = rowElm.getChildNodes();
                    for (int k = 0, kLen = cellElmList.getLength(); k < kLen; k++) {
                        node = cellElmList.item(k);
                        if (node.getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }
                        Element cellElm = (Element) (node);
                        if (!"Cell".equalsIgnoreCase(cellElm.getTagName())) {
                            throw new ParserException("需要Cell元素，并非" + cellElm.getTagName());
                        }

                        CellDef cellDef = createCellDef(cellElm);
                        rowDef.add(cellDef);
                    }
                }
            }
            return workbookDef;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private WorkbookDef createWorkbookDef(Element workbook) {
        WorkbookDef workbookDef = new WorkbookDef();
        return workbookDef;
    }

    private SheetDef createSheetDef(Element sheetElm) {
        SheetDef sheetDef = new SheetDef();
        //
        sheetDef.setName(sheetElm.getAttribute("name"));
        sheetDef.setRowHeight(readInt(sheetElm, "row-height"));
        sheetDef.setColWidth(readInt(sheetElm, "col-width"));
        //
        return sheetDef;
    }

    private RowDef createRowDef(Element rowElm) {
        RowDef rowDef = new RowDef();
        rowDef.setIndex(readInt(rowElm, "index"));
        rowDef.setHeight(readInt(rowElm, "height"));
        return rowDef;
    }

    private CellDef createCellDef(Element cellElm) {
        CellDef cellDef = new CellDef();
        cellDef.setText(cellElm.getTextContent());
        cellDef.setIndex(readInt(cellElm, "index"));
        cellDef.setType(readString(cellElm, "type"));
        cellDef.setWidth(readInt(cellElm, "width"));
        //
        String styleStr = readString(cellElm, "style");
        if (styleStr != null && !styleStr.trim().isEmpty()) {
            Style style = parseStyle(styleStr);
            cellDef.setStyle(style);
        }
        return cellDef;
    }


    private Integer readInt(Element elm, String attr) {
        String value = elm.getAttribute(attr);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private String readString(Element elm, String attr) {
        String value = elm.getAttribute(attr);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private Style parseStyle(String styleStr) {
        if (styleStr == null || styleStr.trim().isEmpty()) {
            return null;
        }
        Style style = new Style();
        String[] styles = styleStr.split(";");
        String[] keyValue;
        for (String s : styles) {
            keyValue = s.split(":");
            String name = keyValue[0].trim();
            String val = keyValue[1].trim();
            //
            if ("font-family".equalsIgnoreCase(name)) {
                style.setFontFamily(val);
            } else if ("font-size".equalsIgnoreCase(name)) {
                style.setFontSize(val);
            } else if ("font-weight".equalsIgnoreCase(name)) {
                style.setFontWeight(val);
            } else if ("color".equalsIgnoreCase(name)) {
                style.setColor(val);
            } else if ("font-style".equalsIgnoreCase(name)) {
                style.setFontStyle(val);
            }
        }
        return style;
    }
}
