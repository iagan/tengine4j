package org.ithinking.tengine.loader;

import java.io.File;

import org.ithinking.tengine.core.Resource;

public class WebinfoLoader extends AbstractLoader {
	private String appRootPath;

	public WebinfoLoader(String appRootPath) {
		this.appRootPath = appRootPath;
	}

	@Override
	public Resource load(String templateId) {
		File file = new File(appRootPath + templateId);
		Resource res = null;
		if(file != null){
			String text = load(file);
			res = new Resource();
			res.setId(templateId);
			res.setText(text);
			res.setPath(file.getAbsolutePath());
			res.setLastModified(file.lastModified());
		}
		return res;
	}
	
	@Override
	public boolean isModified(String templateId, long lastModified) {
		File file = new File(appRootPath + templateId);
		return file.lastModified() != lastModified;
	}

}
