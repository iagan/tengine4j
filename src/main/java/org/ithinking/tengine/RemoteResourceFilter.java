package org.ithinking.tengine;

import org.ithinking.tengine.core.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * 远程静态资源拦截器
 *
 * @author agan
 * @date 2016-08-20
 */
public class RemoteResourceFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RemoteResourceFilter.class);
    private static Configuration conf = Configuration.newConfiguration();
    private boolean isRemote = false;
    private boolean isLocal = false;
    private String prefix;
    private String suffix;
    private String docBasePath;

    /**
     * 远程动态IP
     */
    private boolean isDynamicRemoteHost;
    private String before;
    private String after;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String ctxPath = filterConfig.getServletContext().getRealPath(filterConfig.getServletContext().getContextPath());
        conf = Configuration.getWebConfiguration();
        logger.info("[Init-1] prefix={},suffix={}, charset={}, ctxPath={}", conf.getViewPrefix(), conf.getViewSuffix(), conf.getViewCharset(), ctxPath);
        conf.setViewCharset(XString.defVal(conf.getViewCharset(), "UTF-8"));
        conf.setViewPrefix(XString.defVal(conf.getViewPrefix(), "/"));
        conf.setViewSuffix(XString.defVal(conf.getViewSuffix(), ".html"));
        conf.setWebContextPath(ctxPath);
        //
        docBasePath = conf.getDocBase();
        isDynamicRemoteHost = conf.isDynamicRemoteHost();
        logger.info("[Init-2] prefix={},suffix={}, charset={}, docBasePath={}, isDynamicRemoteUrl={}",
                conf.getViewPrefix(), suffix, conf.getViewCharset(), docBasePath, isDynamicRemoteHost);

        prefix = XString.defVal(conf.getViewPrefix(), "tpl").trim();
        suffix = XString.defVal(conf.getViewSuffix(), ".html").trim();

        if (conf.isRemoteUrl()) {
            isRemote = true;
            if (isDynamicRemoteHost) {
                String[] splits = docBasePath.split("\\{ip\\}");
                if (splits.length < 1 || splits.length > 2) {
                    throw new IllegalArgumentException("Invalid remote url :" + docBasePath);
                }
                before = splits[0].trim();
                after = splits.length == 1 ? "" : splits[1].trim();
            }
        } else if (prefix.startsWith("filepath:")) {
            isLocal = true;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getServletPath();
        if (uri.endsWith(suffix) || uri.endsWith(".do") || uri.endsWith(".json") || uri.endsWith(".action") || uri.indexOf(".") == -1) {
            // 动态资源
            chain.doFilter(request, response);
        } else if (isRemote) {
            String remoteBaseUrl;
            if (isDynamicRemoteHost) {
                // 追踪远程加载
                String ip = WEB.getRemoteIP(req);
                remoteBaseUrl = before + ip + after;
            } else {
                // 远程加载静态资源
                remoteBaseUrl = XString.makeUrl(docBasePath, uri);
            }
            logger.info("[REMOTE_URL]: url={}", remoteBaseUrl);
            Http.get(XString.makeUrl(remoteBaseUrl, uri), response.getOutputStream());
        } else if (isLocal) {
            // 绝对路径本地加载
            chain.doFilter(request, response);
            String path = XString.makePath(docBasePath, uri);
            readTo(path, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void readTo(String path, ServletResponse response) {
        File file = new File(path);
        OutputStream out = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            out = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis, out);
        }
    }

    private void close(InputStream is, OutputStream os) {
        try {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
