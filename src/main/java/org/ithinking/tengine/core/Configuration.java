package org.ithinking.tengine.core;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    // 模板编码
    private String viewCharset;
    // tg.view.prefix
    private String viewPrefix;
    // tg.view.suffix= .html
    private String viewSuffix;

    private static final Map<String,String> CONFIGS;

    static {
        CONFIGS = new HashMap<>();
    }

    public String getViewCharset() {
        return viewCharset;
    }

    public void setViewCharset(String viewCharset) {
        this.viewCharset = viewCharset;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public void setViewPrefix(String viewPrefix) {
        this.viewPrefix = viewPrefix;
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        this.viewSuffix = viewSuffix;
    }
}
