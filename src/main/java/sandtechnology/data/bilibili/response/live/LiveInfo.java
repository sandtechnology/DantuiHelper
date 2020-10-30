package sandtechnology.data.bilibili.response.live;


import com.google.gson.annotations.SerializedName;

public class LiveInfo {
    @SerializedName("room_info")
    private
    RoomInfo roomInfo;
    @SerializedName("anchor_info")
    private AnchorInfo anchorInfo;

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public AnchorInfo getAnchorInfo() {
        return anchorInfo;
    }
}
