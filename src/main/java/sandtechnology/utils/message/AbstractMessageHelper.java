package sandtechnology.utils.message;

import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractMessageHelper {
    private final WriteOnlyMessage ERROR = new WriteOnlyMessage("[Error] ");
    private final WriteOnlyMessage DEBUG = new WriteOnlyMessage("[Debug] ");
    private final WriteOnlyMessage INFO = new WriteOnlyMessage("[Info] ");

    protected AbstractMessageHelper() {
    }

    public abstract void sendPrivateMsg(long qq, WriteOnlyMessage message);

    public abstract void sendTempMsg(long fromGroup, long fromQQ, WriteOnlyMessage message);

    public abstract void sendGroupMsg(long group, WriteOnlyMessage message);


    public void sendingErrorMessage(Throwable e, String... msg) {
        sendGroupMsg(DataContainer.getMasterGroup(), sendWithPrefix(ERROR, new WriteOnlyMessage(String.join("\n", msg)).add(":" + e.toString() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")))));
    }

    public void sendingErrorMessage(Throwable e, WriteOnlyMessage... msg) {
        sendGroupMsg(DataContainer.getMasterGroup(), sendWithPrefix(ERROR, join(msg).add(":" + e.toString() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")))));
    }

    public void sendingDebugMessage(WriteOnlyMessage... msg) {
        sendGroupMsg(DataContainer.getMasterGroup(), sendWithPrefix(DEBUG, join(msg)));
    }

    public void sendPrivateMsg(long qq, String message) {
        sendPrivateMsg(qq, new WriteOnlyMessage(message));
    }

    private WriteOnlyMessage sendWithPrefix(WriteOnlyMessage prefix, WriteOnlyMessage msgs) {
        return prefix.clone().add(join(msgs));
    }

    private WriteOnlyMessage join(WriteOnlyMessage... msgs) {

        if (msgs.length >= 1) {
            WriteOnlyMessage first = msgs[0];
            for (WriteOnlyMessage msg : msgs) {
                if (first != msg) {
                    first.add(msg);
                }
            }
            return first;
        } else {
            return new WriteOnlyMessage();
        }
    }

    public void sendingInfoMessage(WriteOnlyMessage... msg) {
        sendingGroupMessage(DataContainer.getMasterGroup(), sendWithPrefix(INFO, join(msg)));
    }

    public void sendingInfoMessage(String... strings) {
        sendingInfoMessage(new WriteOnlyMessage(String.join("\n", strings)));
    }

    public void sendingGroupMessage(long group, ReadOnlyMessage msg) {
        sendingGroupMessage(group, msg.toWriteOnlyMessage());
    }

    public void sendingGroupMessage(Collection<Long> groups, String... msg) {
        sendingGroupMessage(groups, new WriteOnlyMessage(String.join("\n", msg)));
    }

    public void sendingGroupMessage(long group, String... msg) {
        sendingGroupMessage(group, new WriteOnlyMessage(String.join("\n", msg)));
    }

    public void sendingGroupMessage(Collection<Long> groups, WriteOnlyMessage... msg) {
        for (Long id : groups) {
            sendingGroupMessage(id, msg);
        }
    }

    public void sendingGroupMessage(long group, WriteOnlyMessage... msg) {
        sendGroupMsg(group, join(msg));
    }
}
