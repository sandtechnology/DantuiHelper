package sandtechnology.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;

public class MessageHelper {

    private MessageHelper(){}

    public static void sendingErrorMessage(Throwable e,String...msg){
        CQ.sendPrivateMsg(DataContainer.getMaster(), "[Error] " + join(msg) + ":" + e.toString() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
    }
    public static void sendingDebugMessage(String...msg){
        CQ.sendPrivateMsg(DataContainer.getMaster(),"[Debug] "+join(msg));
    }

    private static String join(String...msg){
        return String.join("\n",msg);
    }
    public static void sendingInfoMessage(String...msg){
        CQ.sendPrivateMsg(DataContainer.getMaster(),"[Info] "+join(msg));
    }
    public static void sendingGroupMessage(Collection<Long> groups, String...msg){
        groups.forEach(id->CQ.sendGroupMsg(id,join(msg)));
    }
    public static void sendingGroupMessage(long group,String...msg){
       CQ.sendGroupMsg(group,join(msg));
    }

}
