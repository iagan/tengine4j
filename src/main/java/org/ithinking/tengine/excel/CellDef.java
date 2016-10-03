package org.ithinking.tengine.excel;

import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 * CellDef
 *
 * @author agan
 * @date 2016-10-02
 */
public class CellDef {
    // 单元格内容
    private String text;
    // 单元格位置
    private Integer index;
    // 单元格宽度
    private Integer width;
    // 数据类型
    private String type;

    // 字体
    private WritableFont font;

    // 单元格格式化
    private WritableCellFormat format;

    private Style style;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WritableFont getFont() {
        return font;
    }

    public void setFont(WritableFont font) {
        this.font = font;
    }

    public WritableCellFormat getFormat() {
        return format;
    }

    public void setFormat(WritableCellFormat format) {
        this.format = format;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
