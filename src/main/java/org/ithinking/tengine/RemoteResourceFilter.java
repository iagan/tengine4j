package org.ithinking.tengine;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.exception.Http404Exception;
import org.ithinking.tengine.loader.RemoteDynamicHostLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        conf.setViewPrefix(XString.defVal(conf.getViewPrefix(), "/tpl"));
        conf.setViewSuffix(XString.defVal(conf.getViewSuffix(), ".html"));
        conf.setWebContextPath(ctxPath);
        //
        docBasePath = conf.getDocBase();
        isDynamicRemoteHost = conf.isDynamicRemoteHost();
        prefix = conf.getViewPrefix();
        suffix = conf.getViewSuffix();
        //
        logger.info("[Init-2] prefix={},suffix={}, charset={}, docBasePath={}, isDynamicRemoteUrl={}",
                conf.getViewPrefix(), suffix, conf.getViewCharset(), docBasePath, isDynamicRemoteHost);

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
        } else if (conf.isFilePath()) {
            isLocal = true;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getServletPath();
        if (uri.endsWith(suffix) || uri.endsWith(".do") || uri.endsWith(".json") || uri.endsWith(".action") || uri.indexOf(".") == -1) {
            if (isDynamicRemoteHost) {
                RemoteDynamicHostLoader.setRemoteIp(WEB.getRemoteIP(req));
            }
            // 动态资源
            chain.doFilter(request, response);
        } else if (isRemote) {
            String remoteBase = docBasePath;
            if (isDynamicRemoteHost) {
                String ip = WEB.getRemoteIP(req);
                remoteBase = before + ip + after;
            }
            String resUrl = XString.makeUrl(remoteBase, uri);
            logger.info("[FILTER_REMOTE_URL]: Resource url={}", resUrl);
            try {
                Http.get(resUrl, response.getOutputStream());
            } catch (Http404Exception e) {
                // 对于未找到的资源，可以直接穿透使用服务器的资源
                logger.error("[FILTER_REMOTE_URL]: Not found resource= {}", resUrl);
                chain.doFilter(request, response);
            }
        } else if (isLocal) {
            // 绝对路径本地加载
            chain.doFilter(request, response);
            String path = XString.makePath(docBasePath, uri);
            readTo(path, (HttpServletResponse) response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void readTo(String path, HttpServletResponse response) {
        File file = new File(path);

        OutputStream out = null;
        FileInputStream fis = null;
        try {
            if (!file.exists() || !file.isFile()) {
                response.sendError(404, "资源未找到");
            } else {
                fis = new FileInputStream(file);
                out = response.getOutputStream();
                byte[] bytes = new byte[1024];
                int len;
                while ((len = fis.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                out.flush();
            }
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
