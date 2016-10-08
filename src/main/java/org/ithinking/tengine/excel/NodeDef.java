package org.ithinking.tengine.excel;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

/**
 * Excel节点定义
 *
 * @author agan
 * @date 2016-10-08
 */
public abstract class NodeDef {

    private Expression forExpr;

    private Expression ifExpr;

    private Expression text;

    private Expression continueExpr;

    private Style style;


    /**
     * 根据订单创建Excel
     *
     * @param context
     */
    public abstract void create(ExcelContext context);


    public void setForeach(String exp) {
        if (XString.isNotBlank(exp)) {
            forExpr = ExpressionFactory.createExpression(exp);
        }
    }

    public void setIfExpr(String exp) {
        if (XString.isNotBlank(exp)) {
            ifExpr = ExpressionFactory.createExpression(exp);
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

}
