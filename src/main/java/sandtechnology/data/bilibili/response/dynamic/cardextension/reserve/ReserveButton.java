package sandtechnology.data.bilibili.response.dynamic.cardextension.reserve;

import com.google.gson.annotations.SerializedName;

public class ReserveButton {

    @SerializedName("type")
    private int type;

    @SerializedName("uncheck")
    private ButtonInfo uncheckedButtonInfo;

    @SerializedName("check")
    private ButtonInfo checkedButtonInfo;

    @SerializedName("status")
    private int status;
    //正常进行时会出现的样式 START
    @SerializedName("jump_style")
    private JumpStyle jumpStyle;
    @SerializedName("jump_url")
    private String jumpURL;

    //正常进行时会出现的样式 End
    public int getType() {
        return type;
    }

    public String getStatusText() {
        ButtonInfo buttonInfo;
        if (uncheckedButtonInfo == null) {
            buttonInfo = checkedButtonInfo;
        } else {
            buttonInfo = uncheckedButtonInfo;
        }
        if (buttonInfo != null) {
            return buttonInfo.getText();
        } else {
            //正常进行时，会出现相关样式，否则预约已被删除
            return jumpStyle != null ? jumpStyle.text + "（" + jumpURL + "）" : "已删除";
        }
    }

    private static class JumpStyle {
        @SerializedName("text")
        String text;
    }
}