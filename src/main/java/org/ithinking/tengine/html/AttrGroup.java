package org.ithinking.tengine.html;

import java.util.ArrayList;
import java.util.List;

import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.DIRECTIVE;
import org.ithinking.tengine.expr.ExpressionFactory;

public class AttrGroup extends AbstractRender {

    private String attrName;
    private Attr xmlAttr;
    private List<Attr> tgAttrList;

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public void addAttr(Attr attr) {
        if (attr.getType() == DIRECTIVE.ANY_ATTR) {
            xmlAttr = attr;
        } else {
            if (tgAttrList == null) {
                tgAttrList = new ArrayList<Attr>();
            }
            tgAttrList.add(attr);
        }
    }

    @Override
    protected void innerInit(Configuration conf) {
        int len = tgAttrList == null ? 0 : tgAttrList.size();
        Attr attr;
        for (int i = 0; i < len; i++) {
            attr = tgAttrList.get(i);
            attr.init(conf);
        }
    }

    @Override
    public void render(Context ctx) {
        int len = tgAttrList == null ? 0 : tgAttrList.size();
        if (len > 0) {
            ctx.write(" ").write(this.attrName);
            ctx.write("=").write("\""); // getStrChar()
            Attr attr;
            for (int i = 0; i < len; i++) {
                attr = tgAttrList.get(i);
                if (i == len - 1) {
                    attr.renderValue(ctx);
                } else {
                    attr.renderValue(ctx);
                    ctx.write(" ");
                }
            }
            ctx.write("\"");
        } else if (xmlAttr != null) {
            xmlAttr.render(ctx);
        }
    }
}
