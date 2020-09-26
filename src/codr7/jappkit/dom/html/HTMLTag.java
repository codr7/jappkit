package codr7.jappkit.dom.html;

import codr7.jappkit.dom.CustomTag;

public class HTMLTag extends CustomTag {
    HTMLTag(String tag) { super(tag); }

    public HTMLTag appendTag(String tag) {
        HTMLTag it = new HTMLTag(tag);
        append(it);
        return it;
    }

    public HTMLTag a(String href) {
        HTMLTag it  = appendTag("a");
        it.set("href", href);
        return it;
    }

    public HTMLTag button(String href) { return appendTag("button"); }
}
