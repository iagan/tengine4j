package org.ithinking.tengine.expr;

import org.ithinking.tengine.core.Context;

import com.googlecode.aviator.AviatorEvaluator;

public class AviatorExpression extends ExpressionSupport {

	private String expression = null;
	private com.googlecode.aviator.Expression compileExpression;

	public AviatorExpression(String expression) {
		this.expression = expression.trim();
		this.compileExpression = AviatorEvaluator.compile(this.expression, true);
	}

	@Override
	public Object execute(Context context) {
		Object val = null;
		try {
			val = compileExpression.execute(context);
		}catch (NullPointerException e){

		}
		return val;
	}

	@Override
	public boolean executeForBoolean(Context context) {
		Object val = execute(context);
		if (val != null && !Boolean.FALSE.equals(val)) {
			return true;
		}
		return false;
	}

	@Override
	public void executeAndWrite(Context context) {
		Object val = execute(context);
		if (val != null) {
			context.write(val.toString());
		} else {
			context.write("null");
		}
	}

	@Override
	public String executeForString(Context context) {
		Object val = execute(context);
		return val == null ? null : val.toString();
	}

}
