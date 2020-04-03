package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class LiveRoomAdapter extends RoomInfo implements IAdapter {

    @SerializedName("round_status")
    private int roundStatus;

    @SerializedName("roomid")
    private long roomID;

    @Override
    public long getRoomID() {
        return roomID;
    }

    @Override
    public Status getStatus() {
        return getStatus(roundStatus);
    }

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("的直播间：\n").add(getRoomURL()).add("\n").add(getTitle()).add("[").add(getStatus().getName()).add("]").add(ImageManager.getImageData(getCoverURL()));
    }
}
