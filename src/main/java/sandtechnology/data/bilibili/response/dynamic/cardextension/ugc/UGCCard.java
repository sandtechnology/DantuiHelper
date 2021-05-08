package sandtechnology.data.bilibili.response.dynamic.cardextension.ugc;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class UGCCard implements Decodable {

    @SerializedName("duration")
    private String duration;

    @SerializedName("oid_str")
    private String oidStr;

    @SerializedName("play_url")
    private String playUrl;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("head_text")
    private String headText;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("multi_line")
    private boolean multiLine;

    @SerializedName("desc_second")
    private String descSecond;

    public String getDuration() {
        return duration;
    }

    public String getOidStr() {
        return oidStr;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHeadText() {
        return headText;
    }


    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public String getDescSecond() {
        return descSecond;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        return message.add("内容分享：").newLine().add(title).newLine()
                .add(playUrl).newLine()
                .add(ImageManager.getImageData(imageUrl));
    }
}