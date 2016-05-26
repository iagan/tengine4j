package org.ithinking.tengine.html;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Param;
import org.ithinking.tengine.expr.Expression;

public class URLParam implements Param {

	private String name;
	private Expression value;

	public URLParam(String name, Expression value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getValue(Context ctx) {
		return value;
	}

}
