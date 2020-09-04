package sandtechnology.checker;

import sandtechnology.bilibili.response.live.IRoomInfo;
import sandtechnology.bilibili.response.live.LiveInfo;
import sandtechnology.bilibili.response.live.LiveStatus;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.config.ConfigLoader;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.ImageManager;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 通过记录开播时间和状态做到直播通知的简单直播监听器
 */
public class LiveRoomChecker implements IChecker {

    private final HTTPHelper httpHelper;
    private final long roomID;
    private Set<Long> groups = new LinkedHashSet<>();

    public LiveRoomChecker(long roomID, Set<Long> groupIDs) {
        this(roomID);
        this.groups = groupIDs;
    }

    private LiveRoomChecker(long roomID) {
        this.roomID = roomID;
        if (ConfigLoader.getHolder().isUsingLiveNewAPI()) {
            httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getH5InfoByRoom?room_id=" + roomID, response -> {
                IRoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                long lastLive = ConfigLoader.getHolder().getLiveCheckerData().getLastLive(roomID);
                if (roomInfo.getStatus() == LiveStatus.Streaming && lastLive != roomInfo.getStartTime()) {
                    ConfigLoader.getHolder().getLiveCheckerData().updateLastLive(roomID, roomInfo.getStartTime());
                    ImageManager.CacheImage image = roomInfo.getPreview();
                    DataContainer.getMessageHelper().sendingGroupMessage(groups, new WriteOnlyMessage(roomInfo.getUserName()).add("开播啦！！！\n").add(roomInfo.getRoomURL()).newLine().add(roomInfo.getTitle()).newLine().add(image));
                }
            });
            httpHelper.setOriginURL("https://live.bilibili.com");
            httpHelper.setReferer("https://live.bilibili.com/h5/" + roomID);
        } else {
            httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomID, response -> {
                LiveInfo liveInfo = response.getLiveInfo();
                RoomInfo roomInfo = liveInfo.getRoomInfo();
                long lastLive = ConfigLoader.getHolder().getLiveCheckerData().getLastLive(roomID);
                if (roomInfo.getStatus() == LiveStatus.Streaming && lastLive != roomInfo.getStartTime()) {
                    ConfigLoader.getHolder().getLiveCheckerData().updateLastLive(roomID, roomInfo.getStartTime());
                    ImageManager.CacheImage image = roomInfo.getPreview();
                    DataContainer.getMessageHelper().sendingGroupMessage(groups, new WriteOnlyMessage(liveInfo.getAnchorInfo().getBaseInfo().getUsername()).add("开播啦！！！\n").add(roomInfo.getRoomURL()).newLine().add(roomInfo.getTitle()).newLine().add(image));
                }
            });
            httpHelper.setOriginURL("https://live.bilibili.com");
            httpHelper.setReferer("https://live.bilibili.com/" + roomID);
        }
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
