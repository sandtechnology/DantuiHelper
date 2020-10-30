package sandtechnology.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
                writeOnlyMessage.add(((TextNode) node).text());
            } else {
                String src = node.attributes().get("src");
                if (!src.isEmpty()) {
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
