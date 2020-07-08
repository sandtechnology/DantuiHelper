package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.user.UserProfile;
import sandtechnology.holder.WriteOnlyMessage;

public class PlainTextAdapter implements IAdapter {

    CommonItem item;
    @SerializedName("user")
    private UserProfile.Info profile;

    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out.add(item.content);
    }

    @Override
    public String getActionText() {
        return "发了一条文字动态";
    }

    private static class CommonItem {
        @SerializedName("content")
        private String content;
    }
}
