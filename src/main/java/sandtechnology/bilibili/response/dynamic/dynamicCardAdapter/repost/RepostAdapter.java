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

        WriteOnlyMessage originMessage;
        //判断是否已被删除
        if (item.isDeleted()) {
            originMessage = new WriteOnlyMessage("错误：").add(item.tips);
        } else {
            originMessage = AdapterSelector.getString(new DynamicData(
                    //补充原动态的用户信息
                    dynamicData.getDesc().getOriginDynamicDesc().setUserProfile(originUser)
                    , originDynamic
                    , originExtensionHolder
                    , originExtend
                    , dynamicData.getDisplayContent().getOriginDisplayHolder()
            ), false);
        }

        //判断特殊动态 进行优化处理
        //没有用户的动态都是分享的非动态内容，如电影
        if (originUser.getInfo().getUserName() == null || originUser.getInfo().getUserName().isEmpty()) {
            return out.add(originMessage);
        } else {
            return out.add("转发了一条动态：")
                    .newLine()
                    .add(dynamicData.getDisplayContent().getEmojiInfo().format(new WriteOnlyMessage(item.content)))
                    .add("\n原动态信息：\n")
                    .add(originMessage);
        }
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
