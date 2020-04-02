package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.user.UserProfile;
import sandtechnology.holder.WriteOnlyMessage;

public class PlainTextAdapter implements IAdapter {

    CommonItem item;
    @SerializedName("user")
    private UserProfile.Info profile;

    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("发了一条文字动态：\n").add(dynamicData.getDisplayContent().getEmojiInfo().format(new WriteOnlyMessage(item.content)));
    }

    private static class CommonItem {
        @SerializedName("content")
        private String content;
    }
}
