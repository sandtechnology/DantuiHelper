package sandtechnology.bilibili.response.live;

import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.ImageManager;

public class RoomInfo implements IRoomInfo {

    @SerializedName("area_name")
    private
    String subArea;
    @SerializedName("parent_area_name")
    private
    String parentArea;
    @SerializedName("cover")
    private
    String coverURL;

    @SerializedName("uname")
    private String userName;
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

    @Override
    public long getRoomID() {
        return roomID;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public ImageManager.CacheImage getPreview() {
        return coverURL.isEmpty() ? (keyframeURL.isEmpty() ? ImageManager.emptyImage : ImageManager.getImageData(keyframeURL)) : ImageManager.getImageData(coverURL);
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

        return parentArea;
    }

    @Override
    public String getSubArea() {
        return subArea;
    }

    @Override
    public LiveStatus getStatus() {
        return LiveStatus.getStatus(status);
    }
}
