package org.ithinking.tengine.core;

public interface Renderable {

    Renderable getParent();

    void setParent(Renderable parent);

    void addChild(Renderable sublock);

    void init(Configuration conf);

    void render(Context context);

    void renderBody(Context context);
}
