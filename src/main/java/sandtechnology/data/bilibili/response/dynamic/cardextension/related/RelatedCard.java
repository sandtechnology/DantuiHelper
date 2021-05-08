package sandtechnology.data.bilibili.response.dynamic.cardextension.related;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class RelatedCard implements Decodable {

    @SerializedName("button")
    private Button button;

    @SerializedName("cover_url")
    private String coverUrl;

    @SerializedName("oid_str")
    private String oidStr;

    @SerializedName("jump_url")
    private String jumpUrl;

    @SerializedName("head_text")
    private String headText;

    @SerializedName("desc_first")
    private String descFirst;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("cover_type")
    private int coverType;

    @SerializedName("desc_second")
    private String descSecond;

    public Button getButton() {
        return button;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getOidStr() {
        return oidStr;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public String getHeadText() {
        return headText;
    }

    public String getDescFirst() {
        return descFirst;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getCoverType() {
        return coverType;
    }

    public String getDescSecond() {
        return descSecond;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        return message.add(headText).add("——").add(title).add("：").newLine()
                .add(jumpUrl).newLine()
                .add(ImageManager.getImageData(coverUrl));
    }
}