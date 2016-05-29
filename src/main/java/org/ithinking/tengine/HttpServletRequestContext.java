package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.TemplateEngine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;

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
        if(charset != null && !charset.trim().isEmpty()){
            this.charset = Charset.forName(charset);
        }
    }


    @Override
    public Object get(Object key) {
        Object value = null;
        String strKey = key.toString();
        char first = strKey.charAt(0);
        char second = strKey.length() > 1 ? strKey.charAt(1) : ' ';
        if (first == '$') { // 获取请求参数
            if (second == '$') {
                value = request.getParameterValues(strKey.substring(2));
            } else {
                value = request.getParameter(strKey.substring(1));
            }
        } else if (first == '_') { // 获取Cookie
            Cookie[] cookies = request.getCookies();
            if (second == '_') {
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
        if(s != null) {
            write(s.getBytes(charset));
        }
        return this;
    }

    @Override
    public Context write(String s, int start, int len) {
        if(s != null) {
            s = s.substring(start, start + len);
            write(s);
        }
        return this;
    }

    @Override
    public Context write(char[] values, int start, int len) {
        if(values != null && values.length != 0) {
            String ss = new String(values, start, len);
            write(ss.getBytes(charset));
        }
        return this;
    }

    @Override
    public Context write(byte[] bytes){
        if(bytes != null && bytes.length > 0) {
            write(bytes, 0, bytes.length);
        }
        return this;
    }

    @Override
    public Context write(byte[] bytes, int start, int len){
        try{
            if(bytes != null && bytes.length > 0) {
                response.getOutputStream().write(bytes, start, len);
            }
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
