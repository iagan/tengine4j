package org.ithinking.tengine.core;

import java.util.HashMap;
import java.util.Map;

import org.ithinking.tengine.html.Document;
import org.ithinking.tengine.html.parser.HtmlParser;

public class TemplateManager {
	
	private static Map<String, Template> TEMPLATES = new HashMap<String,Template>();
	
	private Loader loader;
	private HtmlParser parser;
	
	public TemplateManager(Loader loader, Configuration conf, HtmlParser parser){
		this.loader = loader;
		this.parser = parser;
	}

	public boolean isModified(Template template){
		return loader.isModified(template.getId(), template.getLastModified());
	}
	
	public void reload(Template template){
		Resource res = loader.load(template.getId());
		Document doc = parser.parse(res.getText());
		template.setResource(res);
		template.setDocument(doc);
	}
	
	public Template getTemplate(String id){
		Template tpl = TEMPLATES.get(id);
		if(tpl != null && !tpl.isModified()){
			return tpl;
		}else{
			Resource res = loader.load(id);
			if(res != null){
				Document doc = parser.parse(res.getText());
				
				if(doc != null){
					tpl = new Template(this);
					tpl.setResource(res);
					tpl.setDocument(doc);
					TEMPLATES.put(id, tpl);
				}
			}
		}
		return tpl;
	}
}
