package sandtechnology.utils.message;

import sandtechnology.Mirai;
import sandtechnology.holder.WriteOnlyMessage;

public class MiraiMessageHelper extends AbstractMessageHelper {

    public MiraiMessageHelper() {
    }

    public void sendPrivateMsg(long qq, WriteOnlyMessage message) {
        super.sendPrivateMsg(qq, message);
        Mirai.getBot().getFriend(qq).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(qq).build()));
    }

    public void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message) {
        super.sendTempMsg(fromGroup, fromQQ, message);
        Mirai.getBot().getGroup(fromGroup).get(fromQQ).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromQQ(fromQQ).fromGroup(fromGroup).type(WriteOnlyMessage.Type.Temp).build()));
    }

    public void sendGroupMsg(long group, WriteOnlyMessage message) {
        super.sendGroupMsg(group, message);
        Mirai.getBot().getGroup(group).sendMessage(message.toMessageChain(new WriteOnlyMessage.ExtraData.ExtraDataBuilder().fromGroup(group).type(WriteOnlyMessage.Type.Group).build()));
    }


}
