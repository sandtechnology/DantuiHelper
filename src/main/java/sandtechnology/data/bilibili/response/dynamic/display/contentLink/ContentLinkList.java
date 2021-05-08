package sandtechnology.data.bilibili.response.dynamic.display.contentLink;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ContentLinkList {

    @SerializedName("rich_details")
    private List<ContentLinkItem> richDetails;

    public List<ContentLinkItem> getRichDetails() {
        return richDetails;
    }

    public Map<String, String> getReplaceStrMap() {
        if (richDetails == null || richDetails.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new LinkedHashMap<>();
        richDetails.forEach(contentLinkItem -> result.put(contentLinkItem.getOrigText(), contentLinkItem.getText() + "：" + contentLinkItem.getJumpUri()));
        return result;
    }
}