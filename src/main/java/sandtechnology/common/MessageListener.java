package sandtechnology.common;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import sandtechnology.Mirai;
import sandtechnology.checker.DynamicChecker;
import sandtechnology.config.ConfigLoader;
import sandtechnology.config.section.ModuleEnablerData;
import sandtechnology.data.bilibili.response.dynamic.DynamicData;
import sandtechnology.data.bilibili.response.live.LiveStatus;
import sandtechnology.data.bilibili.response.live.RoomInfo;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.Pair;
import sandtechnology.utils.SeenCounter;
import sandtechnology.utils.http.BiliBiliHTTPHelper;
import sandtechnology.utils.message.AbstractMessageHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class MessageListener implements ListenerHost {

    private static final MessageListener messageListener = new MessageListener();
    private final Map<Long, Pair<SeenCounter, ReadOnlyMessage>> repatingMap = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> waitingMessageMap = new ConcurrentHashMap<>();
    private final List<Pair<Long, Long>> waitingReplyMessageMap = new ArrayList<>(1);
    private final DataContainer dataContainer = DataContainer.getDataContainer();
    private final AbstractMessageHelper messageHelper = DataContainer.getMessageHelper();
    private final Map<Long, Pair<AtomicLong, AtomicLong>> countingMap = DataContainer.getCountingMap();

    public static MessageListener getMessageListener() {
        return messageListener;
    }

    private final Map<Long, Pair<Long, SeenCounter>> nudgedCounterMap = new HashMap<>();

    @EventHandler
    public void onNudge(NudgeEvent event) {
        if (!(event.getFrom() instanceof Member)) {
            return;
        }

        Group group = ((Member) event.getFrom()).getGroup();
        if (ConfigLoader.getHolder().getModuleEnablerData().isEnable(ModuleEnablerData.ModuleType.NUDGE_RESPONSE, group.getId())) {
            SeenCounter counter = nudgedCounterMap.merge(group.getId(), new Pair<>(System.currentTimeMillis(), new SeenCounter()),

                    (oldPair, newPair) -> {
                        //10分钟
                        if (System.currentTimeMillis() - oldPair.getFirst() >= 10 * 60 * 60 * 1000) {
                            return newPair;
                        } else {
                            oldPair.getLast().seenAgain();
                            return oldPair;
                        }
                    }
            ).getLast();
            if (counter.now() == 1) {
                group.sendMessage("你们是憨憨吗？为啥戳我！");
            } else if (counter.now() == 3) {
                group.sendMessage("你们好烦...不要怪我戳你哦！");
            } else if (counter.now() == 10) {
                group.sendMessage("不理你们了，哼！");
            }
            if (counter.now() >= 3 && counter.now() < 10) {
                event.getFrom().nudge().sendTo(event.getSubject());
            }
        }

    }

    @EventHandler
    public void onTempMsg(GroupTempMessageEvent event) {
        //onTempMsg(event.getGroup().getId(), event.getSender().getId(), new ReadOnlyMessage(event.getMessage()));
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
                    messageHelper.sendingInfoMessage(new WriteOnlyMessage(dataContainer.getCountingData()));
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
                if (command[0].equals("reload")) {
                    Start.exit();
                    DataContainer.getDataContainer().setReloading(true);
                    ConfigLoader.load();
                    Start.start();
                    DataContainer.getDataContainer().setReloading(false);
                    messageHelper.sendPrivateMsg(fromQQ, "重载完成！");
                }
                if (command[0].equals("fetch")) {
                    if (command.length == 2) {
                        new DynamicChecker(Long.parseLong(command[1]), h -> messageHelper.sendingInfoMessage(h.getDynamicsDataList().getDynamics().get(0).getMessage())).check();
                    } else if (command.length == 3) {
                        new DynamicChecker(Long.parseLong(command[1]), h -> {
                            long lastTimestamp = Long.parseLong(command[2]);
                            for (DynamicData dynamicData : h.getDynamicsDataList().getDynamics()) {
                                if (dynamicData.getDesc().getTimestamp() >= lastTimestamp) {
                                    IWriteOnlyMessage dynamicDataMessage = dynamicData.getMessage();
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
                        new BiliBiliHTTPHelper("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=" + Long.parseLong(command[1]), rep -> messageHelper.sendingInfoMessage(rep.getDynamicData().getMessage())).execute();
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

    private final Map<Long, List<Long>> memberListMap = new ConcurrentHashMap<>();
    public void onGroupMsg(long fromGroup, long fromQQ, ReadOnlyMessage readOnlyMessage) {
        //回复和发送消息模块
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

        //转为string开始解析指令
        String msg = readOnlyMessage.toString();


        //对于mirai好友会话拉跨的临时解决办法
        if (fromGroup == DataContainer.getMasterGroup() && fromQQ == DataContainer.getMaster()) {
            onPrivateMsg(fromQQ, readOnlyMessage);
            return;
        }

        //补充某群内小助手的信息
        if (fromQQ == 3351265297L && msg.startsWith("Ruki 开播啦啦啦！！！")) {
            new BiliBiliHTTPHelper("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=21403609", response -> {
                RoomInfo roomInfo = response.getLiveInfo().getRoomInfo();
                if (roomInfo.getStatus() == LiveStatus.Streaming) {
                    messageHelper.sendingGroupMessage(532589427L, new WriteOnlyMessage("这个小助手还是不太聪明的样子，我来补上：\n").add(roomInfo.getRoomURL()).newLine().add(roomInfo.getPreview()));
                }
            }).execute();
            return;
        }
        //复读模块
        if (ConfigLoader.getHolder().getModuleEnablerData().isEnable(ModuleEnablerData.ModuleType.REPEATER, fromGroup)) {
            Pair<SeenCounter, ReadOnlyMessage> pairData;
            if (repatingMap.containsKey(fromGroup)) {
                pairData = repatingMap.get(fromGroup);
            } else {
                pairData = new Pair<>(new SeenCounter(), readOnlyMessage);
                repatingMap.put(fromGroup, pairData);
            }
            //消息判断

            //是否为空
            if (readOnlyMessage.isEmpty()) {
                return;
            }
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
