package sandtechnology.bilibili.response.dynamic.lottery;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.TimeUtil;

import java.time.Instant;
import java.util.stream.Collectors;

public class LotteryData {

    int status;
    @SerializedName("lottery_time")
    long endTime;
    @SerializedName("lottery_at_num")
    int requiredATCount;
    @SerializedName("lottery_feed_limit")
    int isFeedNeeded;
    @SerializedName("need_post")
    int isRePostNeeded;
    @SerializedName("first_prize")
    int firstPrizeCount;
    @SerializedName("first_prize_cmt")
    String firstPrizeName;
    @SerializedName("first_prize_pic")
    String firstPrizePicURL;
    @SerializedName("second_prize")
    int secondPrizeCount;
    @SerializedName("second_prize_cmt")
    String secondPrizeName;
    @SerializedName("second_prize_pic")
    String secondPrizePicURL;
    @SerializedName("third_prize")
    int thirdPrizeCount;
    @SerializedName("third_prize_cmt")
    String thirdPrizeName;
    @SerializedName("third_prize_pic")
    String thirdPrizePicURL;
    @SerializedName("lottery_result")
    LotteryResult lotteryResult;
    @SerializedName("business_type")
    int type;

    private String getJoinLotteryCondition() {

        StringBuilder stringBuilder = new StringBuilder();
        if (isRePostNeeded == 1 && isFeedNeeded == 1) {
            stringBuilder.append("关注并转发本动态");
        } else if (isRePostNeeded == 1) {
            stringBuilder.append("转发本动态");
        }
        if (requiredATCount != 0) {
            stringBuilder.append("，并同时@").append(requiredATCount).append("个人");
        }
        return stringBuilder.toString();
    }

    private String getPrizeInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        if (firstPrizeCount != 0) {
            stringBuilder.append(" 一等奖：").append(firstPrizeName);
            if (lotteryResult != null) {
                stringBuilder.append("\n 中奖者：").append(lotteryResult.firstPrizeResult.stream().map(LotteryUser::getUserName).collect(Collectors.joining("、")));
            }
        }
        if (secondPrizeCount != 0) {
            stringBuilder.append("\n 二等奖：").append(secondPrizeName);
            if (lotteryResult != null) {
                stringBuilder.append("\n 中奖者：").append(lotteryResult.secondPrizeResult.stream().map(LotteryUser::getUserName).collect(Collectors.joining("、")));
            }
        }
        if (thirdPrizeCount != 0) {
            stringBuilder.append("\n 三等奖：").append(thirdPrizeName);
            if (lotteryResult != null) {
                stringBuilder.append("\n 中奖者：\n").append(lotteryResult.thirdPrizeResult.stream().map(LotteryUser::getUserName).collect(Collectors.joining("、")));
            }
        }
        return stringBuilder.toString();
    }

    public Status getStatus() {
        return Status.fromID(status);
    }

    public WriteOnlyMessage toWriteOnlyMessage() {
        return new WriteOnlyMessage("===\n互动抽奖信息：").newLine()
                .add("状态：").add(getStatus().getName()).newLine()
                .add("条件：").add(getJoinLotteryCondition()).newLine()
                .add(getStatus() == Status.Ended ? "" : "开奖时间：剩余" + TimeUtil.getFormattedTimeSec(endTime - Instant.now().getEpochSecond() * 1000) + "\n")
                .add("奖品：").newLine().add(getPrizeInfo());
    }

    @Override
    public String toString() {
        return "LotteryData{" +
                "status=" + status +
                ", endTime=" + endTime +
                ", requiredATCount=" + requiredATCount +
                ", isFeedNeeded=" + isFeedNeeded +
                ", isRePostNeeded=" + isRePostNeeded +
                ", firstPrizeCount='" + firstPrizeCount + '\'' +
                ", firstPrizeName='" + firstPrizeName + '\'' +
                ", firstPrizePicURL='" + firstPrizePicURL + '\'' +
                ", secondPrizeCount='" + secondPrizeCount + '\'' +
                ", secondPrizeName='" + secondPrizeName + '\'' +
                ", secondPrizePicURL='" + secondPrizePicURL + '\'' +
                ", thirdPrizeCount='" + thirdPrizeCount + '\'' +
                ", thirdPrizeName='" + thirdPrizeName + '\'' +
                ", thirdPrizePicURL='" + thirdPrizePicURL + '\'' +
                ", type=" + type +
                '}';
    }

    enum Status {
        Waiting(0, "等待开奖"),
        Ended(2, "已开奖"),
        Unknown(-1, "未知");

        int id;
        String name;

        Status(int id, String name) {
            this.id = id;
            this.name = name;
        }

        static Status fromID(int id) {
            for (Status value : Status.values()) {
                if (value.id == id) {
                    return value;
                }
            }
            return Unknown;
        }

        public String getName() {
            return name;
        }
    }
}
