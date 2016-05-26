package org.ithinking.tengine.expr;

import java.util.ArrayList;
import java.util.List;

import org.ithinking.tengine.core.Context;

public class CompositeExpression implements Expression {

	private List<Expression> exprList;

	@Override
	public Object execute(Context context) {
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
		if (exprList != null) {
			Object val;
			for (int i = 0, len = exprList.size(); i < len; i++) {
				val = exprList.get(i).execute(context);
				if (val != null) {
					context.write(val.toString());
				} else {
					context.write("null");
				}
			}
		}
	}

	@Override
	public String executeForString(Context context) {
		return (String)execute(context);
	}
}
