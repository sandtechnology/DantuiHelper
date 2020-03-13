package sandtechnology.bilibili.response.liveinfo;

import com.google.gson.annotations.SerializedName;

public class RoomInfo {

    enum Status{
        NoStreaming("闲置"),
        Streaming("直播"),
        PlayingVideo("轮播"),
        Unknown("未知");
        String name;
        Status(String str){
            name=str;
        }

        public String getName() {
            return name;
        }
    }

    @SerializedName("area_name")
    String subArea;
    @SerializedName("parent_area_name")
    String parentArea;

    @SerializedName("live_status")
    private
    short status;
    @SerializedName("cover")
    String coverURL;
    @SerializedName("keyframe")
    String keyframeURL;
    @SerializedName("title")
    String title;

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

    public Status getStatus() {
        switch (status){
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
}
