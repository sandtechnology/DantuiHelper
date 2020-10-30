package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.repost;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.DynamicData;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.AdapterSelector;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.IRepostAdapter;
import sandtechnology.data.bilibili.response.dynamic.extension.ExtensionHolder;
import sandtechnology.data.bilibili.response.user.UserProfile;
import sandtechnology.holder.WriteOnlyMessage;

public class RepostAdapter implements IRepostAdapter {

    @SerializedName("origin")
    String originDynamic;
    @SerializedName("origin_extend_json")
    String originExtend;
    @SerializedName("origin_user")
    UserProfile originUser;
    @SerializedName("item")
    CommonItem repostDynamic;
    @SerializedName("origin_extension")
    private final ExtensionHolder originExtensionHolder = new ExtensionHolder();
    @SerializedName("user")
    private UserProfile.Info profile;

    @Override
    public WriteOnlyMessage getContent(WriteOnlyMessage out, DynamicData dynamicData) {

        //解析原动态部分
        WriteOnlyMessage originMessage;
        //判断是否已被删除
        if (repostDynamic.isOriginDeleted()) {
            originMessage = new WriteOnlyMessage(" ❗ ").add(repostDynamic.tips);
        } else {
            //原动态解析
            DynamicData originDynamicData = new DynamicData(
                    //补充原动态的用户信息
                    dynamicData.getDesc().getOriginDynamicDesc().setUserProfile(originUser)
                    , originDynamic
                    , originExtensionHolder
                    , originExtend
                    , dynamicData.getDisplayContent().getOriginDisplayHolder()
            );
            if (isShareDynamic()) {
                return new WriteOnlyMessage(dynamicData.getDesc().getUserProfile().getInfo().getUserName()).add(AdapterSelector.getString(originDynamicData, false, repostDynamic.content));
            } else {
                originMessage = AdapterSelector.getString(originDynamicData, false);
            }
        }

        return out.add(repostDynamic.content)
                .add("\n原动态信息：\n")
                .add(originMessage);
    }

    @Override
    public String getActionText() {
        return "转发了一条动态";
    }

    @Override
    public boolean isShareDynamic() {
        return !repostDynamic.isOriginDeleted() && (originUser.getInfo().getUserName() == null || originUser.getInfo().getUserName().isEmpty());
    }

    private static class CommonItem {
        @SerializedName("content")
        private String content;
        @SerializedName("miss")
        private int deleted;
        @SerializedName("tips")
        private String tips;

        public boolean isOriginDeleted() {
            return deleted == 1;
        }
    }
}
