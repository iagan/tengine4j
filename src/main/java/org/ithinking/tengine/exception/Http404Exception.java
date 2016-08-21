package org.ithinking.tengine.exception;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-08-21
 */
public class Http404Exception extends ParserException {

    private static final long serialVersionUID = 1L;

    public Http404Exception(){
        this("page not find");
    }

    public Http404Exception(String message, Throwable e){
        super(message, e);
    }

    public Http404Exception(String message){
        super(message);
    }

}
