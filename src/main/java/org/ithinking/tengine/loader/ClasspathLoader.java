package org.ithinking.tengine.loader;

import java.io.File;
import java.net.URL;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Resource;

public class ClasspathLoader extends AbstractLoader {

    @SuppressWarnings("unused")
    private Configuration conf;

    public ClasspathLoader(Configuration conf) {
        this.conf = conf;
    }

    public Resource load(String templateId) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("");
        Resource res = null;
        if (url.getProtocol().equals("file")) {
            File file = new File(url.getFile() + "/" + templateId);
            if (file.exists()) {
                String text = this.load(file);
                if (text != null) {
                    res = new Resource();
                    res.setId(templateId);
                    res.setText(text);
                    res.setPath(file.getAbsolutePath());
                    res.setLastModified(file.lastModified());
                }
            }

        }
        return res;
    }

    @Override
    public boolean isModified(String templateId, long lastModified) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("");
        if (url.getProtocol().equals("file")) {
            File file = new File(url.getFile() + "/" + templateId);
            return file.lastModified() != lastModified;
        }
        return false;
    }

}
