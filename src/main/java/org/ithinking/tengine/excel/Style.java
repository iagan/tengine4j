package org.ithinking.tengine.excel;

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
}
