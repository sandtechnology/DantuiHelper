package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.data.bilibili.response.live.RoomInfo;
import sandtechnology.holder.IWriteOnlyMessage;

public class LiveRoomAdapter extends RoomInfo implements IAdapter {

    @SerializedName("roomid")
    private long roomID;

    @Override
    public long getRoomID() {
        return roomID;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        return out.add(getRoomURL()).newLine().add(getTitle()).add("[").add(getStatus().getName()).add("]").newLine().add(getPreview());
    }

    @Override
    public String getActionText() {
        return "的直播间";
    }
}
