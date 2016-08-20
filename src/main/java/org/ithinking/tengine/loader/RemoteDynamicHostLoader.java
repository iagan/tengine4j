package org.ithinking.tengine.loader;

import org.ithinking.tengine.XString;

/**
 * 远程加载动态主机资源
 *
 * @author agan
 * @date 2016-08-20
 */
public class RemoteDynamicHostLoader extends RemoteLoader {

    private String before;
    private String after;
    private static final ThreadLocal<String> REMOTE_IP = new ThreadLocal<>();

    public static void setRemoteIp(String remoteIp) {
        REMOTE_IP.set(remoteIp);
    }

    public RemoteDynamicHostLoader(String remoteUrl, String charset) {
        super(remoteUrl, charset);
        String[] splits = remoteUrl.split("\\{ip\\}");
        if (splits.length >= 1 && splits.length <= 2) {
            throw new IllegalArgumentException("Invalid remote url :" + remoteUrl);
        }
        before = splits[0].trim();
        after = splits.length == 1 ? "" : splits[1].trim();
    }

    protected String getRemoteUrl(String templateId) {
        String baseUrl = before + REMOTE_IP.get() + after;
        return XString.makeUrl(baseUrl, templateId);
    }

    public static void main(String[] args) {
        String[] splits = "http://{ip}/".split("\\{ip\\}");
        System.out.println(splits.length);
    }
}
