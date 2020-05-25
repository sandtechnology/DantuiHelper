package sandtechnology.checker;

import sandtechnology.bilibili.response.live.LiveInfo;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.ImageManager;

import java.util.LinkedHashSet;
import java.util.Set;

public class LiveRoomChecker implements IChecker {

    private final HTTPHelper httpHelper;
    private final long roomID;
    private long lastLive;
    private Set<Long> groups = new LinkedHashSet<>();

    public LiveRoomChecker(long roomID, Set<Long> groupIDs) {
        this(roomID);
        this.groups = groupIDs;
    }

    private LiveRoomChecker(long roomID) {
        this.roomID = roomID;
        httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomID, response -> {
            LiveInfo liveInfo = response.getLiveInfo();
            RoomInfo roomInfo = liveInfo.getRoomInfo();
            if (roomInfo.getStatus() == RoomInfo.Status.Streaming && lastLive != roomInfo.getStartTime()) {
                lastLive = roomInfo.getStartTime();
                ImageManager.CacheImage image = roomInfo.getImage();
                DataContainer.getMessageHelper().sendingGroupMessage(groups, new WriteOnlyMessage(liveInfo.getAnchorInfo().getBaseInfo().getUsername()).add("开播啦！！！\n").add(roomInfo.getRoomURL()).newLine().add(roomInfo.getTitle()).newLine().add(image));
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
