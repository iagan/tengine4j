package org.ithinking.tengine.html;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

public class Expr extends AbstractRender {

	private Expression expression;

	public Expr(String expression) {
		this.expression = ExpressionFactory.createExpression(expression);
	}

	@Override
	public String toString() {
		return "${" + expression + "}";
	}

	@Override
	public void render(Context context) {
		Object result = expression.execute(context);
        if(result instanceof Expression){
            result = ((Expression)result).execute(context);
        }
		context.write(result == null ? null : result.toString());
	}
}
