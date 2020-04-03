package sandtechnology.utils;

import sandtechnology.Mirai;
import sandtechnology.holder.ReadOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;

public class MessageHelper {

    private static final WriteOnlyMessage ERROR = new WriteOnlyMessage("[Error] ");
    private static final WriteOnlyMessage DEBUG = new WriteOnlyMessage("[Debug] ");
    private static final WriteOnlyMessage INFO = new WriteOnlyMessage("[Info] ");

    private MessageHelper() {
    }

    public static void sendingErrorMessage(Throwable e, String... msg) {
        sendPrivateMsg(DataContainer.getMaster(), sendWithPrefix(ERROR, new WriteOnlyMessage(String.join("\n", msg)).add(":" + e.toString() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")))));
    }

    public static void sendingErrorMessage(Throwable e, WriteOnlyMessage... msg) {
        sendPrivateMsg(DataContainer.getMaster(), sendWithPrefix(ERROR, join(msg).add(":" + e.toString() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")))));
    }

    public static void sendingDebugMessage(WriteOnlyMessage... msg) {
        sendPrivateMsg(DataContainer.getMaster(), sendWithPrefix(DEBUG, join(msg)));
    }

    public static void sendPrivateMsg(long qq, WriteOnlyMessage message) {
        if (CQ != null) {
            CQ.sendPrivateMsg(qq, message.toCQString());
        } else {
            Mirai.getBot().getFriend(qq).sendMessage(message.toMessageChain(Mirai.getBot(), WriteOnlyMessage.Type.Friend, qq));
        }
    }

    public static void sendPrivateMsg(long qq, String message) {
        sendPrivateMsg(qq, new WriteOnlyMessage(message));
    }

    public static void sendGroupMsg(long group, WriteOnlyMessage message) {
        if (CQ != null) {
            CQ.sendGroupMsg(group, message.toCQString());
        } else {
            Mirai.getBot().getGroup(group).sendMessage(message.toMessageChain(Mirai.getBot(), WriteOnlyMessage.Type.Group, group));
        }
    }

    private static WriteOnlyMessage sendWithPrefix(WriteOnlyMessage prefix, WriteOnlyMessage msgs) {
        return prefix.clone().add(join(msgs));
    }

    private static WriteOnlyMessage join(WriteOnlyMessage... msgs) {

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

    public static void sendingInfoMessage(WriteOnlyMessage... msg) {
        sendPrivateMsg(DataContainer.getMaster(), sendWithPrefix(INFO, join(msg)));
    }

    public static void sendingInfoMessage(String... strings) {
        sendingInfoMessage(new WriteOnlyMessage(String.join("\n", strings)));
    }

    public static void sendingGroupMessage(long group, ReadOnlyMessage msg) {
        if (CQ != null) {
            CQ.sendGroupMsg(group, msg.get().toString());
        } else {
            Mirai.getBot().getGroup(group).sendMessage(msg.get());
        }
    }

    public static void sendingGroupMessage(Collection<Long> groups, String... msg) {
        sendingGroupMessage(groups, new WriteOnlyMessage(String.join("\n", msg)));
    }

    public static void sendingGroupMessage(long group, String... msg) {
        sendingGroupMessage(group, new WriteOnlyMessage(String.join("\n", msg)));
    }


    public static void sendingGroupMessage(Collection<Long> groups, WriteOnlyMessage... msg) {
        groups.forEach(id -> sendingGroupMessage(id, msg));
    }

    public static void sendingGroupMessage(long group, WriteOnlyMessage... msg) {
        sendGroupMsg(group, join(msg));
    }

}
