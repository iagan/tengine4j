package org.ithinking.tengine.html;

import java.util.ArrayList;
import java.util.List;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Param;
import org.ithinking.tengine.expr.Expression;

public class FragAttr extends Attr {

	private Expression path;

	private Document refDoc;
	private List<Param> params;
	private Expression fragid;
	private int pos = 0;
	private boolean bodyOnly = false;
	//
	public static final int POS_INCLUDE = 1;
	public static final int POS_REPLACE = 2;
	public static final int POS_FIRST = 4;
	public static final int POS_LAST = 8;

	public FragAttr(String name) {
		super(name);
	}

	public Document getRefDoc() {
		return this.refDoc;
	}

	public Expression getPath() {
		return path;
	}

	public void setPath(Expression path) {
		this.path = path;
	}

	public void addParam(Param param) {
		if (params == null) {
			params = new ArrayList<Param>();
		}
		params.add(param);
	}

	@Override
	protected void innerInit(Configuration conf) {
		if(this.fragid != null){
			this.fragid.init(conf);
		}
	}

	public Expression getFragid() {
		return fragid;
	}

	public void setFragid(Expression fragid) {
		this.fragid = fragid;
	}

	public void setPos(int pos) {
		this.pos |= pos;
	}

	public void setBodyOnly(boolean bodyOnly) {
		this.bodyOnly = bodyOnly;
	}

	public boolean isBodyOnly() {
		return bodyOnly;
	}

	public boolean isInclude() {
		return pos == 0 || ((pos & POS_INCLUDE) == POS_INCLUDE);
	}

	public boolean isReplace() {
		return (pos & POS_REPLACE) == POS_REPLACE;
	}

	public boolean isFirst() {
		return (pos & POS_FIRST) == POS_FIRST;
	}

	public boolean isLast() {
		return (pos & POS_LAST) == POS_LAST;
	}
}
