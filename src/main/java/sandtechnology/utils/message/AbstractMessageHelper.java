package sandtechnology.utils.message;

import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.DataContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class AbstractMessageHelper {
    private final IWriteOnlyMessage ERROR = new WriteOnlyMessage("[Error] ");
    private final IWriteOnlyMessage DEBUG = new WriteOnlyMessage("[Debug] ");
    private final IWriteOnlyMessage INFO = new WriteOnlyMessage("[Info] ");

    protected AbstractMessageHelper() {
    }

    public abstract void sendPrivateMsg(long qq, IWriteOnlyMessage message);

    public abstract void sendTempMsg(long fromGroup, long fromQQ, IWriteOnlyMessage message);

    public abstract void sendGroupMsg(long group, IWriteOnlyMessage message);


    public void sendingErrorMessage(Throwable e, String... msg) {
        StringJoiner joiner = new StringJoiner("\n");
        //错误信息
        for (String s : msg) {
            joiner.add(s);
        }
        //当存在错误信息时添加错误详情
        if (joiner.length() != 0) {
            joiner.add("错误详情：");
        }
        //异常名称
        joiner.add(e.toString());
        //stacktrace
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            joiner.add(stackTraceElement.toString());
        }
        sendGroupMsg(DataContainer.getMasterGroup(), sendWithPrefix(ERROR, new WriteOnlyMessage(joiner.toString())));
    }

    public void sendingErrorMessage(Throwable e, IWriteOnlyMessage msg) {
        sendGroupMsg(DataContainer.getMasterGroup(), sendWithPrefix(ERROR, join(msg).add(":" + e.toString() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")))));
    }

    public void sendingDebugMessage(IWriteOnlyMessage... msg) {
        sendGroupMsg(DataContainer.getMasterGroup(), sendWithPrefix(DEBUG, join(msg)));
    }

    public void sendPrivateMsg(long qq, String message) {
        sendPrivateMsg(qq, new WriteOnlyMessage(message));
    }

    private IWriteOnlyMessage sendWithPrefix(IWriteOnlyMessage prefix, IWriteOnlyMessage... msgs) {
        return prefix.clone().add(join(msgs));
    }

    private IWriteOnlyMessage join(IWriteOnlyMessage... msgs) {

        if (msgs.length >= 1) {
            IWriteOnlyMessage first = msgs[0];
            for (IWriteOnlyMessage msg : msgs) {
                if (first != msg) {
                    first.add(msg);
                }
            }
            return first;
        } else {
            return new WriteOnlyMessage();
        }
    }

    public void sendingInfoMessage(IWriteOnlyMessage msg) {
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

    public void sendingGroupMessage(Collection<Long> groups, IWriteOnlyMessage msg) {
        for (Long id : groups) {
            sendingGroupMessage(id, msg);
        }
    }

    public void sendingGroupMessage(long group, IWriteOnlyMessage msg) {
        sendGroupMsg(group, join(msg));
    }
}
