package sandtechnology.utils.message;

import sandtechnology.holder.WriteOnlyMessage;

public class DebugMessageHelper extends AbstractMessageHelper {
    @Override
    public void sendPrivateMsg(long qq, WriteOnlyMessage message) {
        System.out.printf("[Private][%d] %s%n", qq, message.toCQString());
    }

    @Override
    public void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message) {
        System.out.printf("[Temp][%d:%d] %s%n", fromGroup, fromQQ, message.toCQString());
    }

    @Override
    public void sendGroupMsg(long group, WriteOnlyMessage message) {
        System.out.printf("[Group][%d] %s%n", group, message.toCQString());
    }
}
