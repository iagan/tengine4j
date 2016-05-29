package org.ithinking.tengine.html;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ithinking.tengine.Indicator;
import org.ithinking.tengine.core.AbstractRender;
import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.DIRECTIVE;
import org.ithinking.tengine.core.TAG;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.expr.Expression;
import org.ithinking.tengine.expr.ExpressionFactory;

public class Tag extends AbstractRender {

    private String tagName;
    private List<Attr> attrList;
    private List<AttrGroup> attrGroups;
    private int features = 0;
    private boolean ignore = false;
    private boolean isHeader = false;
    private Expression ifExpression;
    private Expression repeatExpression;
    private Expression continueExpression;
    private Expression headerExpression;
    private Attr repeatAttr;
    private Document doc;
    private String fragid;
    private FragAttr fragAttr;

    public Tag(String name) {
        this(name, 0);
    }

    public Tag(String name, int features) {
        this.tagName = name;
        this.features = features;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getFeatures() {
        return features;
    }

    public void setFeatures(int features) {
        this.features |= features;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public String getFragid() {
        return fragid;
    }

    private boolean isSkip(Context context) {
        boolean skip = false;
        if (ignore) {
            skip = true;
        } else if (this.ifExpression != null) {
            boolean val = ifExpression.executeForBoolean(context);
            skip = !val;
        }
        return skip;
    }

    private boolean isContinue(Context context) {
        if (continueExpression != null) {
            return continueExpression.executeForBoolean(context);
        }
        return true;
    }

    private boolean isLoop(Context context) {
        return repeatExpression != null;
    }

    private Object getLoop(Context context) {
        if (repeatExpression != null) {
            return repeatExpression.execute(context);
        }
        return null;
    }

    private boolean isBean(Object obj) {
        return false;
    }

    /**
     * 渲染标签属性
     *
     * @param context
     */
    private void renderAttrs(Context context) {
        AttrGroup attrGroup;
        for (int i = 0, len = attrGroups == null ? 0 : attrGroups.size(); i < len; i++) {
            attrGroup = attrGroups.get(i);
            attrGroup.render(context);
        }
    }

    /**
     * 渲染起始标签
     *
     * @param context
     */
    private void renderStartTag(Context context) {
        context.write("<").write(tagName);
        renderAttrs(context);
        if ((features & TAG.F_SELF_CLOSE) == TAG.F_SELF_CLOSE) {
            context.write("/>");
        } else {
            context.write(">");
        }
    }

    private void renderTag(Context ctx) {
        if (fragAttr == null) {
            this.renderStartTag(ctx);
            this.renderBody(ctx);
            this.renderEndTag(ctx);
        } else {
            renderFragTag(ctx);
        }

    }

    private void renderFragTag(Context ctx) {
        String tplPath = fragAttr.getPath().executeForString(ctx);
        if (tplPath == null || tplPath.trim().isEmpty()) {
            throw new RuntimeException("template path is null.");
        }
        Template tpl = ctx.loadTemplate(tplPath);
        Document refDoc = tpl == null ? null : tpl.getDocument();
        if (refDoc == null) {
            throw new RuntimeException("template is not exists.");
        }

        String targetFragId = fragAttr.getFragid() == null ? null : fragAttr.getFragid().executeForString(ctx);
        boolean bodyOnly = fragAttr.isBodyOnly();
        if (fragAttr.isInclude()) {
            this.renderStartTag(ctx);
            this.renderFragTag(ctx, refDoc, bodyOnly, targetFragId);
            this.renderEndTag(ctx);
        } else if (fragAttr.isReplace()) {
            this.renderFragTag(ctx, refDoc, bodyOnly, targetFragId);
        } else if (fragAttr.isFirst()) {
            this.renderStartTag(ctx);
            this.renderFragTag(ctx, refDoc, bodyOnly, targetFragId);
            this.renderBody(ctx);
            this.renderEndTag(ctx);
        } else if (fragAttr.isLast()) {
            this.renderStartTag(ctx);
            this.renderBody(ctx);
            this.renderFragTag(ctx, refDoc, bodyOnly, targetFragId);
            this.renderEndTag(ctx);
        } else {
            throw new RuntimeException("未知渲染位置!");
        }
    }

    private void renderFragTag(Context ctx, Document refDoc, boolean bodyOnly, String fragId) {
        if (fragId == null) {
            refDoc.renderBody(ctx);
        } else {
            Tag fragment = refDoc.getFragment(fragId);
            if (fragment != null) {
                if (bodyOnly) {
                    fragment.renderBody(ctx);
                } else {
                    fragment.render(ctx);
                }
            }
        }
    }

    private void renderEndTag(Context context) {
        // 关闭标签
        if (((features & TAG.F_TAG_CLOSE) == TAG.F_TAG_CLOSE) || features == 0) {
            context.write("</").write(tagName).write(">");
        }
    }

    private void renderList(Context context, List<?> list) {
        try {
            int size = list == null ? 0 : list.size();
            if (size > 0) {
                Object obj;
                String key = repeatAttr.getParam();
                if (key == null || key.isEmpty()) {
                    key = "$item";
                }
                Indicator index = new Indicator();
                index.setSize(size);
                context.add("$stat", index);
                for (int i = 0, row = 1; i < size; i++) {
                    obj = list.get(i);
                    index.setIndex(i);
                    context.add(key, obj);
                    if (this.isContinue(context)) {
                        index.setRow(row++);
                        this.renderTag(context);
                    }
                }
            }
        } finally {
            if (list instanceof Closeable) {
                close(((Closeable) list));
            }
        }
    }

    private void renderIterator(Context context, Iterator<?> iterator) {
        try {
            Indicator index = new Indicator();
            int i = 0, row = 1;
            context.add("$stat", index);
            String key = repeatAttr.getParam();
            if (key == null || key.isEmpty()) {
                key = "$item";
            }
            Object item;
            while (iterator.hasNext()) {
                item = iterator.next();
                index.setIndex(i);
                context.add(key, item);
                if (this.isContinue(context)) {
                    index.setRow(row++);
                    this.renderTag(context);
                }
            }
        } finally {
            if (iterator instanceof Closeable) {
                close(((Closeable) iterator));
            }
        }
    }

    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderArray(Context context, Object[] array) {
        Object obj = null;
        String key = repeatAttr.getParam();
        if (key == null || key.isEmpty()) {
            key = "$item";
        }
        for (int i = 0, size = array.length; i < size; i++) {
            obj = array[i];
            context.add(key, obj);
            if (this.isContinue(context)) {
                this.renderTag(context);
            }
        }
    }

    private void renderMap(Context context, Map<?, ?> map) {
        Iterator<?> iter = map.keySet().iterator();
        Object key;
        while (iter.hasNext()) {
            key = iter.next();
            context.add("$key", key).add("$value", map.get(key));
            this.renderTag(context);
        }
    }

    private void renderSet(Context context, Set<?> set) {
        Iterator<?> iter = set.iterator();
        Object key;
        while (iter.hasNext()) {
            key = iter.next();
            context.add("$value", key);
            this.renderTag(context);
        }
    }

    private void renderObject(Context context, Object obj) {

    }

    @Override
    public void render(Context ctx) {
        if (!this.isHeader) {
            if (!this.isSkip(ctx)) {
                if (this.isLoop(ctx)) {
                    Object obj = this.getLoop(ctx);
                    if (obj != null) {
                        if (obj instanceof List) {
                            renderList(ctx, (List<?>) obj);
                        } else if (obj instanceof Map) {
                            renderMap(ctx, (Map<?, ?>) obj);
                        } else if (obj instanceof Set) {
                            renderSet(ctx, (Set<?>) obj);
                        } else if (obj.getClass().isArray()) {
                            renderArray(ctx, (Object[]) obj);
                        } else if (isBean(obj)) {
                            renderObject(ctx, obj);
                        } else if (obj instanceof Iterator) {
                            renderIterator(ctx, (Iterator<?>) obj);
                        }
                    }
                } else {
                    renderTag(ctx);
                }
            }
        }
    }

    /**
     * Header
     *
     * @param ctx
     */
    public void writeHeader(Context ctx) {
        if (this.isHeader) {
            if (!this.isSkip(ctx)) {
                if (headerExpression != null) {
                    Object result = headerExpression.execute(ctx);
                    if (result instanceof Map) {
                        Map<?, ?> headers = (Map<?, ?>) result;
                        Iterator<?> keys = headers.keySet().iterator();
                        Object key, val;
                        while (keys.hasNext()) {
                            key = keys.next();
                            val = headers.get(key);
                            ctx.writeHeader(key.toString(), val == null ? "" : val.toString());
                        }
                    }
                } else {
                    Attr name = this.findAttr("http-equiv");
                    Attr value = this.findAttr("content");
                    if (name != null && value != null) {
                        ctx.writeHeader(name.getValue(), value.getValue());
                    }
                }
            }
        }
    }

    public void addAttr(Attr attr) {
        if (attrList == null) {
            attrList = new ArrayList<Attr>();
        }
        if (attrGroups == null) {
            attrGroups = new ArrayList<AttrGroup>();
        }

        if (attr.getType() == DIRECTIVE.FRAG) {
            this.fragAttr = (FragAttr) attr;
            String param = this.fragAttr.getParam();
            param = param == null ? "" : param.toLowerCase();
            if (param.contains("body")) {
                fragAttr.setBodyOnly(true);
            }
            if (param.isEmpty() || param.contains("include")) {
                fragAttr.setPos(FragAttr.POS_INCLUDE);
            } else if (param.contains("replace")) {
                fragAttr.setPos(FragAttr.POS_REPLACE);
            } else if (param.contains("first")) {
                fragAttr.setPos(FragAttr.POS_FIRST);
            } else if (param.contains("last")) {
                fragAttr.setPos(FragAttr.POS_LAST);
            }

        } else if (attr.getType() == DIRECTIVE.FRAGID) {
            this.fragid = attr.getValue();
        } else if (attr.getType() == DIRECTIVE.HEADER) {
            isHeader = true;
            headerExpression = ExpressionFactory.createExpression(attr.getValue());
        } else if (attr.getType() == DIRECTIVE.IGNORE) {
            ignore = true;
        } else if (attr.getType() == DIRECTIVE.REPEAT) {
            this.repeatExpression = ExpressionFactory.createExpression(attr.getValue());
            this.repeatAttr = attr;
        } else if (attr.getType() == DIRECTIVE.CONTINUE) {
            this.continueExpression = ExpressionFactory.createExpression(attr.getValue());
        } else if (attr.getType() == DIRECTIVE.IF) {
            this.ifExpression = ExpressionFactory.createExpression(attr.getValue());
        } else {
            AttrGroup group = findAttrGroup(attr.getName());
            if (group == null) {
                group = new AttrGroup();
                group.setAttrName(attr.getName());
                attrGroups.add(group);
            }
            group.addAttr(attr);
        }
        this.attrList.add(attr);
    }

    private AttrGroup findAttrGroup(String name) {
        AttrGroup group = null;
        for (int i = 0, len = attrGroups == null ? 0 : attrGroups.size(); i < len; i++) {
            group = attrGroups.get(i);
            if (group.getAttrName().equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    private Attr findAttr(String name) {
        Attr attr = null;
        for (int i = 0, len = attrList == null ? 0 : attrList.size(); i < len; i++) {
            attr = attrList.get(i);
            if (attr.getName().equalsIgnoreCase(name)) {
                return attr;
            }
        }
        return null;
    }

    @Override
    protected void innerInit(Configuration conf) {
        if (attrList != null) {
            for (Attr attr : attrList) {
                attr.init(conf);
                if (attr.getType() == DIRECTIVE.FRAGID) {
                    doc.addFragment(this);
                }
            }
        }

        // 属性组初始化
        if (attrGroups != null) {
            for (AttrGroup group : attrGroups) {
                group.init(conf);
            }
        }

        if (ifExpression != null) {
            ifExpression.init(conf);
        }

        if (repeatExpression != null) {
            repeatExpression.init(conf);
        }

        if (continueExpression != null) {
            continueExpression.init(conf);
        }

        if (headerExpression != null) {
            headerExpression.init(conf);
        }

        if (repeatAttr != null) {
            repeatAttr.init(conf);
        }

        if (fragAttr != null) {
            fragAttr.init(conf);
        }

        if (isHeader) {
            doc.addHeader(this);
        }
    }
}
