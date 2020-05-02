package sandtechnology.common;

import sandtechnology.Mirai;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;
import static sandtechnology.utils.ImageManager.getImageData;

public class Listener {

    private static final Map<Long, Pair<SeenCounter, ReadOnlyMessage>> repatingMap = new ConcurrentHashMap<>();

    private static final Map<Long, List<Long>> waitingMessageMap = new ConcurrentHashMap<>();
    private static final List<Pair<Long, Long>> waitingReplyMessageMap = new ArrayList<>(1);
    private static final Map<Long, AtomicLong> countingMap = DataContainer.getCountingMap();

    public static void onTempMsg(long fromGroup, long fromQQ, ReadOnlyMessage message) {
        String msg = message.toString();
        if (msg.equalsIgnoreCase("/info")) {
            MessageHelper.sendTempMsg(fromGroup, fromQQ, DataContainer.getVersionMessage());
        } else if (msg.startsWith("/msg")) {
            MessageHelper.sendTempMsg(fromGroup, fromQQ, new WriteOnlyMessage("留言已收到！会尽快进行回复"));
            MessageHelper.sendPrivateMsg(DataContainer.getMaster(), new WriteOnlyMessage("来自群").add(fromGroup + "临时会话的").add(fromQQ + "向你发送了留言："));
            MessageHelper.sendPrivateMsg(DataContainer.getMaster(), message.toWriteOnlyMessage());
        } else {
            MessageHelper.sendTempMsg(fromGroup, fromQQ, new WriteOnlyMessage("回复/info查看版本等信息\n回复/msg [内容] 留言"));
        }

    }

    public static void onPrivateMsg(long fromQQ, ReadOnlyMessage message) {

        String msg = message.toString();
        // 这里处理消息
        if (!waitingReplyMessageMap.isEmpty()) {
            if (msg.equalsIgnoreCase("cancel")) {
                waitingReplyMessageMap.clear();
                MessageHelper.sendPrivateMsg(fromQQ, "已取消");
            } else {
                Pair<Long, Long> pair = waitingReplyMessageMap.remove(0);
                MessageHelper.sendTempMsg(pair.getFirst(), pair.getLast(), message.toWriteOnlyMessage());
                MessageHelper.sendPrivateMsg(fromQQ, "已发送");
            }
        }
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
                if (command[0].equals("sendruki")) {
                    MessageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到Ruki粉丝群的内容"));
                    waitingMessageMap.put(fromQQ, DataContainer.getRukiTargetGroup());
                }
                if (command[0].equals("stats")) {
                    MessageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage(DataContainer.getCountingData()));
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
                if (command[0].equals("reply")) {
                    if (command.length == 3) {
                        MessageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到临时会话的内容"));
                        waitingReplyMessageMap.add(new Pair<>(Long.parseLong(command[1]), Long.parseLong(command[2])));
                    }
                }
                if (command[0].equals("restart")) {
                    MessageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("正在重启...."));
                    Start.exit();
                    if (CQ == null) {
                        Mirai.getBot().close(null);
                        System.exit(0);
                    } else {
                        Start.start();
                    }
                }
                if (command[0].equals("fetch")) {
                    if (command.length == 2) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setHandler(h -> MessageHelper.sendingInfoMessage(h.getDynamicsDataList().getDynamics().get(0).getMessage())).check();
                    } else if (command.length == 3) {
                        new BiliBiliDynamicChecker(Long.parseLong(command[1])).setHandler(h -> {
                            for (DynamicData dynamicData : h.getDynamicsDataList().getDynamics()) {
                                WriteOnlyMessage dynamicDataMessage = dynamicData.getMessage();
                                MessageHelper.sendingInfoMessage(dynamicDataMessage);
                            }
                        }).setLastTimestamp(Long.parseLong(command[2])).check();
                    } else {
                        MessageHelper.sendingInfoMessage("/fetch [UID] [timestamp]");
                    }
                }
                if (command[0].equals("get")) {
                    if (command.length == 2) {
                        new HTTPHelper("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=" + Long.parseLong(command[1]), rep -> MessageHelper.sendingInfoMessage(rep.getDynamicData().getMessage())).execute();
                    }
                }
                if (command[0].equals("info")) {
                    MessageHelper.sendPrivateMsg(fromQQ, DataContainer.getVersionMessage());
                }
            }
        }
        }

    public static void onGroupMsg(long fromQQ, long fromGroup, ReadOnlyMessage readOnlyMessage) {
        if (!countingMap.containsKey(fromGroup)) {
            countingMap.put(fromGroup, new AtomicLong(1));
        } else {
            countingMap.get(fromGroup).addAndGet(1);
        }
        String msg = readOnlyMessage.toString();
        if (fromGroup == 1074152108L && fromQQ == DataContainer.getMaster()) {
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
                return;
            }
            Pair<SeenCounter, ReadOnlyMessage> pairData;
            if (repatingMap.containsKey(fromGroup)) {
                pairData = repatingMap.get(fromGroup);
            } else {
                pairData = new Pair<>(new SeenCounter(), readOnlyMessage);
                repatingMap.put(fromGroup, pairData);
            }
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
