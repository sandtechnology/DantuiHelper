package sandtechnology.data.bilibili.response.dynamic.cardextension.reserve;

import com.google.gson.annotations.SerializedName;

public class DescTitle {

    @SerializedName("style")
    private int style;

    @SerializedName("text")
    private String text;

    public int getStyle() {
        return style;
    }

    public String getText() {
        return text;
    }
}