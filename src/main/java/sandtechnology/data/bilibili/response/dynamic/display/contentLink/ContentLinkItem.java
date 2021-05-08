package sandtechnology.data.bilibili.response.dynamic.display.contentLink;

import com.google.gson.annotations.SerializedName;

public class ContentLinkItem {

    @SerializedName("jump_uri")
    private String jumpUri;

    @SerializedName("text")
    private String text;

    @SerializedName("icon_type")
    private int iconType;

    @SerializedName("orig_text")
    private String origText;

    public String getJumpUri() {
        return jumpUri;
    }

    public String getText() {
        return text;
    }

    public int getIconType() {
        return iconType;
    }

    public String getOrigText() {
        return origText;
    }
}