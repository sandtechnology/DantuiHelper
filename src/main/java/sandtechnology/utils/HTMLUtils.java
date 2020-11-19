package sandtechnology.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.LinkedList;
import java.util.List;

public class HTMLUtils {

    private HTMLUtils() {
    }

    public static WriteOnlyMessage parse(String html) {
        return parse(html, new WriteOnlyMessage());
    }

    public static WriteOnlyMessage parse(String html, WriteOnlyMessage writeOnlyMessage) {
        Document document = Jsoup.parse(html);
        List<Node> nodeList = flatNodes(document.body().childNodes());
        for (Node node : nodeList) {
            if (node instanceof TextNode) {
                String text = ((TextNode) node).text();
                if (!text.isEmpty()) {
                    //过滤网页链接文本
                    if (!node.hasParent() && !(node.parent().parent() instanceof Element) || !(((Element) node.parent().parent()).tagName().equals("a"))) {
                        writeOnlyMessage.add(((TextNode) node).text());
                    }
                }

            } else {
                //处理换行
                if (node instanceof Element) {
                    if (((Element) node).tagName().equals("br")) {
                        writeOnlyMessage.newLine();
                    }
                }
                //解析链接
                writeOnlyMessage.add(node.attr("data-url"));

                //不解析链接图标
                if (!node.hasAttr("alt")) {
                    continue;
                }
                //解析可能的图片
                String src = node.attributes().get("src");
                if (!src.isEmpty()) {
                    if (src.startsWith("//")) {
                        src = "https:" + src;
                    }
                    writeOnlyMessage.add(ImageManager.getImageData(src));
                }
            }

        }
        return writeOnlyMessage;
    }

    private static List<Node> flatNodes(List<Node> nodes) {
        if (nodes.isEmpty()) {
            return nodes;
        }
        List<Node> result = new LinkedList<>();
        for (Node node : nodes) {
            result.add(node);
            result.addAll(flatNodes(node.childNodes()));
            //清空子节点确保不被重复解析
            node.empty();
        }
        return result;
    }
}
