package sandtechnology.common;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import sandtechnology.Mirai;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.live.RoomInfo;
import sandtechnology.checker.DynamicChecker;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.Pair;
import sandtechnology.utils.SeenCounter;
import sandtechnology.utils.message.AbstractMessageHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static sandtechnology.utils.ImageManager.getImageData;

public class MessageListener implements ListenerHost {

    private static MessageListener messageListener = new MessageListener();
    private final Map<Long, Pair<SeenCounter, ReadOnlyMessage>> repatingMap = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> waitingMessageMap = new ConcurrentHashMap<>();
    private final List<Pair<Long, Long>> waitingReplyMessageMap = new ArrayList<>(1);
    private final DataContainer dataContainer = DataContainer.getDataContainer();
    private final AbstractMessageHelper messageHelper = DataContainer.getMessageHelper();
    private final Map<Long, Pair<AtomicLong, AtomicLong>> countingMap = DataContainer.getCountingMap();

    public static MessageListener getMessageListener() {
        return messageListener;
    }

    @EventHandler
    public void onTempMsg(TempMessageEvent event) {
        onTempMsg(event.getGroup().getId(), event.getSender().getId(), new ReadOnlyMessage(event.getMessage()));
    }

    public void onTempMsg(long fromGroup, long fromQQ, ReadOnlyMessage message) {
        String msg = message.toString();
        if (msg.equalsIgnoreCase("/info")) {
            messageHelper.sendTempMsg(fromGroup, fromQQ, dataContainer.getVersionMessage());
        } else if (msg.startsWith("/msg")) {
            messageHelper.sendTempMsg(fromGroup, fromQQ, new WriteOnlyMessage("留言已收到！会尽快进行回复"));
            messageHelper.sendPrivateMsg(DataContainer.getMaster(), new WriteOnlyMessage("来自群").add(fromGroup + "临时会话的").add(fromQQ + "向你发送了留言："));
            messageHelper.sendPrivateMsg(DataContainer.getMaster(), message.toWriteOnlyMessage());
        } else {
            messageHelper.sendTempMsg(fromGroup, fromQQ, new WriteOnlyMessage("回复/info查看版本等信息\n回复/msg [内容] 留言"));
        }

    }

    @EventHandler
    public void onPrivateMsg(FriendMessageEvent event) {
        onPrivateMsg(event.getSender().getId(), new ReadOnlyMessage(event.getMessage()));
    }

    public void onPrivateMsg(long fromQQ, ReadOnlyMessage message) {

        String msg = message.toString();
        // 这里处理消息
        if (!waitingReplyMessageMap.isEmpty()) {
            if (msg.equalsIgnoreCase("cancel")) {
                waitingReplyMessageMap.clear();
                messageHelper.sendPrivateMsg(fromQQ, "已取消");
            } else {
                Pair<Long, Long> pair = waitingReplyMessageMap.remove(0);
                messageHelper.sendTempMsg(pair.getFirst(), pair.getLast(), message.toWriteOnlyMessage());
                messageHelper.sendPrivateMsg(fromQQ, "已发送");
            }
        }
        if (waitingMessageMap.containsKey(fromQQ)) {
            if (msg.equalsIgnoreCase("cancel")) {
                waitingMessageMap.remove(fromQQ);
                messageHelper.sendPrivateMsg(fromQQ, "已取消");
            } else {
                messageHelper.sendingGroupMessage(waitingMessageMap.remove(fromQQ), message.toWriteOnlyMessage());
                messageHelper.sendPrivateMsg(fromQQ, "已发送");
            }
        }
        long owner = DataContainer.getMaster();


        if (fromQQ == owner) {
            if (msg.startsWith("/")) {
                String[] command = msg.substring(1).split(" ");
                if (command.length == 1 && command[0].equals("test")) {
                    messageHelper.sendingInfoMessage(new Random().nextInt(2000) + "www");
                }
                if (command[0].equals("sendruki")) {
                    messageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到Ruki粉丝群的内容"));
                    waitingMessageMap.put(fromQQ, dataContainer.getRukiTargetGroup());
                }
                if (command[0].equals("stats")) {
                    messageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage(dataContainer.getCountingData()));
                }
                if (command[0].equals("send")) {
                    if (command.length == 1) {
                        messageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到群的内容"));
                        waitingMessageMap.put(fromQQ, dataContainer.getTargetGroup());
                    } else if (command.length == 2) {
                        messageHelper.sendPrivateMsg(fromQQ, "请发送需要发送到群的内容");
                        waitingMessageMap.put(fromQQ, Collections.singletonList(Long.parseLong(command[1])));
                    }
                }
                if (command[0].equals("reply")) {
                    if (command.length == 3) {
                        messageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("请发送需要发送到临时会话的内容"));
                        waitingReplyMessageMap.add(new Pair<>(Long.parseLong(command[1]), Long.parseLong(command[2])));
                    }
                }
                if (command[0].equals("restart")) {
                    messageHelper.sendPrivateMsg(fromQQ, new WriteOnlyMessage("正在重启...."));
                    Start.exit();
                    if (DataContainer.getDataContainer().getBotType() == DataContainer.BotType.Mirai) {
                        Mirai.getBot().close(null);
                        System.exit(0);
                    } else {
                        Start.start();
                    }
                }
                if (command[0].equals("fetch")) {
                    if (command.length == 2) {
                        new DynamicChecker(Long.parseLong(command[1]), h -> messageHelper.sendingInfoMessage(h.getDynamicsDataList().getDynamics().get(0).getMessage())).check();
                    } else if (command.length == 3) {
                        new DynamicChecker(Long.parseLong(command[1]), h -> {
                            long lastTimestamp = Long.parseLong(command[2]);
                            for (DynamicData dynamicData : h.getDynamicsDataList().getDynamics()) {
                                if (dynamicData.getDesc().getTimestamp() >= lastTimestamp) {
                                    WriteOnlyMessage dynamicDataMessage = dynamicData.getMessage();
                                    messageHelper.sendingInfoMessage(dynamicDataMessage);
                                }
                            }
                        }).check();
                    } else {
                        messageHelper.sendingInfoMessage("/fetch [UID] [timestamp]");
                    }
                }
                if (command[0].equals("get")) {
                    if (command.length == 2) {
                        new HTTPHelper("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=" + Long.parseLong(command[1]), rep -> messageHelper.sendingInfoMessage(rep.getDynamicData().getMessage())).execute();
                    }
                }
                if (command[0].equals("info")) {
                    messageHelper.sendPrivateMsg(fromQQ, dataContainer.getVersionMessage());
                }
            }
        }
    }

    @EventHandler
    public void onGroupMsg(GroupMessageEvent event) {
        onGroupMsg(event.getGroup().getId(), event.getSender().getId(), new ReadOnlyMessage(event.getMessage()));
    }

    private Map<Long, List<Long>> memberListMap = new ConcurrentHashMap<>();
    public void onGroupMsg(long fromGroup, long fromQQ, ReadOnlyMessage readOnlyMessage) {
        if (!countingMap.containsKey(fromGroup)) {
            countingMap.put(fromGroup, new Pair<>(new AtomicLong(1), new AtomicLong(1)));
            memberListMap.put(fromGroup, new CopyOnWriteArrayList<>(Collections.singleton(fromQQ)));
        } else {
            List<Long> memberList = memberListMap.get(fromGroup);
            if (!memberList.contains(fromQQ)) {
                memberList.add(fromQQ);
                countingMap.get(fromGroup).getFirst().incrementAndGet();
            }
            countingMap.get(fromGroup).getLast().incrementAndGet();
        }
        String msg = readOnlyMessage.toString();
        if (fromGroup == DataContainer.getMasterGroup() && fromQQ == DataContainer.getMaster()) {
            onPrivateMsg(fromQQ, readOnlyMessage);
            return;
        }
        //群号
        if (dataContainer.getTargetGroup().contains(fromGroup)) {
            //补充群内小助手的信息
            if (fromQQ == 3351265297L && msg.startsWith("Ruki 开播啦啦啦！！！")) {
                new HTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21403609", response -> {
                    RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                    if (roomInfo.getStatus() == RoomInfo.Status.Streaming) {
                        messageHelper.sendingGroupMessage(532589427L, new WriteOnlyMessage("这个小助手还是不太聪明的样子，我来补上：\n").add(roomInfo.getRoomURL()).newLine().add(getImageData(roomInfo.getCoverURL())));
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
                    messageHelper.sendingGroupMessage(fromGroup, pairData.getLast());
                }
            } else {
                pairData.setLast(readOnlyMessage);
                pairData.getFirst().firstSeen();
            }

        }
    }
}
