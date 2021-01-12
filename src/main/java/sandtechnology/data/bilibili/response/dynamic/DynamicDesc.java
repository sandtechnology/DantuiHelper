package sandtechnology.data.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
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
    @SerializedName("pre_dy_id_str")
    private
    String preDynamicID;
    @SerializedName("orig_dy_id_str")
    private
    String originDynamicID;
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

    @NotNull
    public String getDynamicID() {
        if (dynamicID != null) {
            return dynamicID;
        }
        if (preDynamicID != null) {
            return preDynamicID;
        }
        if (originDynamicID != null) {
            return originDynamicID;
        }
        //wtf
        return "0";
    }

    public int getType() {
        return type;
    }
}
