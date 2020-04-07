package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.repost;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.AdapterSelector;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.dynamic.extension.ExtensionHolder;
import sandtechnology.bilibili.response.user.UserProfile;
import sandtechnology.holder.WriteOnlyMessage;

public class RepostAdapter implements IAdapter {

    @SerializedName("origin")
    String originDynamic;
    @SerializedName("origin_extend_json")
    String originExtend;
    @SerializedName("origin_user")
    UserProfile originUser;
    CommonItem item;
    @SerializedName("origin_extension")
    private final ExtensionHolder originExtensionHolder = new ExtensionHolder();
    @SerializedName("user")
    private UserProfile.Info profile;

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("转发了").add(item.isDeleted() ? "[源动态已被删除]" : originUser.getInfo().getUserName()).add("的动态：")
                .add(dynamicData.getDisplayContent().getEmojiInfo().format(new WriteOnlyMessage(item.content)))
                .add("\n原动态信息：\n")
                .add(item.isDeleted() ? new WriteOnlyMessage(item.tips).addFirst("错误：") : AdapterSelector.getString(
                        new DynamicData(
                                dynamicData.getDesc().getOriginDynamicDesc().setUserProfile(originUser)
                                , originDynamic
                                , originExtensionHolder
                                , originExtend
                                , dynamicData.getDisplayContent().getOriginDisplayHolder()
                        ), false));
    }

    private static class CommonItem {
        @SerializedName("content")
        private String content;
        @SerializedName("miss")
        private int deleted;
        @SerializedName("tips")
        private String tips;

        public boolean isDeleted() {
            return deleted == 1;
        }
    }
}