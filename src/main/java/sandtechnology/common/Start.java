package sandtechnology.common;

import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.DynamicChecker;
import sandtechnology.checker.IChecker;
import sandtechnology.checker.LiveRoomChecker;
import sandtechnology.config.ConfigLoader;
import sandtechnology.config.section.SubscribeConfig;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.ThreadHelper;

import java.util.*;

public class Start {
    private static Timer timer;
    private static boolean enable;

    public static void start() {
        timer = new Timer();
        SubscribeConfig subscribeConfig = ConfigLoader.getHolder().getSubscribeNodeMap();
        if (subscribeConfig.isEmpty()) {
            //生成示例
            subscribeConfig.getSubscribeDynamic().put(123456L, Collections.singleton(123456L));
            subscribeConfig.getSubscribeLiveRoom().put(123456L, Collections.singleton(123456L));
            ConfigLoader.save();
            DataContainer.getMessageHelper().sendingInfoMessage("示例已生成，请编辑配置文件");
            return;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            private long time;
            private final List<IChecker> runnables = new ArrayList<>();

            {
                subscribeConfig.getSubscribeLiveRoom().forEach(
                        (liveRoomID, groupID) -> runnables.add(new LiveRoomChecker(liveRoomID, groupID))
                );
                subscribeConfig.getSubscribeDynamic().forEach(
                        (uid, groupID) -> runnables.add(new DynamicChecker(uid, groupID))
                );
                runnables.add(new IChecker() {
                    private long lastLive;
                    HTTPHelper httpHelper;

                    {
                        httpHelper = new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21610959", response -> {
                            RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                            if (roomInfo.getStatus() == RoomInfo.Status.Streaming && lastLive != roomInfo.getStartTime()) {
                                lastLive = roomInfo.getStartTime();
                                ImageManager.CacheImage image = roomInfo.getImage();
                                DataContainer.getMessageHelper().sendingGroupMessage(532589427L, new WriteOnlyMessage("星沙姐播了！！！！她播了她播了她播了！！！！！").newLine().add(roomInfo.getRoomURL()).add("\n直播标题：" + roomInfo.getTitle()).add("\n直播封面").add(image));
                            }
                        });
                        httpHelper.setOriginURL("https://live.bilibili.com");
                        httpHelper.setReferer("https://live.bilibili.com/21610959");
                    }

                    @Override
                    public void check() {
                        ThreadHelper.sleep(1500);
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
