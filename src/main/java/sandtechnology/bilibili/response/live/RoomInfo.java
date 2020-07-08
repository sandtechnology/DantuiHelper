package sandtechnology.bilibili.response.live;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import sandtechnology.utils.ImageManager;

public class RoomInfo {

    @SerializedName("area_name")
    private
    String subArea;
    @SerializedName("parent_area_name")
    private
    String parentArea;
    @SerializedName("cover")
    private
    String coverURL;

    @SerializedName("room_id")
    long roomID;

    @SerializedName("live_start_time")
    long startTime;
    @SerializedName("live_status")
    private
    short status;
    @SerializedName("keyframe")
    private
    String keyframeURL;
    @SerializedName("title")
    private
    String title;

    public long getRoomID() {
        return roomID;
    }

    public long getStartTime() {
        return startTime;
    }

    public ImageManager.CacheImage getImage() {
        return coverURL.isEmpty() ? (keyframeURL.isEmpty() ? ImageManager.emptyImage : ImageManager.getImageData(keyframeURL)) : ImageManager.getImageData(coverURL);
    }

    public String getRoomURL() {
        return "https://live.bilibili.com/" + getRoomID();
    }

    public enum Status {
        NoStreaming("闲置中"),
        Streaming("直播中"),
        PlayingVideo("轮播中"),
        Unknown("未知");
        final String name;

        Status(String str) {
            name = str;
        }

        public String getName() {
            return name;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getKeyframeURL() {
        return keyframeURL;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public String getParentArea() {
        return parentArea;
    }

    public String getSubArea() {
        return subArea;
    }

    @NotNull
    public static Status getStatus(int roundStatus) {
        switch (roundStatus) {
            case 0:
                return Status.NoStreaming;
            case 1:
                return Status.Streaming;
            case 2:
                return Status.PlayingVideo;
            default:
                return Status.Unknown;
        }
    }

    public Status getStatus() {
        return getStatus(status);
    }
}
