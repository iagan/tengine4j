package org.ithinking.tengine.core;

import java.io.*;
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
    // 默认配置
    private static final Configuration DEFAULT;

    static {
        DEFAULT = new Configuration();
        try {
            ClassLoader classLoader = Configuration.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");
            loadProperties(urls);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DEFAULT.isDefault = true;
        }
    }

    private Configuration(){
        this.isDefault = false;
    }

    private static void loadProperties(Enumeration<URL> urls) {
        if(urls != null) {
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
                        config(DEFAULT, loadProperties(files[i]));
                    }
                }
            }
        }
    }

    private static Properties loadProperties(File file) {
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
        }

        return properties;
    }

    private static void close(InputStream is){
        if(is != null){
            try{
                is.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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

    /**
     * just for testing.
     *
     * @param args
     */
    public static void main(String[] args){
        Configuration conf = Configuration.newConfiguration();
        System.out.print(conf);
    }
}
