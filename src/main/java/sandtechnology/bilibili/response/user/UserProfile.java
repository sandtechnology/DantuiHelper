package sandtechnology.bilibili.response.user;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("info")
    Info info;


    static class Info {
        int uid;
        @SerializedName("uname")
        String userName;
        @SerializedName("face")
        String avatarImgURL;
    }
}
