package sandtechnology.utils.message;

import net.mamoe.mirai.message.data.MessageChain;
import sandtechnology.Mirai;
import sandtechnology.holder.WriteOnlyMessage;

import static sandtechnology.utils.DataContainer.sendMessageStat;

public class MiraiMessageHelper extends AbstractMessageHelper {

    public MiraiMessageHelper() {
    }

    public void sendPrivateMsg(long qq, WriteOnlyMessage message) {
        sendPrivateMsg(qq, message, 1);
    }

    public void sendPrivateMsg(long qq, WriteOnlyMessage message, int times) {
        try {
            sendMessageStat();
            Mirai.getBot().getFriendOrFail(qq).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(qq).build()));
        } catch (Exception e) {
            if (times < 3) {
                sendingErrorMessage(e, "Error when sending message");
                sendPrivateMsg(qq, message, ++times);
            } else {
                sendingErrorMessage(e, "sending message failed, giving up, content" + message.toString());
            }
        }
    }

    public void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message) {
        sendTempMsg(fromGroup, fromQQ, message, 1);
    }

    public void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message, int times) {

        try {
            if (message.isLongMessage()) {
                sendTempMsg(fromGroup, fromQQ, new WriteOnlyMessage("发送消息过长，已经停止发送"));
                return;
            }
            sendMessageStat();
            Mirai.getBot().getGroup(fromGroup).getOrFail(fromQQ).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(fromQQ).fromGroup(fromGroup).type(WriteOnlyMessage.Type.Temp).build()));

        } catch (Exception e) {
            if (times < 3) {
                sendingErrorMessage(e, "Error when sending message");
                sendTempMsg(fromGroup, fromQQ, message, ++times);
            } else {
                sendingErrorMessage(e, "sending message failed, giving up, content" + message.toString());
            }
        }
    }

    public void sendGroupMsg(long group, WriteOnlyMessage message) {
        sendGroupMsg(group, message, 1);
    }


    public void sendGroupMsg(long group, WriteOnlyMessage message, int times) {
        try {
            if (!Mirai.getBot().getGroups().contains(group)) {
                return;
            }
            MessageChain messageChain = message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromGroup(group).type(WriteOnlyMessage.Type.Group).build());
            if (messageChain.isEmpty()) {
                return;
            }
            sendMessageStat();
            Mirai.getBot().getGroup(group).sendMessage(messageChain);
        } catch (Exception e) {
            if (times < 3) {
                sendingErrorMessage(e, "Error when sending message");
                sendGroupMsg(group, message, ++times);
            } else {
                sendingErrorMessage(e, "sending message failed, giving up, content" + message.toString());
            }
        }
    }


}
