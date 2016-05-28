package org.ithinking.tengine.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Configuration {
    // 模板编码
    private String viewCharset;
    // tg.view.prefix
    private String viewPrefix;
    // tg.view.suffix= .html
    private String viewSuffix;
    private boolean isDefault = false;

    private static final Map<String,String> CONFIGS;
    private static final Configuration DEFAULT;

    static {
        DEFAULT = new Configuration();
        CONFIGS = new HashMap<>();
        DEFAULT.isDefault = true;
        ClassLoader classLoader = Configuration.class.getClassLoader();
        try {
            Enumeration<URL> urls = classLoader.getResources("");
            String filePath;
            while (urls.hasMoreElements()) {
                filePath = urls.nextElement().getFile();
                File file = new File(filePath);
                if (file.exists() && file.isDirectory()) {
                    File[] files = file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".properties");
                        }
                    });

                    for (int i = 0, len = files == null ? 0 : files.length; i < len; i++) {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(file));
                        config(DEFAULT, properties);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Configuration(){

    }

    public static Configuration newConfiguration(){
        Configuration configuration = new Configuration();
        configuration.copyFromDefault();
        return configuration;

    }

    private void copyFromDefault(){
        this.setViewCharset(DEFAULT.getViewCharset());
        this.setViewPrefix(DEFAULT.getViewPrefix());
        this.setViewSuffix(DEFAULT.getViewSuffix());
    }

    public static Configuration getDefault(){
        return DEFAULT;
    }

    private static void config(Configuration configuration, Properties properties) {
        if (properties != null) {
            Iterator<Object> keys = properties.keySet().iterator();
            String key, value;
            while (keys.hasNext()) {
                key = keys.next().toString();
                value = properties.getProperty(key);
                key = key.toLowerCase().trim();
                if ("tg.view.charset".equals(key)) {
                    configuration.setViewCharset(value == null ? null : value.trim());
                } else if ("tg.view.prefix".equals(key)) {
                    configuration.setViewPrefix(value == null ? null : value.trim());
                } else if ("tg.view.suffix".equals(key)) {
                    configuration.setViewSuffix(value == null ? null : value.trim());
                }
            }
        }
    }

    /**
     * just for testing.
     *
     * @param args
     */
    public static void main(String[] args){
        Configuration conf = new Configuration();
        System.out.print(conf);
    }

    public String getViewCharset() {
        return viewCharset;
    }

    public void setViewCharset(String viewCharset) {
        if(!isDefault) {
            this.viewCharset = viewCharset;
        }
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public void setViewPrefix(String viewPrefix) {
        if(!isDefault) {
            this.viewPrefix = viewPrefix;
        }
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        if(!isDefault) {
            this.viewSuffix = viewSuffix;
        }
    }
}
