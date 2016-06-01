package org.ithinking.tengine.loader;

import java.io.File;
import java.io.FileNotFoundException;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Resource;

public class FilepathLoader extends AbstractLoader{

	private String docRoot;
	private String encoding;

	public FilepathLoader(String docRoot, String encoding){
		this.docRoot = docRoot;
		this.encoding = XString.defVal(encoding, "UTF-8");
	}

	@Override
	public Resource load(String templateId) {
		File file = new File(XString.makePath(docRoot, templateId));
		Resource res = null;
		if(file != null){
			String text = load(file, encoding);
            if(text != null) {
                res = new Resource();
                res.setPath(file.getAbsolutePath());
                res.setId(templateId);
                res.setText(text);
                res.setPath(file.getAbsolutePath());
                res.setLastModified(file.lastModified());
            }
		}
		return res;
	}

}
