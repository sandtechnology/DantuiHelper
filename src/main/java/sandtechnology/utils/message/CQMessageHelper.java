package sandtechnology.utils.message;

import sandtechnology.holder.WriteOnlyMessage;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;

public class CQMessageHelper extends AbstractMessageHelper {

    public CQMessageHelper() {
    }

    public void sendPrivateMsg(long qq, WriteOnlyMessage message) {
        super.sendPrivateMsg(qq, message);
        CQ.sendPrivateMsg(qq, message.toCQString());
    }

    public void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message) {
        super.sendTempMsg(fromGroup, fromQQ, message);
        CQ.sendPrivateMsg(fromQQ, message.toCQString());
    }

    public void sendGroupMsg(long group, WriteOnlyMessage message) {
        super.sendGroupMsg(group, message);
        CQ.sendGroupMsg(group, message.toCQString());
    }


}
