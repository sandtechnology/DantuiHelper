package sandtechnology.data.bilibili.response.dynamic.cardextension.reserve;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;

public class ReserveLottery implements Decodable {

    @SerializedName("share_icon")
    private String shareIcon;

    @SerializedName("jump_url")
    private String jumpUrl;

    @SerializedName("icon")
    private String icon;

    @SerializedName("text")
    private String text;

    @SerializedName("lottery_type")
    private int lotteryType;

    public String getShareIcon() {
        return shareIcon;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public int getLotteryType() {
        return lotteryType;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        if (lotteryType == 1) {
            return message.newLine().add(text).add("（ ").add(jumpUrl).add("）");
        } else {
            return message.newLine().add("未知预约抽奖类型，请打开动态查看");
        }
    }
}