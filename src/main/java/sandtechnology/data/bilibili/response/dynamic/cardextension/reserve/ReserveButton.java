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
        return buttonInfo == null ? "已删除" : buttonInfo.getText();
    }

    public boolean isValid() {
        return status == 1;
    }
}