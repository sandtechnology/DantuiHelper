package sandtechnology.common;

import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.MessageHelper;
import sandtechnology.utils.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static sandtechnology.utils.ImageManager.getImageData;

public class Listener {

    private static final Map<Long, Pair<AtomicInteger, ReadOnlyMessage>> repatingMap = new HashMap<>();

    private static final Map<Long, List<Long>> waitingMessageMap = new ConcurrentHashMap<>();
    public static void onPrivateMsg(long fromQQ, ReadOnlyMessage message) {

        String msg = message.toString();
        // 这里处理消息
        if (waitingMessageMap.containsKey(fromQQ)) {
            if (msg.equalsIgnoreCase("cancel")) {
                waitingMessageMap.remove(fromQQ);
                MessageHelper.sendPrivateMsg(fromQQ, "已取消");
            } else {
                MessageHelper.sendingGroupMessage(waitingMessageMap.remove(fromQQ), message.toWriteOnlyMessage());
                MessageHelper.sendPrivateMsg(fromQQ, "已发送");
            }
        }
        long owner = DataContainer.getMaster();
        if (fromQQ == owner) {
            if (msg.startsWith("/")) {
                String[] command = msg.substring(1).split(" ");
                if (command.length == 1 && command[0].equals("test")) {
                    MessageHelper.sendingInfoMessage(new Random().nextInt(2000) + "www");
                }
                if (command[0].equals("send")) {
                    if (command.length == 2) {
                        MessageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到群的内容"));
                        waitingMessageMap.put(fromQQ, DataContainer.getTargetGroup());
                    } else if (command.length == 3) {
                        MessageHelper.sendPrivateMsg(fromQQ, "请发送需要发送到群的内容");
                        waitingMessageMap.put(fromQQ, Collections.singletonList(Long.parseLong(command[1])));
                    }
                }
                if (command[0].equals("fetch")) {
                    if (command.length == 2) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setHandler(h -> MessageHelper.sendingInfoMessage(h.getDynamicsDataList().getDynamics().get(0).getMessage())).check();
                    } else if (command.length == 3) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setLastTimestamp(Long.parseLong(command[2])).check();
                    } else {
                        MessageHelper.sendingInfoMessage("/fetch [UID] [timestamp]");
                    }
                }
            }
        }
    }

    public static void onGroupMsg(long fromQQ, long fromGroup, ReadOnlyMessage readOnlyMessage) {
        String msg = readOnlyMessage.toString();
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
            Pair<AtomicInteger, ReadOnlyMessage> pairData;
            if (repatingMap.containsKey(fromGroup)) {
                pairData = repatingMap.get(fromGroup);
            } else {
                pairData = new Pair<>(new AtomicInteger(0), readOnlyMessage);
                repatingMap.put(fromGroup, pairData);
            }
            //消息判断
            if (readOnlyMessage.equals(pairData.getLast())) {
                if (pairData.getFirst().incrementAndGet() == 2) {
                    MessageHelper.sendingGroupMessage(fromGroup, pairData.getLast());
                }
            } else {
                pairData.setLast(readOnlyMessage);
                pairData.getFirst().set(1);
            }

        }
    }
}
