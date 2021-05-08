package sandtechnology.data.bilibili.response.dynamic.cardextension.related;

import com.google.gson.annotations.SerializedName;

public class Button {

    @SerializedName("jump_style")
    private JumpStyle jumpStyle;

    @SerializedName("jump_url")
    private String jumpUrl;

    @SerializedName("type")
    private int type;

    public JumpStyle getJumpStyle() {
        return jumpStyle;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public int getType() {
        return type;
    }

    public static class JumpStyle {

        @SerializedName("icon")
        private String icon;

        @SerializedName("text")
        private String text;

        public String getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }
    }
}