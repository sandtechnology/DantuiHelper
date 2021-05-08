package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.data.bilibili.response.live.RoomInfo;
import sandtechnology.holder.IWriteOnlyMessage;

public class StreamingAdapter implements IAdapter {
    @SerializedName("live_play_info")
    RoomInfo roomInfo;


    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        return message.add(roomInfo.getRoomURL()).newLine().add(roomInfo.getTitle()).add("[").add(roomInfo.getStatus().getName()).add("]").newLine().add(roomInfo.getPreview());
    }

    @Override
    public String getActionText() {
        return "直播了以下内容";
    }
}
