package sandtechnology.bilibili.response.live;

import com.google.gson.annotations.SerializedName;

public class AnchorInfo {
    @SerializedName("base_info")
    private UserBaseInfo baseInfo;

    public UserBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public static class UserBaseInfo {
        @SerializedName("uname")
        String username;
        @SerializedName("face")
        String faceImgURL;
        String gender;

        public String getUsername() {
            return username;
        }
    }
}
