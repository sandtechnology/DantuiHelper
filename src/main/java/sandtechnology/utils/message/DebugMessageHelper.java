package sandtechnology.utils.message;

import sandtechnology.holder.IWriteOnlyMessage;

public class DebugMessageHelper extends AbstractMessageHelper {
    @Override
    public void sendPrivateMsg(long qq, IWriteOnlyMessage message) {
        System.out.printf("[Private][%d] %s%n", qq, message.toCQString());
    }

    @Override
    public void sendTempMsg(long fromGroup, long fromQQ, IWriteOnlyMessage message) {
        System.out.printf("[Temp][%d:%d] %s%n", fromGroup, fromQQ, message.toCQString());
    }

    @Override
    public void sendGroupMsg(long group, IWriteOnlyMessage message) {
        System.out.printf("[Group][%d] %s%n", group, message.toCQString());
    }
}
