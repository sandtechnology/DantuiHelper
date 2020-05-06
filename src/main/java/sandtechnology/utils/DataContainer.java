package sandtechnology.utils;

import sandtechnology.Mirai;
import sandtechnology.holder.WriteOnlyMessage;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;

public class DataContainer {
    private static final long master = 1294790523;
    private static final List<Long> targetGroup = new ArrayList<>();
    private static final List<Long> rukiTargetGroup = new ArrayList<>();
    private static final String versionMessage = "机器人信息：" +
            "\n编写者：sandtechnology" +
            "\n版本号：%s" +
            "\n开源地址（基于AGPLv3开源）：https://github.com/sandtechnology/DantuiHelper" +
            "\n%s" +
            "\n%s" +
            "\n运行时间：%s";
    private static final String coreVersion = getVersion() + (isJCQ() ? "（JCQ内核）" : "（Mirai内核，版本号1.0-RC）");
    private static final String coreOpenSourceLink = isJCQ() ? "JCQ项目地址：https://github.com/Meowya/JCQ-CoolQ" : "Mirai项目地址：https://github.com/mamoe/mirai";
    private static final Map<Long, AtomicLong> countingMap = new ConcurrentHashMap<>();

    public static Map<Long, AtomicLong> getCountingMap() {
        return countingMap;
    }

    static {
        rukiTargetGroup.addAll(Arrays.asList(857529607L, 1005397354L, 1035554886L, 739568838L, 752224664L, 1027385586L));
        targetGroup.add(532589427L);
    }

    public static boolean isJCQ() {
        return CQ != null;
    }

    public static String getVersion() {
        return "v2.3.4";
    }

    public static WriteOnlyMessage getVersionMessage() {
        return new WriteOnlyMessage(String.format(versionMessage, coreVersion, coreOpenSourceLink, getMemoryUsage(), getRunningTime()));
    }

    public static String getCountingData() {
        StringBuilder stringBuilder = new StringBuilder("统计信息：\n");
        for (Map.Entry<Long, AtomicLong> entry : countingMap.entrySet()) {
            Long group = entry.getKey();
            AtomicLong count = entry.getValue();
            stringBuilder.append(Mirai.getBot().getGroup(group).getName());
            stringBuilder.append("(");
            stringBuilder.append(group);
            stringBuilder.append("):");
            stringBuilder.append(count);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static String getRunningTime() {
        long offset = ManagementFactory.getRuntimeMXBean().getUptime();
        //秒
        long sec = offset / 1000;
        //毫秒
        long millsec = offset - sec * 1000;
        //分钟
        long minutes = 0;
        //小时
        long hours = 0;
        //天
        long day = 0;
        if (sec >= 60) {
            minutes = sec / 60;
            sec -= minutes * 60;
            if (minutes >= 60) {
                hours = minutes / 60;
                minutes -= hours * 60;
                if (hours >= 24) {
                    day = hours / 24;
                    hours -= day * 24;
                }
            }

        }
        return String.format("%d天%d时%d分%d秒%d毫秒", day, hours, minutes, sec, millsec);
    }

    private static String getMemoryUsage() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        return String.format("堆内存：%.2f/%.2fMB\n堆外内存：%.2f/%dMB", heapMemoryUsage.getUsed() / 1E6, heapMemoryUsage.getMax() / 1E6, nonHeapMemoryUsage.getUsed() / 1E6, nonHeapMemoryUsage.getMax());
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
