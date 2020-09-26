package codr7.jappkit.dom.tags;

import codr7.jappkit.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class CustomTag implements Node {
    public final String tag;

    public CustomTag(String tag) { this.tag = tag; }

    @Override
    public String tag() { return tag; }

    public Object get(String attr) { return attrs.get(attr); }

    public CustomTag set(String attr, Object val) {
        attrs.put(attr, val);
        return this;
    }

    @Override
    public Stream<Map.Entry<String, Object>> attrs() { return attrs.isEmpty() ? null : attrs.entrySet().stream(); }

    @Override
    public Stream<Node> childNodes() { return childNodes.isEmpty() ? null : childNodes.stream(); }

    private Map<String, Object> attrs = new TreeMap<>();
    private List<Node> childNodes = new ArrayList<>();
}
