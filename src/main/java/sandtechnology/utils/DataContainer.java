package sandtechnology.utils;

import sandtechnology.holder.WriteOnlyMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;

public class DataContainer {
    private static final long master = 1294790523;
    private static final List<Long> targetGroup = new ArrayList<>();
    private static final List<Long> rukiTargetGroup = new ArrayList<>();
    private static final String version = "v2.2";
    private static final WriteOnlyMessage message = new WriteOnlyMessage("机器人信息：").newLine().add("编写者：sandtechnology").newLine().add("版本号：").add(version).add(isJCQ() ? "（JCQ内核）" : "（Mirai内核）").newLine().add("开源地址（基于AGPLv3开源）：https://github.com/sandtechnology/DantuiHelper").newLine().add(isJCQ() ? "JCQ项目地址：https://github.com/Meowya/JCQ-CoolQ" : "Mirai项目地址：https://github.com/mamoe/mirai");

    public static boolean isJCQ() {
        return CQ != null;
    }

    public static String getVersion() {
        return version;
    }

    public static WriteOnlyMessage getVersionMessage() {
        return message;
    }

    static {
        rukiTargetGroup.addAll(Arrays.asList(1035554886L, 739568838L, 752224664L, 1027385586L));
        targetGroup.add(532589427L);
    }

    public static List<Long> getRukiTargetGroup() {
        return rukiTargetGroup;
    }


    public static List<Long> getTargetGroup() {
        return targetGroup;
    }

    public static long getMaster() {
        return master;
    }
}
