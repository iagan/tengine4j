package org.ithinking.tengine.excel;

import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableFont;
import org.ithinking.tengine.XString;
import org.ithinking.tengine.exception.ParserException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

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
            ByteArrayInputStream bis = new ByteArrayInputStream(text.getBytes("utf-8"));
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

    /**
     * 读取公共指令和Style
     *
     * @param nodeDef
     * @param elm
     */
    private void readNodeDef(NodeDef nodeDef, Element elm) {
        NamedNodeMap attrs = elm.getAttributes();
        for (int i = 0, len = attrs == null ? 0 : attrs.getLength(); i < len; i++) {
            Node attr = attrs.item(i);
            String name = attr.getNodeName();
            String val = attr.getNodeValue();
            if (XString.isBlank(val)) {
                continue;
            }
            if ("if".equalsIgnoreCase(name)) {
                nodeDef.setIfExpr(val);
            } else if ("continue".equalsIgnoreCase(name)) {
                nodeDef.setContinueExpr(val);
            } else if ("for".equalsIgnoreCase(name)) {
                nodeDef.setForeach(null, val);
            } else if (name.startsWith("for-")) {
                String param = name.substring(4);
                nodeDef.setForeach(XString.toHumpName(param), val);
            } else if ("index".equalsIgnoreCase(name)) {
                nodeDef.setIndex(Integer.parseInt(val));
            } else if ("width".equals(name)) {

            } else if ("style".equalsIgnoreCase(name)) {
                Style style = parseStyle(val);
                if (style != null) {
                    nodeDef.setStyle(style);
                    style.setFont(createWritableFont(style));
                }
            }

        }


        String text = readOnlyText(elm);
        if (XString.isNotBlank(text)) {
            nodeDef.setText(text);
        }

    }

    private WorkbookDef createWorkbookDef(Element workbook) {
        WorkbookDef workbookDef = new WorkbookDef();
        //
        readNodeDef(workbookDef, workbook);
        //
        return workbookDef;
    }

    private SheetDef createSheetDef(Element sheetElm) {
        SheetDef sheetDef = new SheetDef();
        //
        sheetDef.setName(readString(sheetElm, "name"));
        sheetDef.setRowHeight(readInt(sheetElm, "row-height"));
        sheetDef.setColWidth(readInt(sheetElm, "col-width"));
        //
        readNodeDef(sheetDef, sheetElm);
        return sheetDef;
    }

    private RowDef createRowDef(Element rowElm) {
        RowDef rowDef = new RowDef();
        rowDef.setIndex(readInt(rowElm, "index"));
        rowDef.setHeight(readInt(rowElm, "height"));
        //
        readNodeDef(rowDef, rowElm);
        //
        return rowDef;
    }

    private CellDef createCellDef(Element cellElm) {
        CellDef cellDef = new CellDef();
        String type = readString(cellElm, "type");
        cellDef.setType(type);
        //
        readNodeDef(cellDef, cellElm);
        //
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

    /**
     * 获取元素的文本值，如果元素包含子元素，则返回null
     *
     * @param elm
     * @return
     */
    private String readOnlyText(Element elm) {
        NodeList nodeList = elm.getChildNodes();
        StringBuilder sb = new StringBuilder();
        Node node;
        for (int i = 0, len = nodeList == null ? 0 : nodeList.getLength(); i < len; i++) {
            node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return null;
            }
            if(node.getNodeType() == Node.TEXT_NODE){
                sb.append(node.getNodeValue());
            }

        }
        return sb.toString();
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


    /**
     * 创建字体
     *
     * @param style
     * @return
     * @throws Exception
     */
    private WritableFont createWritableFont(Style style) {
        try {
            if (style == null) {
                return null;
            }
            WritableFont writableFont = null;
            // 字体名称
            WritableFont.FontName fontName = null;
            // 字体颜色
            Colour colour = null;
            // 字体大小
            Integer fontSize = null;
            // 下划线
            UnderlineStyle underlineStyle = null;
            // 是否粗体
            Boolean boldStyle = null;
            // 是否斜体
            Boolean italic = null;

            /**
             * 字体名称
             */
            if (XString.isNotBlank(style.getFontFamily())) {
                if ("Arial".equalsIgnoreCase(style.getFontFamily())) {
                    fontName = WritableFont.ARIAL;
                } else if ("Times New Roman".equalsIgnoreCase(style.getFontFamily())) {
                    fontName = WritableFont.TIMES;
                } else if ("Courier New".equalsIgnoreCase(style.getFontFamily())) {
                    fontName = WritableFont.COURIER;
                } else if ("Tahoma".equalsIgnoreCase(style.getFontFamily())) {
                    fontName = WritableFont.TAHOMA;
                } else {
                    fontName = WritableFont.createFont(style.getFontFamily());
                }
            }

            /**
             * 字体颜色
             */
            if (XString.isNotBlank(style.getColor())) {
                Colour[] colours = Colour.getAllColours();
                for (Colour c : colours) {
                    if (c.getDescription().equalsIgnoreCase(style.getColor())) {
                        colour = c;
                        break;
                    }
                }
            }

            /**
             * 字体大小
             */
            if (XString.isNotBlank(style.getFontSize())) {
                fontSize = Integer.parseInt(style.getFontSize());
            }

            /**
             * 下划线
             */
            if (XString.isNotBlank(style.getUnderline())) {
                if (UnderlineStyle.NO_UNDERLINE.getDescription().equalsIgnoreCase(style.getUnderline())) {
                    underlineStyle = UnderlineStyle.NO_UNDERLINE;
                }
                if (UnderlineStyle.DOUBLE.getDescription().equalsIgnoreCase(style.getUnderline())) {
                    underlineStyle = UnderlineStyle.DOUBLE;
                }
                if (UnderlineStyle.DOUBLE_ACCOUNTING.getDescription().equalsIgnoreCase(style.getUnderline())) {
                    underlineStyle = UnderlineStyle.DOUBLE_ACCOUNTING;
                }
                if (UnderlineStyle.SINGLE.getDescription().equalsIgnoreCase(style.getUnderline())) {
                    underlineStyle = UnderlineStyle.SINGLE;
                }
                if (UnderlineStyle.SINGLE_ACCOUNTING.getDescription().equalsIgnoreCase(style.getUnderline())) {
                    underlineStyle = UnderlineStyle.SINGLE_ACCOUNTING;
                }
            }

            /**
             * 是否粗体
             */
            if (XString.isNotBlank(style.getFontWeight())) {
                if ("none".equalsIgnoreCase(style.getFontWeight())) {
                    boldStyle = false;
                } else if ("bold".equalsIgnoreCase(style.getFontWeight())) {
                    boldStyle = true;
                } else if ("Normal".equalsIgnoreCase(style.getFontWeight())) {
                    boldStyle = false;
                }
            }

            /**
             * 字体风格(正常/斜体)
             */
            if (XString.isNotBlank(style.getFontStyle())) {
                if ("normal".equalsIgnoreCase(style.getFontStyle())) {
                    italic = false;
                } else if ("italic".equalsIgnoreCase(style.getFontStyle())) {
                    italic = true;
                } else if ("oblique".equalsIgnoreCase(style.getFontStyle())) {
                    // italic = true;
                }
            }


            if (fontName != null || colour != null || fontSize != null || underlineStyle != null || boldStyle != null || italic != null) {
                if (fontName == null) {
                    fontName = WritableFont.ARIAL;
                    writableFont = new WritableFont(fontName);

                }
                if (colour != null) {
                    writableFont.setColour(colour);
                }
                if (fontSize != null) {
                    writableFont.setPointSize(fontSize);
                }
                if (underlineStyle != null) {
                    writableFont.setUnderlineStyle(underlineStyle);
                }

                if (italic != null) {
                    writableFont.setItalic(italic);
                }

                if (boldStyle != null) {
                    if (boldStyle == true) {
                        writableFont.setBoldStyle(WritableFont.BOLD);
                    } else {
                        writableFont.setBoldStyle(WritableFont.NO_BOLD);
                    }
                }
            }
            return writableFont;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
