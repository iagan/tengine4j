package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.TemplateManager;

import javax.servlet.ServletContext;
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

    public HttpServletRequestContext(TemplateManager manager, HttpServletRequest request, HttpServletResponse response, String charset) {
        super(manager);
        this.request = request;
        this.response = response;
        if(charset != null && !charset.trim().isEmpty()){
            this.charset = Charset.forName(charset);
        }
    }


    @Override
    public Object get(Object key) {
        Object value = null;
        String strKey = key.toString();
        char first = strKey.charAt(0);
        char last = strKey.charAt(strKey.length() - 1);
        if (first == '&') { // 获取请求参数
            if (last == ']') {
                int pos = strKey.indexOf("[");
                strKey = pos != -1 ? strKey.substring(1, pos) : strKey.substring(1, strKey.length() - 1);
                value = request.getParameterValues(strKey);
            } else {
                value = request.getParameter(strKey.substring(1));
            }
        } else if (first == '#') { // 获取Cookie
            Cookie[] cookies = request.getCookies();
            if (last == ']') {
                value = cookies;
            } else {
                strKey = strKey.substring(1);
                for(Cookie c : cookies){
                    if(strKey.equals(c.getName())){
                        value = c;
                        break;
                    }
                }
            }
        } else {
            value = super.get(strKey);
            if (value == null) {
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

    @Override
    public Context write(String s) {
        write(s.getBytes(charset));
        return this;
    }

    @Override
    public Context write(String s, int start, int len) {
        s = s.substring(start, start + len);
        write(s);
        return this;
    }

    @Override
    public Context write(char[] values, int start, int len) {
        String ss = new String(values, start, len);
        write(ss.getBytes(charset));
        return this;
    }

    @Override
    public Context write(byte[] bytes){
        write(bytes, 0, bytes.length);
        return this;
    }

    @Override
    public Context write(byte[] bytes, int start, int len){
        try{
            response.getOutputStream().write(bytes, start, len);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Context writeHeader(String name, String value){
        response.addHeader(name, value);
        return this;
    }
}
