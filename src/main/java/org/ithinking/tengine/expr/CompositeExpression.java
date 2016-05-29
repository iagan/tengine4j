package org.ithinking.tengine.expr;

import java.util.ArrayList;
import java.util.List;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;

public class CompositeExpression extends ExpressionSupport {

    private List<Expression> exprList;
    private Expression onlyOne;

    @Override
    public void init(Configuration conf) {
        super.init(conf);
        if (exprList != null && exprList.size() == 1) {
            onlyOne = exprList.get(0);
        }
    }

    @Override
    public Object execute(Context context) {
        if (onlyOne != null) {
            return onlyOne.execute(context);
        }
        if (exprList != null) {
            StringBuilder sb = new StringBuilder();
            Object val;
            for (int i = 0, len = exprList.size(); i < len; i++) {
                val = exprList.get(i).execute(context);
                sb.append(val);
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    public boolean executeForBoolean(Context context) {
        if (onlyOne != null) {
            return onlyOne.executeForBoolean(context);
        }
        return false;
    }

    public void addExpression(Expression expression) {
        if (expression != null) {
            if (exprList == null) {
                exprList = new ArrayList<Expression>();
            }
            exprList.add(expression);
        }
    }

    @Override
    public void executeAndWrite(Context context) {
        Object val;
        if (onlyOne != null) {
            val = onlyOne.execute(context);
            context.write(val == null ? "" : val.toString());
        }
        if (exprList != null) {
            for (int i = 0, len = exprList.size(); i < len; i++) {
                val = exprList.get(i).execute(context);
                context.write(val == null ? "" : val.toString());
            }
        }
    }

    @Override
    public String executeForString(Context context) {
        Object val = execute(context);
        return val == null ? null : val.toString();
    }
}
