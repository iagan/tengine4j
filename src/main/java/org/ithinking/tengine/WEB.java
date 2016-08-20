package org.ithinking.tengine;

import javax.servlet.http.HttpServletRequest;

/**
 * Web 工具类
 *
 * @author agan
 * @date 2016-08-20
 */
public class WEB {

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.trim().isEmpty()) {
            ip = request.getHeader("X-real-ip");
        } else {
            int i = ip.indexOf(",");
            ip = i > 0 ? ip.substring(0, i) : ip;
        }

        if (ip == null || ip.trim().isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
