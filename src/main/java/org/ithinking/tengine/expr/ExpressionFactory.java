package org.ithinking.tengine.expr;

import org.ithinking.tengine.ExprParser;
import org.ithinking.tengine.ExprParser.Handler;

public class ExpressionFactory {

	public static Expression createExpression(String expression) {
		if(expression == null || expression.trim().isEmpty()){
			return null;
		}
		return new AviatorExpression(expression);
	}
	
	public static Expression createStringExpression(String expression) {
		if(expression == null || expression.trim().isEmpty()){
			return null;
		}
		return new StringExpression(expression);
	}

	public static Expression createExpression(String expression, boolean composite) {
		if(expression == null || expression.trim().isEmpty()){
			return null;
		}
		if (composite) {
			return createCompositeExpression(expression);
		} else {
			return new AviatorExpression(expression);
		}
	}

	public static Expression createCompositeExpression(String expression) {
		final CompositeExpression compositeExpression = new CompositeExpression();
		ExprParser.parseText(expression, new Handler() {
			@Override
			public void plainText(String text, int start, int count) {
				String expr = text.substring(start, start + count);
				compositeExpression.addExpression(new StringExpression(expr));
			}

			@Override
			public void expression(String text, int start, int count) {
				String expr = text.substring(start, start + count);
				compositeExpression.addExpression(createExpression(expr));
			}
		});
		return compositeExpression;
	}
}
