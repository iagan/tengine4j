package org.ithinking.tengine.loader;

import java.io.File;
import java.net.URL;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Resource;

public class ClasspathLoader extends AbstractLoader {
    // 文件编码
    private String encoding;
    // 路径前缀
    private String prefix;

    public ClasspathLoader(String prefix, String encoding) {
        this.prefix = XString.defVal(prefix, "");
        this.encoding = XString.defVal(encoding, "UTF-8");
    }

    public Resource load(String templateId) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("");
        Resource res = null;
        if (url.getProtocol().equals("file")) {
            File file = new File(url.getFile() + "/" + prefix + "/" + templateId);
            if (file.exists() && file.isFile()) {
                String text = this.load(file, encoding);
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
}
