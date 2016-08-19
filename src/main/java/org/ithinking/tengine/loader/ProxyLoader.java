package org.ithinking.tengine.loader;

import org.ithinking.tengine.Http;
import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Resource;

/**
 * 远程Web请求
 *
 * @author agan
 * @date 2016-08-19
 */
public class ProxyLoader extends AbstractLoader {
    private String remoteUrl;
    private String charset;

    public ProxyLoader(String remoteUrl, String charset) {
        this.remoteUrl = remoteUrl;
        this.charset = charset;
    }

    @Override
    public Resource load(String templateId) {
        String url = XString.makeUrl(remoteUrl, templateId);
        String context = Http.get(url);
        Resource resource = new Resource();
        resource.setId(templateId);
        resource.setLastModified(-1);
        resource.setPath(url);
        resource.setText(context);
        return resource;
    }
}
