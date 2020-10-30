package sandtechnology.data.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.user.UserProfile;

public class DynamicDesc {


    private int type;

    private long timestamp;

    @SerializedName("dynamic_id_str")
    private
    String dynamicID;

    @SerializedName("user_profile")
    private
    UserProfile userProfile;

    @SerializedName("bvid")
    String bvid;
    @SerializedName("origin")
    DynamicDesc originDynamicDesc;

    public String getBvid() {
        return bvid;
    }

    public UserProfile getUserProfile() {
        if (userProfile == null) {
            throw new RuntimeException("用户信息不存在");
        }
        return userProfile;
    }

    public DynamicDesc setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getOriginDynamicID() {
        return originDynamicDesc.dynamicID;
    }

    public DynamicDesc getOriginDynamicDesc() {
        return originDynamicDesc;
    }

    public boolean isRepost() {
        return originDynamicDesc != null;
    }

    public String getDynamicID() {
        return dynamicID;
    }

    public int getType() {
        return type;
    }
}
