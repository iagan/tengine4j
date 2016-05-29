package org.ithinking.tengine.expr;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-05-29
 */
public abstract class ExpressionSupport implements Expression {

    protected Configuration conf;

    @Override
    public void init(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Object execute(Context context) {
        return null;
    }

    @Override
    public void executeAndWrite(Context context) {

    }

    @Override
    public boolean executeForBoolean(Context context) {
        return false;
    }

    @Override
    public String executeForString(Context context) {
        return null;
    }
}
