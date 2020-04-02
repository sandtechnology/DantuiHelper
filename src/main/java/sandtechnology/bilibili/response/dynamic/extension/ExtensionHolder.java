package sandtechnology.bilibili.response.dynamic.extension;

import sandtechnology.utils.JsonHelper;

public class ExtensionHolder {

    private String vote;


    public String getVoteInfo() {
        return vote == null ? "" : JsonHelper.getGsonInstance().fromJson(vote, Vote.class).toString();
    }
}
