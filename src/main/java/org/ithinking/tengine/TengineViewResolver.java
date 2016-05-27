package org.ithinking.tengine;

import org.springframework.web.servlet.View;

import java.util.Locale;

/**
 * @author fuchujian
 */
public class TengineViewResolver extends org.springframework.web.servlet.view.AbstractCachingViewResolver implements org.springframework.core.Ordered{

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
