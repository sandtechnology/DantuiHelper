package sandtechnology.utils;

import sandtechnology.Mirai;
import sandtechnology.config.ConfigLoader;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.message.AbstractMessageHelper;
import sandtechnology.utils.message.CQMessageHelper;
import sandtechnology.utils.message.MiraiMessageHelper;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DataContainer {
    private final static List<Long> targetGroup = Collections.singletonList(532589427L);
    private final static List<Long> rukiTargetGroup = Arrays.asList(857529607L, 1005397354L, 1035554886L, 739568838L, 752224664L, 1027385586L);
    private static final String versionMessage = "机器人信息：" +
            "\n编写者：sandtechnology" +
            "\n版本号：%s%s" +
            "\n开源地址（基于AGPLv3开源）：https://github.com/sandtechnology/DantuiHelper" +
            "\n%s" +
            "\n%s" +
            "\n%s" +
            "\n运行时间：%s";
    private final static Map<Long, AtomicLong> countingMap = new ConcurrentHashMap<>();
    private final static AtomicLong processDataSuccessCount = new AtomicLong();
    private final static AtomicLong processDataFailedCount = new AtomicLong();
    private final static AtomicLong sendMessageCount = new AtomicLong();
    private static DataContainer dataContainer;
    private final BotType botType;

    public static long getMasterGroup() {
        return ConfigLoader.getHolder().getMasterGroup();
    }

    public static String getVersion() {
        return "v2.5";
    }

    public static AtomicLong getProcessDataFailedCount() {
        return processDataFailedCount;
    }

    public static AtomicLong getProcessDataSuccessCount() {
        return processDataSuccessCount;
    }

    public static AtomicLong getSendMessageCount() {
        return sendMessageCount;
    }

    public static Map<Long, AtomicLong> getCountingMap() {
        return countingMap;
    }

    private final AbstractMessageHelper messageHelper;

    private DataContainer(BotType botType) {
        this.botType = botType;
        messageHelper = botType == BotType.JCQ ? new CQMessageHelper() : new MiraiMessageHelper();
    }

    public static DataContainer getDataContainer() {
        return dataContainer;
    }

    public static void initialize(BotType botType) {
        if (dataContainer == null) {
            dataContainer = new DataContainer(botType);
        } else throw new IllegalStateException("Already initialized!");
    }

    public static void sendMessageStat() {
        getSendMessageCount().incrementAndGet();
    }

    public static AbstractMessageHelper getMessageHelper() {
        return getDataContainer().messageHelper;
    }

    public static long getMaster() {
        return ConfigLoader.getHolder().getMaster();
    }


    public WriteOnlyMessage getVersionMessage() {
        return new WriteOnlyMessage(String.format(versionMessage, getVersion(), botType.getCoreDesc(), botType.getOpenSourceLink(), getHandlerInfo(), getMemoryUsage(), getRunningTime()));
    }

    public String getHandlerInfo() {
        long totalProcessData = processDataFailedCount.get() + processDataSuccessCount.get();
        return String.format("发送消息条数：%d\n处理数据条数：%d\n成功条数：%d\n失败条数：%d\n成功率：%.2f %%", sendMessageCount.get(), totalProcessData, processDataSuccessCount.get(), processDataFailedCount.get(), (double) processDataSuccessCount.get() * 100 / (double) totalProcessData);
    }

    public String getCountingData() {
        StringBuilder stringBuilder = new StringBuilder("统计信息：\n");
        List<Map.Entry<Long, AtomicLong>> list = new ArrayList<>(countingMap.entrySet());
        list.sort(Comparator.comparingLong(e -> e.getValue().get()));
        Collections.reverse(list);
        for (Map.Entry<Long, AtomicLong> entry : list) {
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

    public List<Long> getRukiTargetGroup() {
        return rukiTargetGroup;
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
        return String.format("堆内存：%.2f/%.2fMB\n堆外内存：%.2f/%dMB", heapMemoryUsage.getCommitted() / 1E6, heapMemoryUsage.getUsed() / 1E6, nonHeapMemoryUsage.getUsed() / 1E6, nonHeapMemoryUsage.getMax());
    }

    public List<Long> getTargetGroup() {
        return targetGroup;
    }


    public enum BotType {
        Mirai("（Mirai内核，版本号1.0.0）", "Mirai项目地址：https://github.com/mamoe/mirai"),
        JCQ("（JCQ内核，版本号为1.2.7）", "JCQ项目地址：https://github.com/Meowya/JCQ-CoolQ");
        private final String coreDesc;
        private final String openSourceLink;

        BotType(String coreDesc, String openSourceLink) {
            this.coreDesc = coreDesc;
            this.openSourceLink = openSourceLink;
        }

        public String getCoreDesc() {
            return coreDesc;
        }

        public String getOpenSourceLink() {
            return openSourceLink;
        }
    }

    public BotType getBotType() {
        return botType;
    }
}
