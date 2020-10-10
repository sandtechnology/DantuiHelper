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
        long uid;
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

        public long getUid() {
            return uid;
        }
    }
}
