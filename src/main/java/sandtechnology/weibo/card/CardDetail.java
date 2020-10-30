package sandtechnology.weibo.card;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.HTMLUtils;
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
    long id;
    @SerializedName("user")
    UserInfo userInfo;
    @SerializedName("pics")
    List<WeiboImage> images;

    public long getId() {
        return id == 0 ? id = Long.parseLong(idStr) : id;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public String getRawText() {
        return RawText;
    }

    public String getUrl() {
        return url;
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

    public WriteOnlyMessage toWriteOnlyMessage() {
        WriteOnlyMessage writeOnlyMessage = new WriteOnlyMessage();
        writeOnlyMessage
                .add("微博链接：").add(url).newLine()
                .add(userInfo.getNickName()).add("发了一条微博：").newLine();
        HTMLUtils.parse(htmlText, writeOnlyMessage);
        return writeOnlyMessage;
    }

}
