package sandtechnology.data.weibo.card;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import sandtechnology.data.weibo.Response;
import sandtechnology.data.weibo.image.WeiboImage;
import sandtechnology.data.weibo.user.UserInfo;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.HTMLUtils;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.http.DataGetter;
import sandtechnology.utils.http.WeiboHTTPHelper;

import java.util.List;

public class CardDetail {

    private static final DataGetter<Response, LongTextCard> longTextCardDataGetter = new DataGetter<>(new WeiboHTTPHelper("https://m.weibo.cn/statuses/extend", null), new TypeToken<LongTextCard>() {
    }, "id");
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
    @SerializedName("page_info")
    PageInfo pageInfo;
    @SerializedName("isLongText")
    boolean isLongText;
    @SerializedName("title")
    CardTitle title;


    public boolean isOnTop() {
        return title != null && title.text != null && title.text.equals("置顶");
    }

    public boolean isLongText() {
        return isLongText;
    }

    public long getID() {
        return CachedID == 0 ? CachedID = Long.parseLong(idStr) : CachedID;
    }

    public String getHtmlText() {
        if (isLongText()) {
            synchronized (longTextCardDataGetter) {
                longTextCardDataGetter.getHttpHelper().setReferer(getUrl());
                longTextCardDataGetter.query(Long.toString(getID()));
                if (longTextCardDataGetter.getData().isOk()) {
                    htmlText = longTextCardDataGetter.getData().getLongTextContent();
                } else {
                    htmlText = "长文本获取失败，原文本：" + htmlText;
                }
            }
        }
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
        HTMLUtils.parse(getHtmlText(), writeOnlyMessage);
        if (images != null) {
            writeOnlyMessage.newLine();
            for (WeiboImage image : images) {
                writeOnlyMessage.add(ImageManager.getImageData(image.getOriginURL()));
            }
        }
        if (pageInfo != null) {
            writeOnlyMessage.newLine().add(pageInfo.getTitle()).newLine().add(ImageManager.getImageData(pageInfo.getImage().getUrl()));
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
                    .newLine().add("原微博信息：").newLine()
                    .add(repostCardDetails.userInfo.getNickName()).add("发了一条微博：").newLine();
            repostCardDetails.parse(writeOnlyMessage);
        }
        return writeOnlyMessage;
    }

}
