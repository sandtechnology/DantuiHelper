package sandtechnology.common;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static sandtechnology.utils.ImageManager.getImageData;

public class Listener {

    private static final Map<Long, Pair<SeenCounter, ReadOnlyMessage>> repatingMap = new ConcurrentHashMap<>();

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
                    if (command.length == 1) {
                        MessageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到群的内容"));
                        waitingMessageMap.put(fromQQ, DataContainer.getTargetGroup());
                    } else if (command.length == 2) {
                        MessageHelper.sendPrivateMsg(fromQQ, "请发送需要发送到群的内容");
                        waitingMessageMap.put(fromQQ, Collections.singletonList(Long.parseLong(command[1])));
                    }
                }
                if (command[0].equals("fetch")) {
                    if (command.length == 2) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setHandler(h -> MessageHelper.sendingInfoMessage(h.getDynamicsDataList().getDynamics().get(0).getMessage())).check();
                    } else if (command.length == 3) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setHandler(h -> h.getDynamicsDataList().getDynamics().stream().map(DynamicData::getMessage).forEach(MessageHelper::sendingInfoMessage)).setLastTimestamp(Long.parseLong(command[2])).check();
                    } else {
                        MessageHelper.sendingInfoMessage("/fetch [UID] [timestamp]");
                    }
                }
                if (command[0].equals("get")) {
                    if (command.length == 2) {
                        new HTTPHelper("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=" + Long.parseLong(command[1]), rep -> MessageHelper.sendingInfoMessage(rep.getDynamicData().getMessage())).execute();
                    }
                }
            }
            }
        }

    public static void onGroupMsg(long fromQQ, long fromGroup, ReadOnlyMessage readOnlyMessage) {
        String msg = readOnlyMessage.toString();
        if (fromGroup == 1074152108L) {
            onPrivateMsg(fromQQ, readOnlyMessage);
            return;
        }
        //群号
        if (DataContainer.getTargetGroup().contains(fromGroup)) {
            //补充群内小助手的信息
            if (fromQQ == 3351265297L && msg.startsWith("Ruki 开播啦啦啦！！！")) {
                new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21403609", response -> {
                    RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                    if (roomInfo.getStatus() == RoomInfo.Status.Streaming) {
                        MessageHelper.sendingGroupMessage(532589427L, new WriteOnlyMessage("这个小助手还是不太聪明的样子，我来补上：\n").add(roomInfo.getRoomURL()).newLine().add(getImageData(roomInfo.getCoverURL())));
                    }
                }).execute();
            }
            Pair<SeenCounter, ReadOnlyMessage> pairData;
            if (repatingMap.containsKey(fromGroup)) {
                pairData = repatingMap.get(fromGroup);
            } else {
                pairData = new Pair<>(new SeenCounter(), readOnlyMessage);
                repatingMap.put(fromGroup, pairData);
            }
            System.out.println(pairData.getLast().toString());
            //消息判断
            if (readOnlyMessage.equals(pairData.getLast())) {
                if (pairData.getFirst().seenAgain().now() == 2) {
                    MessageHelper.sendingGroupMessage(fromGroup, pairData.getLast());
                }
            } else {
                pairData.setLast(readOnlyMessage);
                pairData.getFirst().firstSeen();
            }

        }
    }
}
