package org.ithinking.tengine.core;

public interface Loader {
	public boolean isModified(String templateId, long lastModified);
	public Resource load(String path);
}
