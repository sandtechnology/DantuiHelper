package sandtechnology.data.weibo.card;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import sandtechnology.data.weibo.Response;
import sandtechnology.data.weibo.image.WeiboImage;
import sandtechnology.data.weibo.user.UserInfo;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTMLUtils;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.http.DataGetter;
import sandtechnology.utils.http.WeiboHTTPHelper;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoField.*;

public class CardDetail {

    private static final DataGetter<Response, LongTextCard> longTextCardDataGetter = new DataGetter<>(new WeiboHTTPHelper("https://m.weibo.cn/statuses/extend", null), new TypeToken<LongTextCard>() {
    }, "id");
    private static final DateTimeFormatter weiboTimePattern = new DateTimeFormatterBuilder()
            .appendPattern("EEE MMM ")
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .appendPattern(" Z ")
            .appendValue(YEAR, 4)
            .toFormatter(Locale.ENGLISH);
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
    private static final ZoneOffset OFFSET_UTC_8 = ZoneOffset.ofHours(8);
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

    long timeSec = -1L;

    public String getRawText() {
        return RawText;
    }

    public String getUrl() {
        return "https://m.weibo.cn/status/" + bid;
    }

    public String getSourceDevice() {
        return sourceDevice;
    }

    public String getHtmlText() {
        if (isLongText()) {
            synchronized (longTextCardDataGetter) {
                longTextCardDataGetter.getHttpHelper().setReferer(getUrl());
                longTextCardDataGetter.query(Long.toString(getID()));
                LongTextCard longTextCard = longTextCardDataGetter.getData();
                if (longTextCard != null && longTextCard.isOk()) {
                    htmlText = longTextCard.getLongTextContent();
                    isLongText = false;
                } else {
                    htmlText = "长文本获取失败，原文本：" + htmlText;
                }
            }
        }
        return htmlText;
    }

    public long getTimeSec() {
        if (timeSec != -1L) {
            return timeSec;
        }
        try {
            TemporalAccessor accessor = weiboTimePattern.parse(time);
            return timeSec = accessor.query(TemporalQueries.localTime()).atDate(accessor.query(TemporalQueries.localDate())).toEpochSecond(OFFSET_UTC_8);
        } catch (Exception e) {
            e.printStackTrace();
            DataContainer.getMessageHelper().sendingErrorMessage(e, "Failed to parse weibo time: " + time);
            return timeSec = 0L;
        }
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

    private void parse(IWriteOnlyMessage IWriteOnlyMessage) {
        HTMLUtils.parse(getHtmlText(), IWriteOnlyMessage);
        if (images != null) {
            IWriteOnlyMessage.newLine();
            for (WeiboImage image : images) {
                IWriteOnlyMessage.add(ImageManager.getImageData(image.getOriginURL()));
            }
        }
        if (pageInfo != null) {
            IWriteOnlyMessage.newLine().add(pageInfo.getTitle()).newLine().add(ImageManager.getImageData(pageInfo.getImage().getUrl()));
        }
    }

    public IWriteOnlyMessage toWriteOnlyMessage() {
        IWriteOnlyMessage IWriteOnlyMessage = new WriteOnlyMessage();
        IWriteOnlyMessage
                .add("微博链接：").add(getUrl()).newLine()
                .add(userInfo.getNickName()).add(repostCardDetails != null ? "转发了一条微博：" : "发了一条微博：").newLine();
        parse(IWriteOnlyMessage);
        if (repostCardDetails != null) {
            IWriteOnlyMessage
                    .newLine().add("原微博信息：").newLine()
                    .add(repostCardDetails.userInfo.getNickName()).add("发了一条微博：").newLine();
            repostCardDetails.parse(IWriteOnlyMessage);
        }
        return IWriteOnlyMessage;
    }

}
