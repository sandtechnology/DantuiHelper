package sandtechnology.bilibili.response.live;

import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.ImageManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class RoomStaticInfo implements IRoomInfo {


    //与ISO_LOCAL_DATE_TIME的区别就是以空格为分隔符
    private static DateTimeFormatter biliBiliDateTimeFormatter = new DateTimeFormatterBuilder()
            .append(ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .append(ISO_LOCAL_TIME)
            .toFormatter();

    @SerializedName("roomid")
    private String roomID;
    @SerializedName("live_time")
    private String liveTime;
    @SerializedName("user_cover")
    private String coverURL;
    @SerializedName("uname")
    private String userName;
    @SerializedName("live_status")
    private int status;
    @SerializedName("area_name")
    private String subAreaName;

    private String title;

    @Override
    public long getRoomID() {
        return Long.parseLong(roomID);
    }

    @Override
    public long getStartTime() {
        return LocalDateTime.parse(liveTime, biliBiliDateTimeFormatter).getSecond();
    }

    @Override
    public ImageManager.CacheImage getPreview() {
        return coverURL.isEmpty() ? ImageManager.emptyImage : ImageManager.getImageData(coverURL);
    }

    @Override
    public String getUserName() {
        return userName;
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getParentArea() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSubArea() {
        return subAreaName;
    }

    @Override
    public LiveStatus getStatus() {
        return LiveStatus.getStatus(status);
    }
}
