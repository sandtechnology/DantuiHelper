package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.holder.WriteOnlyMessage;

public class LiveRoomAdapter extends RoomInfo implements IAdapter {

    @SerializedName("roomid")
    private long roomID;

    @Override
    public long getRoomID() {
        return roomID;
    }

    @Override
    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out.add(getRoomURL()).newLine().add(getTitle()).add("[").add(getStatus().getName()).add("]").newLine().add(getPreview());
    }

    @Override
    public String getActionText() {
        return "的直播间";
    }
}
