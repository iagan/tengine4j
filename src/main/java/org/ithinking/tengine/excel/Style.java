package org.ithinking.tengine.excel;

import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 * 样式
 *
 * @author agan
 * @date 2016-10-03
 */
public class Style {
    // 字体名称/类型
    private String fontFamily;
    // 字体大小
    private String fontSize;
    // 字体粗细
    private String fontWeight;
    // 字体颜色
    private String color;
    // 字体风格(斜体/正常)
    private String fontStyle;
    // 下划线样式
    private String underline;


    // 单元格对齐方式 Alignment.CENTRE
    private String align;
    // 背景颜色
    private String background;
    // Border

    // 高度(行高)
    private Integer height;
    // 宽度(列宽)
    private Integer width;

    // 字体
    private WritableFont font;

    // 单元格格式化
    private WritableCellFormat format;


    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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
}
