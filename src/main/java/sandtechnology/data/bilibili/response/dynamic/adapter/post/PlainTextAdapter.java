package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.data.bilibili.response.user.UserProfile;
import sandtechnology.holder.IWriteOnlyMessage;

public class PlainTextAdapter implements IAdapter {

    CommonItem item;
    @SerializedName("user")
    private UserProfile.Info profile;

    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
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
