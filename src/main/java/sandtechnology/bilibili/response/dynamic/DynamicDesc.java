package sandtechnology.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.user.UserProfile;

public class DynamicDesc {


    private int type;

    private long timestamp;

    @SerializedName("dynamic_id_str")
    private
    String dynamicID;

    @SerializedName("user_profile")
    private
    UserProfile userProfile;

    @SerializedName("origin")
    private
    DynamicDesc originDynamicDesc;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public DynamicDesc getOriginDynamicDesc() {
        return originDynamicDesc;
    }

    public String getDynamicID() {
        return dynamicID;
    }

    public int getType() {
        return type;
    }
}
