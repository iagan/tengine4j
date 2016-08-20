package org.ithinking.tengine.loader;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Loader;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-05-29
 */
public class LoaderFactory {

    public static Loader createLoader(Configuration conf) {
        String docBaseUrl = conf.getTplDocBase().trim();
        String charset = conf.getViewCharset();
        Loader loader;
        if (conf.isClassPath()) {
            loader = new ClasspathLoader(docBaseUrl, charset);
        } else if (conf.isDynamicRemoteHost()) {
            loader = new RemoteDynamicHostLoader(docBaseUrl, charset);
        } else if (conf.isRemoteUrl()) {
            loader = new RemoteLoader(docBaseUrl, charset);
        } else if (conf.isFilePath()) {
            loader = new FilepathLoader(docBaseUrl, charset);
        } else {
            loader = new FilepathLoader(docBaseUrl, charset);
        }
        return loader;
    }
}
