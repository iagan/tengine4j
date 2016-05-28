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
    public Object get(String key) {
        /**
        request.getServletContext().getAttribute("");
        request.getSession().getAttribute("");
        request.getParameter("");
        request.getAttribute("");
         **/
       Object value = super.get(key);
        if(value == null){
            value = request.getAttribute(key);
            if(value == null){
                HttpSession session = request.getSession(false);
                if(session != null){
                    value = session.getAttribute(key);
                }
                if(value == null){
                    value = request.getServletContext().getAttribute(key);
                }
            }
        }
        return value;
    }

    @Override
    public Map<String, Object> values() {
        HashMap<String, Object> result = new HashMap<>();
        String key;
        Enumeration<String> keys;
        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession(false);

        // 1.ServletContext
        keys = servletContext.getAttributeNames();
        if (keys != null) {
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                result.put(key, servletContext.getAttribute(key));
            }
        }

        // 2.session
        if(session != null) {
            keys = session.getAttributeNames();
            if (keys != null) {
                while (keys.hasMoreElements()) {
                    key = keys.nextElement();
                    result.put(key, session.getAttribute(key));
                }
            }
        }

        // 3.Request
        keys = request.getAttributeNames();
        if (keys != null) {
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                result.put(key, request.getAttribute(key));
            }
        }


        // 4.Custom
        Map<String, Object> values = super.values();
        result.putAll(values);


        return result;
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
