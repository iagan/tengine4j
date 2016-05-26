package org.ithinking.tengine.html;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.DIRECTIVE;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

public class Attr extends AbstractRender {
	private String name;
	private String param;
	private String value;
	private String strChar = "";
	private DIRECTIVE type;
	private int valueType = 0;
	private Expression valueExpression;

	public Attr() {

	}

	public Attr(String name) {
		this.name = name;
	}

	@Override
	public void render(Context ctx) {
		ctx.write(" ").write(this.name).write("=");
		ctx.write(getStrChar());
		renderValue(ctx);
		ctx.write(getStrChar());
	}

	/**
	 * 渲染值部分
	 * 
	 * @param ctx
	 */
	public void renderValue(Context ctx) {
		if (valueExpression == null) {
			valueExpression = ExpressionFactory.createExpression(value, valueType != 0);
		}
		valueExpression.executeAndWrite(ctx);
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
