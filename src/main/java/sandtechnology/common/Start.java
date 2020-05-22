package sandtechnology.common;

import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.checker.IChecker;
import sandtechnology.checker.LiveRoomChecker;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.ImageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Start {
    private static Timer timer;
    private static boolean enable;

    public static void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private long time;
            private final List<IChecker> runnables = new ArrayList<>();

            {
                runnables.add(new BiliBiliDynamicChecker(452785178).addGroups(532589427L));
                runnables.add(new BiliBiliDynamicChecker(420249427).addGroups(DataContainer.getDataContainer().getRukiTargetGroup()));
                runnables.add(new LiveRoomChecker(21403609L, DataContainer.getDataContainer().getRukiTargetGroup()));
                runnables.add(new IChecker() {
                    private long lastLive;
                    final HTTPHelper httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21610959", response -> {
                        RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                        if (roomInfo.getStatus() == RoomInfo.Status.Streaming && lastLive != roomInfo.getStartTime()) {
                            lastLive = roomInfo.getStartTime();
                            ImageManager.CacheImage image = roomInfo.getImage();
                            DataContainer.getMessageHelper().sendingGroupMessage(532589427L, new WriteOnlyMessage("星沙姐播了！！！！她播了她播了她播了！！！！！").newLine().add(roomInfo.getRoomURL()).add("\n直播标题：" + roomInfo.getTitle()).add("\n直播封面").add(image));
                        }
                    });

                    @Override
                    public void check() {
                        httpHelper.execute();
                    }
                });
            }


            @Override
            public void run() {
                try {
                    if (time == 3 * 60 * 6) {
                        time = 0;
                        ImageManager.deleteCacheImage();
                    } else {
                        time++;
                    }
                    for (IChecker runnable : runnables) {
                        runnable.check();
                    }
                } catch (Throwable e) {
                    DataContainer.getMessageHelper().sendingErrorMessage(e, "");
                }
            }
        }, 0, 20000);
        enable = true;
    }

    public static void exit() {
        if (enable) {
            timer.cancel();
            enable = false;
        }
    }
}
