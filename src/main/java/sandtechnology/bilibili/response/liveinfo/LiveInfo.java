package sandtechnology.bilibili.response.liveinfo;


import com.google.gson.annotations.SerializedName;

public class LiveInfo  {
    @SerializedName("room_info")
    RoomInfo roomInfo;

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }
}
