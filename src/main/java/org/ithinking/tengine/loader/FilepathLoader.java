package org.ithinking.tengine.loader;

import java.io.File;

import org.ithinking.tengine.core.Resource;

public class FilepathLoader extends AbstractLoader{

	@Override
	public Resource load(String templatePath) {
		File file = new File(templatePath);
		Resource res = null;
		if(file != null){
			String text = load(file);
			res = new Resource();
			res.setId(templatePath);
			res.setText(text);
			res.setPath(file.getAbsolutePath());
			res.setLastModified(file.lastModified());
		}
		return res;
	}
	
	@Override
	public boolean isModified(String templateId, long lastModified) {
		// TODO Auto-generated method stub
		return false;
	}
}
