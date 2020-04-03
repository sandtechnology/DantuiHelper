package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.user.UserProfile;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class MiniVideoAdapter implements IAdapter {

    Item item;
    AuthorProfile user;

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("发了一个小视频：").add(dynamicData.getDisplayContent().getEmojiInfo().format(item.description)).newLine().add(ImageManager.getImageData(item.cover.originImgURL));
    }

    private static class AuthorProfile extends UserProfile.Info {
        @SerializedName("name")
        String name;
        @SerializedName("head_url")
        String face;

        @Override
        public String getAvatarImgURL() {
            return face;
        }

        @Override
        public String getUserName() {
            return name;
        }
    }

    private static class Item {
        long id;
        String description;
        Cover cover;

        static class Cover {
            @SerializedName("default")
            String defaultImgURL;
            @SerializedName("unclipped")
            String originImgURL;
        }
    }
}
