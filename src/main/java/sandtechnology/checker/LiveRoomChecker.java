package sandtechnology.checker;

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
    private final List<Long> groups;

    public LiveRoomChecker(long roomID, long... groupIDs) {
        groups = new ArrayList<>();
        for (long group : groupIDs) {
            groups.add(group);
        }
        this.roomID = roomID;
        httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomID, response -> {
            RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
            if (roomInfo.getStatus() == RoomInfo.Status.Streaming && lastLive != roomInfo.getStartTime()) {
                lastLive = roomInfo.getStartTime();
                ImageManager.CacheImage image = roomInfo.getImage();
                MessageHelper.sendingGroupMessage(groups, new WriteOnlyMessage("Ruki开播啦！！！").newLine().add(roomInfo.getRoomURL()).add("\n" + roomInfo.getTitle()).newLine().add(image));
            }
        });
    }

    @Override
    public void check() {
        httpHelper.execute();
    }
}
