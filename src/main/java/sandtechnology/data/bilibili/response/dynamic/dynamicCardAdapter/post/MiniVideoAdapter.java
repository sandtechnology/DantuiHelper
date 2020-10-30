package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.data.bilibili.response.user.UserProfile;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class MiniVideoAdapter implements IAdapter {

    Item item;
    AuthorProfile user;

    @Override
    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out.add(item.description).newLine().add(ImageManager.getImageData(item.cover.originImgURL));
    }

    @Override
    public String getActionText() {
        return "发了一个小视频";
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
