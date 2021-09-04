package sandtechnology.utils.message;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.message.data.MessageChain;
import sandtechnology.Mirai;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static sandtechnology.utils.DataContainer.sendMessageStat;

public class MiraiMessageHelper extends AbstractMessageHelper {

    public MiraiMessageHelper() {
    }

    public void sendPrivateMsg(long qq, IWriteOnlyMessage message) {
        sendPrivateMsg(qq, message, 1);
    }

    public void sendPrivateMsg(long qq, IWriteOnlyMessage message, int times) {
        try {
            sendMessageStat();
            Mirai.getBot().getFriendOrFail(qq).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(qq).build()));
        } catch (Exception e) {
            if(message.isErrorMessage()){
                new RuntimeException("错误发送失败，原消息内容：" + message, e).printStackTrace();
                return;
            }
            if (times < 3) {
                sendingErrorMessage(e, "Error when sending message");
                sendPrivateMsg(qq, message, ++times);
            } else {
                sendingErrorMessage(e, "sending message failed, giving up, content" + message);
            }
        }
    }

    public void sendTempMsg(long fromGroup, long fromQQ, IWriteOnlyMessage message) {
        sendTempMsg(fromGroup, fromQQ, message, 1);
    }

    public void sendTempMsg(long fromGroup, long fromQQ, IWriteOnlyMessage message, int times) {

        try {
            if (message.isLongMessage()) {
                sendTempMsg(fromGroup, fromQQ, new WriteOnlyMessage("发送消息过长，已经停止发送"));
                return;
            }
            sendMessageStat();
            Mirai.getBot().getGroup(fromGroup).getOrFail(fromQQ).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(fromQQ).fromGroup(fromGroup).type(WriteOnlyMessage.Type.Temp).build()));

        } catch (Exception e) {
            if(message.isErrorMessage()){
                new RuntimeException("错误发送失败，原消息内容：" + message, e).printStackTrace();
                return;
            }
            if (times < 3) {
                sendingErrorMessage(e, "Error when sending message");
                sendTempMsg(fromGroup, fromQQ, message, ++times);
            } else {
                sendingErrorMessage(e, "sending message failed, giving up, content" + message);
            }
        }
    }

    public void sendGroupMsg(long group, IWriteOnlyMessage message) {
        sendGroupMsg(group, message, 1);
    }

    private static final Map<String, Map<Long, List<MessageChain>>> pendingMessage = new ConcurrentHashMap<>();
    private static volatile boolean waitingOnline = false;

    public void sendGroupMsg(long group, IWriteOnlyMessage message, int times) {
        try {
            if (!Mirai.getBot().getGroups().contains(group)) {
                return;
            }
            MessageChain messageChain = message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromGroup(group).type(WriteOnlyMessage.Type.Group).build());
            if (messageChain.isEmpty()) {
                return;
            }
            sendMessageStat();
            Bot bot = Mirai.getBot();
            if (bot.isOnline()) {
                bot.getGroupOrFail(group).sendMessage(messageChain);
            } else {
                pendingMessage.merge("group",
                        Collections.singletonMap(group, new CopyOnWriteArrayList<>(Collections.singletonList(messageChain)))
                        ,
                        (old, current) -> {
                            current.forEach((groupCode, list) -> old.merge(groupCode, list, (oldList, currentList) -> {
                                oldList.addAll(currentList);
                                return oldList;
                            }));
                            return old;
                        });
                if (!waitingOnline) {
                    bot.getEventChannel().subscribeOnce(BotOnlineEvent.class, botOnlineEvent -> {
                                Iterator<Map.Entry<Long, List<MessageChain>>> iterator = pendingMessage.get("group").entrySet().iterator();
                                while (iterator.hasNext()){
                                    Map.Entry<Long, List<MessageChain>> listEntry= iterator.next();
                                    listEntry.getValue().forEach(
                                      messageChain1 -> bot.getGroupOrFail(listEntry.getKey()).sendMessage(messageChain1));
                                    iterator.remove();

                                }
                        waitingOnline = false;
                            }
                    );
                    waitingOnline = true;
                }
            }
        } catch (Exception e) {
            if(message.isErrorMessage()){
                new RuntimeException("错误发送失败，原消息内容：" + message, e).printStackTrace();
                return;
            }
            if (times < 3) {
                sendingErrorMessage(e, "Error when sending message");
                sendGroupMsg(group, message, ++times);
            } else {
                sendingErrorMessage(e, "sending message failed, giving up, content" + message);
            }
        }
    }


}
