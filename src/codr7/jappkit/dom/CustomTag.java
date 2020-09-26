package codr7.jappkit.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class CustomTag implements Tag {
    public final String tag;

    public CustomTag(String tag) { this.tag = tag; }

    @Override
    public String tag() { return tag; }

    public CustomTag append(Node it) {
        if (childNodes == null) { childNodes = new ArrayList<>(); }
        childNodes.add(it);
        return this;
    }

    public CustomTag append(String it) { return append(new TextNode(it)); }

    public Object get(String it) { return attrs.get(it); }

    public CustomTag set(String it, Object val) {
        if (attrs == null) { attrs = new TreeMap<>(); }
        attrs.put(it, val);
        return this;
    }

    public CustomTag set(String attr) { return set(attr, null); }

    @Override
    public Stream<Map.Entry<String, Object>> attrs() { return (attrs == null || attrs.isEmpty()) ? null : attrs.entrySet().stream(); }

    @Override
    public Stream<Node> childNodes() { return (childNodes == null || childNodes.isEmpty()) ? null : childNodes.stream(); }

    private Map<String, Object> attrs = null;
    private List<Node> childNodes = null;
}
