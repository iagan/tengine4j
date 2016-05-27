package org.ithinking.tengine.core;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    // 模板编码
    private String charset = "UTF-8";
    // tg.view.prefix
    private String viewPrefix = "/WEB-INF/views/";
    // tg.view.suffix= .html
    private String viewSuffix = ".html";

    private static final Map<String,String> CONFIGS;

    static {
        CONFIGS = new HashMap<>();
    }
}
