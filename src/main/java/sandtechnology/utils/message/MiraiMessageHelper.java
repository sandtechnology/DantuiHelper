package sandtechnology.utils.message;

import sandtechnology.Mirai;
import sandtechnology.holder.WriteOnlyMessage;

import static sandtechnology.utils.DataContainer.sendMessageStat;

public class MiraiMessageHelper extends AbstractMessageHelper {

    public MiraiMessageHelper() {
    }

    public void sendPrivateMsg(long qq, WriteOnlyMessage message) {
        try {
            sendMessageStat();
            Mirai.getBot().getFriend(qq).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(qq).build()));
        } catch (Exception e) {
            sendingErrorMessage(e, "Error when sending message");
            sendPrivateMsg(qq, message);
        }
    }

    public void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message) {

        try {
            sendMessageStat();
            Mirai.getBot().getGroup(fromGroup).get(fromQQ).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(fromQQ).fromGroup(fromGroup).type(WriteOnlyMessage.Type.Temp).build()));
        } catch (Exception e) {
            sendingErrorMessage(e, "Error when sending message");
            sendTempMsg(fromGroup, fromQQ, message);
        }
    }

    public void sendGroupMsg(long group, WriteOnlyMessage message) {
        try {
            sendMessageStat();
            Mirai.getBot().getGroup(group).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromGroup(group).type(WriteOnlyMessage.Type.Group).build()));
        } catch (Exception e) {
            sendingErrorMessage(e, "Error when sending message");
            sendGroupMsg(group, message);
        }
    }


}
