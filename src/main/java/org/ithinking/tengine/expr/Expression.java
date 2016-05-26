package org.ithinking.tengine.expr;

import org.ithinking.tengine.core.Context;

public interface Expression {

	public Object execute(Context context);
	
	public void executeAndWrite(Context context);
	
	public boolean executeForBoolean(Context context);
	
	public String executeForString(Context context);
}
