package org.ithinking.tengine.core;

import org.ithinking.tengine.html.Document;

import java.io.File;

public class Template {
    private Document doc;
    private Resource resource;
    private Object bindingView;

    public String getText() {
        return resource.getText();
    }

    public String getPath() {
        return resource.getPath();
    }

    public String getId() {
        return resource.getId();
    }

    public long getLastModified() {
        return resource.getLastModified();
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

    public boolean isModified() {
        if (getLastModified() <= 0) {
            return true;
        } else if (resource.isRemote()) {
            return true;
        } else {
            File file = new File(this.getPath());
            if (!file.exists()) {
                throw new RuntimeException("FileNotFoundException");
            }
            return file.lastModified() != this.getLastModified();
        }
    }
}
