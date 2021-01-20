package sandtechnology.checker;

import sandtechnology.config.ConfigLoader;
import sandtechnology.data.bilibili.response.live.IRoomInfo;
import sandtechnology.data.bilibili.response.live.LiveInfo;
import sandtechnology.data.bilibili.response.live.LiveStatus;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.CacheImage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.http.BiliBiliHTTPHelper;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 通过记录开播时间和状态做到直播通知的简单直播监听器
 */
public class LiveRoomChecker implements IChecker {

    private final BiliBiliHTTPHelper httpHelper;
    private final long roomID;
    private Set<Long> groups = new LinkedHashSet<>();

    public LiveRoomChecker(long roomID, Set<Long> groupIDs) {
        this(roomID);
        this.groups = groupIDs;
    }

    private LiveRoomChecker(long roomID) {
        this.roomID = roomID;
        if (ConfigLoader.getHolder().isUsingLiveNewAPI()) {
            httpHelper = new BiliBiliHTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getH5InfoByRoom?room_id=" + roomID, response -> {
                IRoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                checkAndSendLiveNotice(roomID, roomInfo, roomInfo.getUserName());
            });
            httpHelper.setOriginURL("https://live.bilibili.com");
            httpHelper.setReferer("https://live.bilibili.com/h5/" + roomID);
            httpHelper.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        } else {
            httpHelper = new BiliBiliHTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomID, response -> {
                LiveInfo liveInfo = response.getLiveInfo();
                IRoomInfo roomInfo = liveInfo.getRoomInfo();
                checkAndSendLiveNotice(roomID, roomInfo, liveInfo.getAnchorInfo().getBaseInfo().getUsername());
            });
            httpHelper.setOriginURL("https://live.bilibili.com");
            httpHelper.setReferer("https://live.bilibili.com/" + roomID);
        }
    }

    private void checkAndSendLiveNotice(long roomID, IRoomInfo roomInfo, String username) {
        long lastLive = ConfigLoader.getHolder().getLiveCheckerData().getLastLive(roomID);
        if (roomInfo.getStatus() == LiveStatus.Streaming && lastLive != roomInfo.getStartTime()) {
            ConfigLoader.getHolder().getLiveCheckerData().updateLastLive(roomID, roomInfo.getStartTime());
            CacheImage image = roomInfo.getPreview();
            DataContainer.getMessageHelper().sendingGroupMessage(groups, new WriteOnlyMessage(username).add("开播啦！！！\n").add(roomInfo.getRoomURL()).newLine().add(roomInfo.getTitle()).newLine().add(image));
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
