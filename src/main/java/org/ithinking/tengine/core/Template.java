package org.ithinking.tengine.core;

import org.ithinking.tengine.html.Document;

public class Template {
	private Document doc;
	private TemplateManager manager;
	private Resource resource;
	private Object bindingView;
	
	public Template(TemplateManager manager){
		this.manager = manager;
	}
	
	public String getText(){
		return resource.getText();
	}
	
	public String getPath(){
		return resource.getPath();
	}
	
	public String getId(){
		return resource.getId();
	}
	
	public long getLastModified(){
		return resource.getLastModified();
	}
	
	public boolean isModified(){
		return manager.isModified(this);
	}
	
	public void reload(){
		manager.reload(this);
	}

	public void render(Context context) {
		if (doc != null) {
			doc.render(context);
		}
	}

	public Document getDocument() {
		return doc;
	}
	
	public Document setDocument(Document doc) {
		return this.doc = doc;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Object getBindingView() {
		return bindingView;
	}

	public void setBindingView(Object bindingView) {
		this.bindingView = bindingView;
	}
}
