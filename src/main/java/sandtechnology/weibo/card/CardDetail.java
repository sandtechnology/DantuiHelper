package sandtechnology.weibo.card;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.HTMLUtils;
import sandtechnology.utils.ImageManager;
import sandtechnology.weibo.image.WeiboImage;
import sandtechnology.weibo.user.UserInfo;

import java.util.List;

public class CardDetail {
    @SerializedName("id")
    String idStr;
    @SerializedName("text")
    String htmlText;
    @SerializedName("raw_text")
    String RawText;
    @SerializedName("scheme")
    String url;
    @SerializedName("source")
    String sourceDevice;
    @SerializedName("created_at")
    String time;
    long CachedID;
    @SerializedName("user")
    UserInfo userInfo;
    @SerializedName("pics")
    List<WeiboImage> images;
    @SerializedName("retweeted_status")
    CardDetail repostCardDetails;
    @SerializedName("bid")
    String bid;

    public long getID() {
        return CachedID == 0 ? CachedID = Long.parseLong(idStr) : CachedID;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public String getRawText() {
        return RawText;
    }

    public String getUrl() {
        return "https://m.weibo.cn/status/" + bid;
    }

    public String getSourceDevice() {
        return sourceDevice;
    }

    public String getTime() {
        return time;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public List<WeiboImage> getImages() {
        return images;
    }

    private void parse(WriteOnlyMessage writeOnlyMessage) {
        HTMLUtils.parse(htmlText, writeOnlyMessage);
        if (images != null) {
            writeOnlyMessage.newLine();
            for (WeiboImage image : images) {
                writeOnlyMessage.add(ImageManager.getImageData(image.getOriginURL()));
            }
        }
    }

    public WriteOnlyMessage toWriteOnlyMessage() {
        WriteOnlyMessage writeOnlyMessage = new WriteOnlyMessage();
        writeOnlyMessage
                .add("微博链接：").add(getUrl()).newLine()
                .add(userInfo.getNickName()).add(repostCardDetails != null ? "转发了一条微博：" : "发了一条微博：").newLine();
        parse(writeOnlyMessage);
        if (repostCardDetails != null) {
            writeOnlyMessage
                    .add("原微博信息：").newLine()
                    .add(repostCardDetails.userInfo.getNickName()).add("发了一条微博：").newLine();
            repostCardDetails.parse(writeOnlyMessage);
        }
        return writeOnlyMessage;
    }

}
