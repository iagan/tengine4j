package org.ithinking.tengine.excel;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Excel节点定义
 *
 * @author agan
 * @date 2016-10-08
 */
public abstract class NodeDef {

    private Integer index;

    private Expression forExpr;

    private Expression ifExpr;

    private Expression text;

    private Expression continueExpr;

    private Style style;

    private String forVarName;


    protected abstract int getOffset(ExcelContext context);

    protected abstract void createOne(ExcelContext context, Object dataOne, int offset);


    private boolean isTrue(Object obj) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        } else if (obj instanceof String) {
            return XString.isNotBlank((String) obj);
        } else if (obj instanceof Map) {
            return !((Map) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return ((Object[]) obj).length > 0;
        } else if (obj instanceof Collection) {
            return !((Collection) obj).isEmpty();
        } else {
            return obj != null;
        }
    }

    /**
     * if指令
     *
     * @param context
     * @return
     */
    public boolean ifTrue(ExcelContext context) {
        // 没有设置if指令时，都可以渲染
        if (ifExpr == null) {
            return true;
        }
        return isTrue(ifExpr.execute(context));
    }


    /**
     * Continue 指令
     *
     * @param context
     * @return
     */
    public boolean isContinue(ExcelContext context) {
        if (continueExpr == null) {
            return true;
        }
        return isTrue(continueExpr.execute(context));
    }

    /**
     * for指令
     *
     * @param context
     * @return
     */
    public boolean isForeach(ExcelContext context) {
        return forExpr != null;
    }


    /**
     * 循环for数据源
     *
     * @param context
     */
    public void foreach(ExcelContext context) {
        Object source = forExpr.execute(context);
        String varName = forVarName;
        Object dataOne;
        if (source != null) {
            int offset = this.getOffset(context);
            if (source instanceof List) {
                // 列表
                List list = (List) source;
                for (int i = 0; i < list.size(); i++) {
                    dataOne = list.get(i);
                    context.add(varName, dataOne);
                    if (isContinue(context)) {
                        context.setCurrentRow(offset);
                        this.createOne(context, dataOne, offset);
                        offset++;
                    }
                }
            } else if (source.getClass().isArray()) {
                // 数组
                Object[] array = (Object[]) source;
                for (int i = 0; i < array.length; i++) {
                    dataOne = array[i];
                    context.add(varName, dataOne);
                    if (isContinue(context)) {
                        context.setCurrentRow(offset);
                        this.createOne(context, dataOne, offset);
                        offset++;
                    }
                }
            }
        }
    }


    /**
     * 根据订单创建Excel
     *
     * @param context
     */
    public void create(ExcelContext context) {
        if (ifTrue(context)) {
            if (isForeach(context)) {
                foreach(context);
            } else {
                createOne(context, null, 0);
            }
        }
    }

    public Object getTextValue(ExcelContext context) {
        return getTextValue(context, null);
    }

    public Object getTextValue(ExcelContext context, String def) {
        if (this.text == null) {
            return "";
        }
        Object val = text.execute(context);
        return val == null ? def : val;
    }

    public void setIfExpr(String exp) {
        if (XString.isNotBlank(exp)) {
            ifExpr = ExpressionFactory.createExpression(exp);
        }
    }

    public void setForeach(String varName, String exp) {
        if (XString.isNotBlank(exp)) {
            forExpr = ExpressionFactory.createExpression(exp);
            forVarName = XString.isBlank(varName) ? "$item" : varName.trim();
        }
    }

    public void setText(String exp) {
        if (XString.isNotBlank(exp)) {
            text = ExpressionFactory.createCompositeExpression(exp);
        }
    }

    public void setContinueExpr(String exp) {
        if (XString.isNotBlank(exp)) {
            continueExpr = ExpressionFactory.createExpression(exp);
        }
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
