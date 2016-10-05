package org.ithinking.tengine.html;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.DIRECTIVE;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

public class Attr extends AbstractRender {
    private String name;
    private String param;
    private String param1;
    private String param2;
    private String param3;
    private String value;
    private String strChar = "";
    private DIRECTIVE type;
    private int valueType = 0;
    private Expression valueExpression;
    private boolean isChecked = false;
    private boolean isSelected = false;

    public Attr() {

    }

    public Attr(String name) {
        this.name = name;
    }

    private boolean isBlank(String src) {
        int len = src == null ? 0 : src.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                if (!Character.isWhitespace(src.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isTrue(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue() != 0;
        } else if (obj instanceof String) {
            String val = obj.toString();
            if (isBlank(val) || "0".equals(val) || "false".equals(val)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(Context ctx) {
        if (isChecked || isSelected) {
            Object val = valueExpression.execute(ctx);
            if (isTrue(val)) {
                ctx.write(" ").write(isChecked ? "checked" : "selected");
            }
        } else {
            ctx.write(" ").write(this.name).write("=");
            ctx.write(getStrChar());
            renderValue(ctx);
            ctx.write(getStrChar());
        }
    }

    @Override
    protected void innerInit(Configuration conf) {
        if (this.type != DIRECTIVE.ANY_ATTR) { // 非指令
            if (valueExpression == null) {
                valueExpression = ExpressionFactory.createExpression(value, valueType != 0);
                if (valueExpression != null) {
                    valueExpression.init(conf);
                }
            }
        } else if (valueIsExpr()) { // 指令
            valueExpression = ExpressionFactory.createExpression(value, true);
        }

        if (valueExpression != null) {
            this.isChecked = "checked".equalsIgnoreCase(this.name);
            this.isSelected = "selected".equalsIgnoreCase(this.name);
        }
    }

    // 判断值是否为表达式
    private boolean valueIsExpr() {
        int i = this.value == null ? -1 : this.value.indexOf("${");
        return i != -1 && this.value.indexOf('}', i + 1) != -1;
    }

    /**
     * 渲染值部分
     *
     * @param ctx
     */
    public void renderValue(Context ctx) {
        if (valueExpression != null) {
            valueExpression.executeAndWrite(ctx);
        } else {
            ctx.write(value);
        }

        /**
         if (this.type == DIRECTIVE.ANY_ATTR) {
         ctx.write(value);
         } else {
         if (valueExpression != null) {
         valueExpression.executeAndWrite(ctx);
         }
         }
         **/
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getStrChar() {
        return strChar;
    }

    public void setStrChar(String strChar) {
        this.strChar = strChar;
    }

    public DIRECTIVE getType() {
        return type;
    }

    public void setType(DIRECTIVE type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

}
