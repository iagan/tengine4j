package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.TemplateEngine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author agan
 */
public class HttpServletRequestContext extends DefContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Charset charset = Charset.forName("UTF-8");

    public HttpServletRequestContext(TemplateEngine engine, HttpServletRequest request, HttpServletResponse response, String charset) {
        super(engine);
        this.request = request;
        this.response = response;
        if (charset != null && !charset.trim().isEmpty()) {
            this.charset = Charset.forName(charset);
        }
    }


    /**
     * Parameter : $P_name, $PARAMS
     * Cookie: $C_name, $COOKIES
     * Header : $H_, $HEADERS
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        String strKey = key.toString();
        Object value = super.get(strKey);
        if (value == null) {
            char first = strKey.charAt(0);
            if (first == '$') { // 获取请求参数
                if (strKey.indexOf("_") == 2) {
                    char second = strKey.charAt(1);
                    if (second == 'P') {
                        value = request.getParameter(strKey.substring(3));
                    } else if (second == 'A') {
                        value = request.getParameterValues(strKey.substring(3));
                    } else if (second == 'C') {
                        value = findCookie(request.getCookies(), strKey.substring(3));
                    } else if (second == 'H') {
                        value = request.getHeader(strKey.substring(3));
                    }
                } else if ("$PARAMS".equals(strKey)) {
                    value = getSingleValueParameterMap();
                } else if ("PARAMETERS".equals(strKey)) {
                    value = request.getParameterMap();
                } else if ("$COOKIES".equals(strKey)) {
                    value = request.getCookies();
                } else if ("$HEADERS".equals(strKey)) {
                    value = getHeaderMap();
                }
            } else {
                value = request.getAttribute(strKey);
                if (value == null) {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        value = session.getAttribute(strKey);
                    }
                    if (value == null) {
                        value = request.getServletContext().getAttribute(strKey);
                    }
                }
            }
        }
        return value;
    }

    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    private Map<String, Object> getHeaderMap() {
        Enumeration<String> names = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        if (names != null) {
            String name;
            while (names.hasMoreElements()) {
                name = names.nextElement();
                headers.put(name, request.getHeader(name));
            }
        }
        return headers;
    }

    private Map<String, String> getSingleValueParameterMap() {
        Enumeration<String> names = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                params.put(name, request.getParameter(name));
            }
        }
        return params;
    }

    @Override
    public Context write(String s) {
        if (s != null) {
            write(s.getBytes(charset));
        }
        return this;
    }

    @Override
    public Context write(String s, int start, int len) {
        if (s != null) {
            s = s.substring(start, start + len);
            write(s);
        }
        return this;
    }

    @Override
    public Context write(char[] values, int start, int len) {
        if (values != null && values.length != 0) {
            String ss = new String(values, start, len);
            write(ss.getBytes(charset));
        }
        return this;
    }

    @Override
    public Context write(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            write(bytes, 0, bytes.length);
        }
        return this;
    }

    @Override
    public Context write(byte[] bytes, int start, int len) {
        try {
            if (bytes != null && bytes.length > 0) {
                response.getOutputStream().write(bytes, start, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Context writeHeader(String name, String value) {
        response.addHeader(name, value);
        return this;
    }
}
