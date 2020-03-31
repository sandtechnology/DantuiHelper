package sandtechnology.bilibili.response.user;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("info")
    private
    Info info;

    public Info getInfo() {
        return info;
    }

    public static class Info {
        int uid;
        @SerializedName("uname")
        String userName;
        @SerializedName("face")
        String avatarImgURL;

        public String getAvatarImgURL() {
            return avatarImgURL;
        }

        public String getUserName() {
            return userName;
        }

        public int getUid() {
            return uid;
        }
    }
}
