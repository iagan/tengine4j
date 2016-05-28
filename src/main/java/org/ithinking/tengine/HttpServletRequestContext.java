package org.ithinking.tengine;

import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.TemplateManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author agan
 */
public class HttpServletRequestContext extends DefContext {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public HttpServletRequestContext(TemplateManager manager, HttpServletRequest request, HttpServletResponse response) {
        super(manager);
        this.request = request;
        this.response = response;
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
    public Context write(String s, int start, int len) throws IOException{
        response.getWriter().write(s, start, len);
        return this;
    }

    @Override
    public Context write(char[] values, int start, int len) throws IOException{
        response.getWriter().write(values, start, len);
        return this;
    }

    @Override
    public Context write(byte[] bytes) throws IOException{
        write(bytes, 0, bytes.length);
        return this;
    }

    @Override
    public Context write(byte[] bytes, int start, int len) throws IOException{
        response.getOutputStream().write(bytes, start, len);
        return this;
    }

    @Override
    public Context writeHeader(String name, String value) throws IOException{
        System.out.println(name + ": " + value);
        return this;
    }
}
