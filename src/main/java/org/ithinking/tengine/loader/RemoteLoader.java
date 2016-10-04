package org.ithinking.tengine.loader;

import org.ithinking.tengine.Http;
import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程Web资源加载
 *
 * @author agan
 * @date 2016-08-19
 */
public class RemoteLoader extends AbstractLoader {
    private static final Logger logger = LoggerFactory.getLogger(RemoteLoader.class);
    private String remoteUrl;
    private String charset;

    public RemoteLoader(String remoteUrl, String charset) {
        this.remoteUrl = remoteUrl;
        this.charset = charset;
    }

    protected String getRemoteUrl(String templateId) {
        return XString.makeUrl(remoteUrl, templateId);
    }

    @Override
    public Resource load(String templateId) {
        String url = getRemoteUrl(templateId);
        logger.info("[Load] templateId={} --> remoteUrl:{}", templateId, url);
        String context = Http.get(url, RemoteDynamicHostLoader.getHost());
        Resource resource = new Resource();
        resource.setId(templateId);
        resource.setLastModified(-1);
        resource.setRemote(true);
        resource.setPath(url);
        resource.setText(context);
        return resource;
    }
}
