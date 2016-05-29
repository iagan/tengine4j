package org.ithinking.tengine.expr;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;

public interface Expression {

    /**
     * 初始化表达式(在未执行之前，只执行一次)
     * @param conf
     */
    void init(Configuration conf);

    /**
     * 执行表达式
     *
     * @param context
     * @return
     */
    Object execute(Context context);

    /**
     * 执行表达式并输出
     *
     * @param context
     */
    void executeAndWrite(Context context);

    /**
     * bool表达式
     *
     * @param context
     * @return
     */
    boolean executeForBoolean(Context context);

    /**
     * 执行表达式并以字符串返回
     *
     * @param context
     * @return
     */
    String executeForString(Context context);
}
