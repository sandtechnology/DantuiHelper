package sandtechnology.utils;

import net.mamoe.mirai.Bot;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sobte.cqp.jcq.event.JcqApp.CQ;

public class DataContainer {
    private static final long master = 1294790523;
    private static final List<Long> targetGroup = new ArrayList<>();
    private static final List<Long> rukiTargetGroup = new ArrayList<>();
    private static final String version = "v2.3";
    private static final long startTime = System.currentTimeMillis();
    private static final WriteOnlyMessage message = new WriteOnlyMessage("机器人信息：").newLine().add("编写者：sandtechnology").newLine().add("版本号：").add(version).add(isJCQ() ? "（JCQ内核）" : "（Mirai内核）").newLine().add("开源地址（基于AGPLv3开源）：https://github.com/sandtechnology/DantuiHelper").newLine().add(isJCQ() ? "JCQ项目地址：https://github.com/Meowya/JCQ-CoolQ" : "Mirai项目地址：https://github.com/mamoe/mirai");

    public static boolean isJCQ() {
        return CQ != null;
    }

    public static String getVersion() {
        return version;
    }

    public static WriteOnlyMessage getVersionMessage() {
        return message.clone().add("\n运行时间：").add(getRunningTime());
    }

    private static String getRunningTime() {
        long offset = System.currentTimeMillis() - startTime;
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
            sec = sec - minutes * 60;
            if (minutes >= 60) {
                Bot.getInstance(3459492025L).getFriends();
                hours = minutes / 60;
                minutes = minutes - sec * 60;
                if (hours >= 24) {
                    day = hours / 24;
                    hours = hours - day * 24;
                }
            }

        }
        return day + "天" + hours + "时" + minutes + "分" + sec + "秒" + millsec + "毫秒";
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
