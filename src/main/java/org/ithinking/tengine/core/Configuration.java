package org.ithinking.tengine.core;

import org.ithinking.tengine.XString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    public static final String PERM_KEY = "$_PERM_SET_$";
    // 默认配置
    private static final Configuration DEFAULT;
    private static Configuration WEB;
    // 文档基目录
    private String docBase;
    // 模板编码
    private String viewCharset;
    // tg.view.prefix
    private String viewPrefix;
    // tg.view.suffix= .html
    private String viewSuffix;

    private boolean isDefault = false;
    // 模板输出字符编码
    private String viewOutCharset;
    // tg.enable.perm
    private boolean enablePerm = true;

    // Web上下文路径
    private String webContextPath;
    // 图片基路径
    private String imageBase;

    static {
        DEFAULT = new Configuration();
        try {
            if (!loadDefConfig(Thread.currentThread().getContextClassLoader())) {
                if (!loadDefConfig(Configuration.class.getClassLoader())) {
                    ClassLoader classLoader = Configuration.class.getClassLoader();
                    Enumeration<URL> urls = classLoader.getResources("");
                    logger.info("\n\n[LOAD CONFIG] - {}\n\n", urls);
                    loadProperties(urls);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("\n\n[DEFAULT CONFIG] tg.img.base={}, tg.view.prefix={}, tg.view.suffix={},tg.doc.base={}\n\n",
                    DEFAULT.getImageBase(),
                    DEFAULT.getViewPrefix(),
                    DEFAULT.getViewSuffix(),
                    DEFAULT.getDocBase()
            );
            DEFAULT.isDefault = true;
        }
        WEB = newConfiguration();
        logger.info("\n\n[WEB CONFIG] tg.img.base={}, tg.view.prefix={}, tg.view.suffix={},tg.doc.base={}\n\n",
                WEB.getImageBase(),
                WEB.getViewPrefix(),
                WEB.getViewSuffix(),
                WEB.getDocBase()
        );
    }

    private Configuration() {
        this.isDefault = false;
    }

    /**
     * 加载  tengine.properties 文件
     *
     * @return
     */
    private static boolean loadDefConfig(ClassLoader classLoader) {
        // tengine.properties
        try {
            URL url = classLoader.getResource("/tengine.properties");
            if (url == null) {
                url = classLoader.getResource("tengine.properties");
            }
            if (url == null) {
                return false;
            }
            logger.info("\n\n[LOAD DEF CONFIG] - {}\n\n", url.getFile());
            loadProperties(new File(url.getFile()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("\n\n[LOAD_ERROR]\n\n");
            return false;
        }
    }

    private static void loadProperties(Enumeration<URL> urls) {
        if (urls != null) {
            String filePath;
            while (urls.hasMoreElements()) {
                filePath = urls.nextElement().getFile();
                File file = new File(filePath);
                logger.info("\n\n[LOAD FILE] - {} ", filePath);
                if (file.exists() && file.isDirectory()) {
                    File[] files = file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".properties");
                        }
                    });

                    for (int i = 0, len = files == null ? 0 : files.length; i < len; i++) {
                        logger.info("\n\n[FIND FILE] - {}\n\n", files[i].getAbsoluteFile());
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
            logger.info("\n\n[loadProperties] exists={}, file={}", file.exists(), file.getAbsoluteFile());
            inputStream = new FileInputStream(file);
            properties.load(inputStream);


            String imageBase = properties.getProperty("tg.img.base");
            String prefix = properties.getProperty("tg.view.prefix");
            String suffix = properties.getProperty("tg.view.suffix");
            String docBase = properties.getProperty("tg.doc.base");
            String charset = properties.getProperty("tg.view.charset");
            String enablePerm = properties.getProperty("tg.enable.perm");
            logger.info("\n\n[loadProperties] tg.img.base={}, tg.view.prefix={}, tg.view.suffix={}, tg.doc.base={}, tg.view.charset={}, tg.enable.perm={}\n\n",
                    imageBase, prefix, suffix, docBase, charset, enablePerm);
            DEFAULT.imageBase = imageBase;
            DEFAULT.viewPrefix = prefix;
            DEFAULT.viewSuffix = suffix;
            DEFAULT.docBase = docBase;
            DEFAULT.viewCharset = charset;
            DEFAULT.enablePerm = Boolean.valueOf(XString.defVal(enablePerm, "false"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
        }

        return properties;
    }

    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Configuration newConfiguration() {
        Configuration configuration = new Configuration();
        configuration.copyFromDefault();
        return configuration;

    }

    public static Configuration getWebConfiguration() {
        return WEB;
    }

    private void copyFromDefault() {
        this.imageBase = DEFAULT.imageBase;
        this.docBase = DEFAULT.docBase;
        this.viewCharset = DEFAULT.viewCharset;
        this.viewPrefix = DEFAULT.viewPrefix;
        this.viewSuffix = DEFAULT.viewSuffix;
        this.enablePerm = DEFAULT.enablePerm;
    }

    public static Configuration getDefault() {
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
                logger.info("[LOAD-CONFG] key:{}, value={}", key, value);
                if ("tg.img.base".equals(key)) {
                    configuration.setImageBase(value == null ? null : value.trim());
                } else if ("tg.doc.base".equals(key)) {
                    configuration.setDocBase(value == null ? null : value.trim());
                } else if ("tg.view.charset".equals(key)) {
                    configuration.setViewCharset(value == null ? null : value.trim());
                } else if ("tg.view.prefix".equals(key)) {
                    configuration.setViewPrefix(value == null ? null : value.trim());
                } else if ("tg.view.suffix".equals(key)) {
                    configuration.setViewSuffix(value == null ? null : value.trim());
                } else if ("tg.view.outCharset".equals(key)) {
                    configuration.setViewOutCharset(value == null ? null : value.trim());
                }  else if ("tg.enable.perm".equals(key)) {
                    configuration.setEnablePerm(Boolean.valueOf(XString.defVal(value, "false")));
                }
            }
        }
    }

    public boolean isEnablePerm() {
        return enablePerm;
    }

    public void setEnablePerm(boolean enablePerm) {
        if (!isDefault) {
            this.enablePerm = enablePerm;
        }
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getViewCharset() {
        return viewCharset;
    }

    public void setViewCharset(String viewCharset) {
        if (!isDefault) {
            this.viewCharset = viewCharset;
        }
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public void setViewPrefix(String viewPrefix) {
        if (!isDefault) {
            this.viewPrefix = viewPrefix;
        }
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        if (!isDefault) {
            this.viewSuffix = viewSuffix;
        }
    }

    public String getImageBase() {
        return imageBase;
    }

    public void setImageBase(String imageBase) {
        if (!isDefault) {
            this.imageBase = imageBase;
        }
    }

    public String getViewOutCharset() {
        return viewOutCharset;
    }

    public void setViewOutCharset(String viewOutCharset) {
        if (!isDefault) {
            this.viewOutCharset = viewOutCharset;
        }
    }

    /**
     * Web环境，自动设置
     *
     * @return
     */
    public String getWebContextPath() {
        return webContextPath;
    }

    public void setWebContextPath(String webContextPath) {
        this.webContextPath = webContextPath;
    }

    public boolean isClassPath() {
        return docBase != null && docBase.startsWith("classpath:");
    }

    public boolean isFilePath() {
        return docBase != null && docBase.startsWith("filepath:");
    }

    public boolean isRemoteUrl() {
        return docBase != null && (docBase.startsWith("http:") || docBase.startsWith("https:"));
    }

    public boolean isContextPath() {
        return !isRemoteUrl() && !isFilePath() && !isClassPath();
    }

    public boolean isDynamicRemoteHost() {
        return docBase != null && isRemoteUrl() && docBase.indexOf("{ip}") != -1;
    }

    public String getDocBase() {
        if (isClassPath()) {
            return docBase.substring("classpath:".length());
        } else if (isFilePath()) {
            return docBase.substring("filepath:".length());
        } else if (isRemoteUrl()) {
            return docBase;
        } else {
            // web 环境
            return webContextPath;
        }
    }

    public String getTplDocBase() {
        String docBase = getDocBase();
        if (XString.isBlank(docBase)) {
            return viewPrefix;
        }
        if (isRemoteUrl()) {
            return XString.makeUrl(docBase, viewPrefix, false);
        } else {
            return XString.makePath(docBase, viewPrefix);
        }
    }

    public String getViewOutCharsetOrDefault() {
        return XString.defVal(viewOutCharset, "UTF-8");
    }

    /**
     * just for testing.
     *
     * @param args
     */
    public static void main(String[] args) {
        Configuration conf = Configuration.newConfiguration();
        System.out.print(conf);
    }
}
