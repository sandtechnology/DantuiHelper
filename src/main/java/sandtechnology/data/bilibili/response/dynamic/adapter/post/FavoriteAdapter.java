package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class FavoriteAdapter implements IAdapter {

    @SerializedName("cover")
    private String coverURL;

    @SerializedName("fid")
    private int fid;

    @SerializedName("item")
    private Item item;

    @SerializedName("media_count")
    private int mediaCount;

    @SerializedName("intro")
    private String intro;

    @SerializedName("upper")
    private OwnerInfo ownerInfo;

    @SerializedName("mid")
    private int mid;

    @SerializedName("sharable")
    private boolean sharable;

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private int type;

    @SerializedName("cover_type")
    private int coverType;

    public String getCoverURL() {
        return coverURL;
    }

    public int getFid() {
        return fid;
    }

    public Item getItem() {
        return item;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public String getIntro() {
        return intro;
    }

    public OwnerInfo getOwnerInfo() {
        return ownerInfo;
    }

    public int getMid() {
        return mid;
    }

    public boolean isSharable() {
        return sharable;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public int getCoverType() {
        return coverType;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        return message.add(title).add("（共").add(mediaCount).add("个内容）")
                .newLine()
                .add(ImageManager.getImageData(coverURL));
    }

    @Override
    public String getActionText() {
        return "的收藏夹";
    }

    public static class OwnerInfo {

        @SerializedName("face")
        private String face;

        @SerializedName("name")
        private String name;

        @SerializedName("mid")
        private int mid;

        @SerializedName("followed")
        private int followed;

        public String getFace() {
            return face;
        }

        public String getName() {
            return name;
        }

        public int getMid() {
            return mid;
        }

        public int getFollowed() {
            return followed;
        }
    }

    public static class Item {

        @SerializedName("at_control")
        private String atControl;

        public String getAtControl() {
            return atControl;
        }
    }
}