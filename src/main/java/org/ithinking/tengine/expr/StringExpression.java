package org.ithinking.tengine.expr;

import org.ithinking.tengine.core.Context;

public class StringExpression extends ExpressionSupport {

	private String value;

	public StringExpression(String value) {
		this.value = value;
	}

	@Override
	public Object execute(Context context) {
		return value;
	}

	@Override
	public boolean executeForBoolean(Context context) {
		return value != null;
	}

	@Override
	public void executeAndWrite(Context context) {
		context.write(value);
	}

	@Override
	public String executeForString(Context context) {
		return value;
	}
}
