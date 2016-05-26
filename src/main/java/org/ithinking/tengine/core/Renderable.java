package org.ithinking.tengine.core;

public interface Renderable {
	
	public Renderable getParent();
	
	public void setParent(Renderable parent);
	
	public void addChild(Renderable sublock);
	
	public void init(Configuration conf);

	public void render(Context context);
	
	public void renderBody(Context context);
}
