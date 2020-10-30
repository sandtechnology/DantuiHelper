package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.data.bilibili.response.live.RoomInfo;
import sandtechnology.holder.WriteOnlyMessage;

public class StreamingAdapter implements IAdapter {
    @SerializedName("live_play_info")
    RoomInfo roomInfo;


    @Override
    public WriteOnlyMessage getContent(WriteOnlyMessage message) {
        return message.add(roomInfo.getRoomURL()).newLine().add(roomInfo.getTitle()).add("[").add(roomInfo.getStatus().getName()).add("]").newLine().add(roomInfo.getPreview());
    }

    @Override
    public String getActionText() {
        return "直播了以下内容";
    }
}
