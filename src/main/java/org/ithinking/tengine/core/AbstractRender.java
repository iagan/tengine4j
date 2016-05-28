package org.ithinking.tengine.core;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRender implements Renderable {

	protected Renderable parent;

	protected List<Renderable> sublocks;

	private boolean initialled = false;

	protected Configuration conf;

	@Override
	public Renderable getParent() {
		return parent;
	}

	@Override
	public void setParent(Renderable parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(Renderable sublock) {
		if (sublocks == null) {
			sublocks = new ArrayList<Renderable>();
		}
		sublock.setParent(this);
		sublocks.add(sublock);
	}

	@Override
	public void render(Context context) {

	}

	@Override
	public void init(Configuration conf) {
		if (!initialled) {
			this.conf = conf;
			innerInit(conf);
			if (sublocks != null) {
				for (Renderable sub : sublocks) {
					sub.init(conf);
				}
			}
		}
		initialled = true;
	}

	protected void innerInit(Configuration conf) {
		if(this.conf == null){
			this.conf = conf;
		}
		initialled = true;
	}

	@Override
	public void renderBody(Context context) {
		if (sublocks != null && !sublocks.isEmpty()) {
			for (Renderable sub : sublocks) {
				sub.render(context);
			}
		}
	}
}
