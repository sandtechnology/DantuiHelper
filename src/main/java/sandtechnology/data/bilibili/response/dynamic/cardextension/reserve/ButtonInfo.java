package sandtechnology.data.bilibili.response.dynamic.cardextension.reserve;

import com.google.gson.annotations.SerializedName;

public class ButtonInfo {

    @SerializedName("toast")
    private String toast;

    @SerializedName("disable")
    private int disable;

    @SerializedName("icon")
    private String icon;

    @SerializedName("text")
    private String text;

    public String getToast() {
        return toast;
    }

    public int getDisable() {
        return disable;
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}