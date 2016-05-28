package org.ithinking.tengine.loader;

import org.ithinking.tengine.XString;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Loader;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-05-29
 */
public class LoaderFactory {

    public static Loader createLoader(Configuration configuration) {
        String prefix = XString.defVal(configuration.getViewPrefix(), "").trim();
        String charset = configuration.getViewCharset();
        Loader loader;
        if (prefix.toLowerCase().indexOf("classpath:") == 0) {
            loader = new ClasspathLoader(prefix.substring("classpath:".length()), charset);
        } else {
            loader = new FilepathLoader(XString.makePath(configuration.getViewDocBase(), prefix), charset);
        }
        return loader;
    }
}
