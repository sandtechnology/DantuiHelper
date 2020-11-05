package sandtechnology.data.weibo.user;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    private long id;
    @SerializedName("screen_name")
    private String nickName;
    @SerializedName("description")
    private String description;

    public long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }
}
