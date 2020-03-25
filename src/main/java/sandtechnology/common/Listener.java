package sandtechnology.common;

import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.MessageHelper;
import sandtechnology.utils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static sandtechnology.utils.ImageManager.getImageData;

public class Listener {

    private static final Map<Long, Pair<AtomicInteger, String>> repatingMap = new HashMap<>();

    public static void onPrivateMsg(long fromQQ, String msg) {
        // 这里处理消息
        long owner = DataContainer.getMaster();
        if (fromQQ == owner) {
            if (msg.startsWith("/")) {
                String[] command = msg.substring(1).split(" ");
                if (command.length == 1 && command[0].equals("test")) {
                    MessageHelper.sendingInfoMessage(new Random().nextInt(2000) + "www");
                }
                if (command[0].equals("send")) {
                    if (command.length == 2)
                        MessageHelper.sendingGroupMessage(DataContainer.getTargetGroup(), command[1]);
                    else if (command.length == 3) {
                        MessageHelper.sendingGroupMessage(Long.parseLong(command[1]), command[2]);
                    }
                }
                if (command[0].equals("fetch")) {
                    if (command.length == 2) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setHandler(h -> MessageHelper.sendingInfoMessage(h.getDynamicData().getDynamics().get(0).getInfo())).check();
                    } else if (command.length == 3) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setLastTimestamp(Long.parseLong(command[2])).check();
                    } else {
                        MessageHelper.sendingInfoMessage("/fetch [UID] [timestamp]");
                    }
                }
            }
        }
    }

    public static void onGroupMsg(long fromQQ, long fromGroup, String msg) {
        //群号
        if (DataContainer.getTargetGroup().contains(fromGroup)) {
            if (fromQQ == 3351265297L && msg.startsWith("Ruki 开播啦啦啦！！！")) {
                new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21403609", response -> {
                    RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                    if (roomInfo.getStatus() == RoomInfo.Status.Streaming) {
                        MessageHelper.sendingGroupMessage(532589427L, "这个小助手还是不太聪明的样子，我来补上：", roomInfo.getRoomURL(), getImageData(roomInfo.getCoverURL()).toCQCode());
                    }
                }).execute();
            }
            Pair<AtomicInteger, String> pairData;
            if (repatingMap.containsKey(fromGroup)) {
                pairData = repatingMap.get(fromGroup);
            } else {
                pairData = new Pair<>(new AtomicInteger(0), msg);
                repatingMap.put(fromGroup, pairData);
            }
            //消息判断
            if (msg.equals(pairData.getLast())) {
                if (pairData.getFirst().incrementAndGet() == 2) {
                    MessageHelper.sendingGroupMessage(fromGroup, pairData.getLast());
                }
            } else {
                pairData.setLast(msg);
                pairData.getFirst().set(1);
            }

        }
    }
}
