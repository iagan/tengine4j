package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.TemplateManager;

import javax.servlet.ServletContext;
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
        Object value = super.get(key);
        if(value == null){
            value = request.getAttribute(key.toString());
            if(value == null){
                HttpSession session = request.getSession(false);
                if(session != null){
                    value = session.getAttribute(key.toString());
                }
                if(value == null){
                    value = request.getServletContext().getAttribute(key.toString());
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
        System.out.println(name + ": " + value);
        return this;
    }
}
