package codr7.jappkit.dom.html;

import codr7.jappkit.dom.Node;
import codr7.jappkit.dom.Tag;

import java.util.Map;
import java.util.stream.Stream;

public class Doc implements Tag {
    final HTMLTag head = new HTMLTag("head");
    final HTMLTag body = new HTMLTag("body");

    public Doc(String title) { head.appendTag("title").append(title); }

    @Override
    public String tag() { return "html"; }

    @Override
    public Stream<Map.Entry<String, Object>> attrs() { return Stream.empty(); }

    @Override
    public Stream<Node> childNodes() { return Stream.of(head, body); }
}
