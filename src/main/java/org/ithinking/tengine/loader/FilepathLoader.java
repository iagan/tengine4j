package org.ithinking.tengine.loader;

import java.io.File;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Resource;

public class FilepathLoader extends AbstractLoader{

	private String docRoot;

	public FilepathLoader(String docRoot){
		this.docRoot = docRoot;
	}

	@Override
	public Resource load(String templateId) {
		File file = new File(XString.makePath(docRoot, templateId));
		Resource res = null;
		if(file != null){
			String text = load(file);
			res = new Resource();
			res.setPath(file.getAbsolutePath());
			res.setId(templateId);
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
