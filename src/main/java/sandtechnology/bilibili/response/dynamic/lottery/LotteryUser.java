package sandtechnology.bilibili.response.dynamic.lottery;

import com.google.gson.annotations.SerializedName;

public class LotteryUser {
    long uid;
    @SerializedName("name")
    String userName;
    @SerializedName("face")
    String avatarImgURL;

    public String getAvatarImgURL() {
        return avatarImgURL;
    }

    public String getUserName() {
        return userName;
    }

    public long getUid() {
        return uid;
    }
}
