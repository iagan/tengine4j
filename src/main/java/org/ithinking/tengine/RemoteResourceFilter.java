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
    private boolean isProxy = false;
    private boolean isRemote = false;
    private boolean isLocal = false;
    private String prefix;
    private String suffix;
    private String localFilePrefix;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        prefix = XString.defVal(conf.getViewPrefix(), "tpl").trim();
        suffix = XString.defVal(conf.getViewSuffix(), ".html").trim();
        logger.info("Use remoteResourceFilter: prefix={}, suffix={}", prefix, suffix);
        if (prefix != null) {
            if (prefix.startsWith("http://") || prefix.startsWith("https://")) {
                isProxy = true;
            } else if (prefix.equals("://")) {
                isRemote = true;
            } else if (prefix.startsWith("filepath:")) {
                isLocal = true;
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getServletPath();
        if (uri.endsWith(suffix) || uri.endsWith(".do") || uri.endsWith(".json") || uri.endsWith(".action") || uri.indexOf(".") == -1) {
            // 动态资源
            chain.doFilter(request, response);
        } else if (isProxy) {
            // 远程加载静态资源
            String url = XString.makeUrl(prefix, uri);
            logger.info("[Proxy-load]: url={}", url);
            Http.get(url, response.getOutputStream());
        } else if (isRemote) {
            // 追踪远程加载
            String ip = WEB.getRemoteIP(req);
            String url = XString.makeUrl(ip, uri);
            logger.info("[Remote-load]: url={}", url);
            Http.get(url, response.getOutputStream());
        } else if (isLocal) {
            // 绝对路径本地加载
            chain.doFilter(request, response);
            String path = XString.makePath(localFilePrefix, uri);
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
