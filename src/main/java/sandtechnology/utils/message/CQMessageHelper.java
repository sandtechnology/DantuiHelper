package sandtechnology.utils.message;

import sandtechnology.holder.IWriteOnlyMessage;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;
import static sandtechnology.utils.DataContainer.sendMessageStat;

public class CQMessageHelper extends AbstractMessageHelper {

    public CQMessageHelper() {
    }

    public void sendPrivateMsg(long qq, IWriteOnlyMessage message) {
        sendMessageStat();
        CQ.sendPrivateMsg(qq, message.toString());
    }

    public void sendTempMsg(long fromGroup, long fromQQ, IWriteOnlyMessage message) {
        sendMessageStat();
        CQ.sendPrivateMsg(fromQQ, message.toString());
    }

    public void sendGroupMsg(long group, IWriteOnlyMessage message) {
        sendMessageStat();
        CQ.sendGroupMsg(group, message.toString());
    }


}
