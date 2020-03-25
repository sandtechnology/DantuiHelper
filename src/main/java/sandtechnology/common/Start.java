package sandtechnology.common;

import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.checker.IChecker;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.MessageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Start {
    private static Timer timer;

    public static void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private long time;
            private int index = 0;
            private List<IChecker> runnables = new ArrayList<>();

            {
                runnables.add(new BiliBiliDynamicChecker(452785178).addGroups(532589427L));
                runnables.add(new IChecker() {
                    private long lastLive;
                    HTTPHelper httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21610959", response -> {
                        RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                        if (roomInfo.getStatus() == RoomInfo.Status.Streaming && lastLive != roomInfo.getStartTime()) {
                            lastLive = roomInfo.getStartTime();
                            ImageManager.CacheImage image = roomInfo.getImage();
                            MessageHelper.sendingGroupMessage(532589427L, "星沙姐播了！！！！她播了她播了她播了！！！！！", roomInfo.getRoomURL(), "直播标题：" + roomInfo.getTitle(), "直播封面", image == null ? "无" : image.toCQCode());
                        }
                    });

                    @Override
                    public void check() {
                        httpHelper.execute();
                    }
                });
            }

            private IChecker next() {
                index = index == runnables.size() - 1 ? 0 : index + 1;
                return runnables.get(index);
            }

            @Override
            public void run() {
                try {
                    if (time == 3 * 60 * 12) {
                        time = 0;
                        ImageManager.deleteCacheImage();
                    } else {
                        time++;
                    }
                    next().check();
                } catch (Throwable e) {
                    MessageHelper.sendingErrorMessage(e);
                }
            }
        }, 0, 20000);
    }

    public static void exit() {
        timer.cancel();
    }
}
