package sandtechnology.checker;

import sandtechnology.bilibili.response.live.LiveInfo;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.MessageHelper;

import java.util.ArrayList;
import java.util.List;

public class LiveRoomChecker implements IChecker {

    private final HTTPHelper httpHelper;
    private final long roomID;
    private long lastLive;
    private final List<Long> groups = new ArrayList<>();

    public LiveRoomChecker(long roomID, List<Long> groupIDs) {
        this(roomID);
        groups.addAll(groupIDs);
    }

    private LiveRoomChecker(long roomID) {
        this.roomID = roomID;
        httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomID, response -> {
            LiveInfo liveInfo = response.getLiveInfo();
            RoomInfo roomInfo = liveInfo.getRoomInfo();
            if (roomInfo.getStatus() == RoomInfo.Status.Streaming && lastLive != roomInfo.getStartTime()) {
                lastLive = roomInfo.getStartTime();
                ImageManager.CacheImage image = roomInfo.getImage();
                MessageHelper.sendingGroupMessage(groups, new WriteOnlyMessage(liveInfo.getAnchorInfo().getBaseInfo().getUsername()).add("开播啦！！！").add("\n").add(roomInfo.getRoomURL()).add("\n" + roomInfo.getTitle()).add("\n").add(image));
            }
        });
    }

    public LiveRoomChecker(long roomID, long... groupIDs) {
        this(roomID);
        for (long group : groupIDs) {
            groups.add(group);
        }
    }

    @Override
    public void check() {
        httpHelper.execute();
    }
}
