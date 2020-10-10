package sandtechnology.bilibili.response.dynamic.extension;

import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.JsonHelper;

public class ExtensionHolder {

    @SerializedName("vote")
    private String voteInfo;


    public VoteInfo getVoteInfo() {
        return voteInfo == null ? null : JsonHelper.getGsonInstance().fromJson(voteInfo, VoteInfo.class);
    }
}
