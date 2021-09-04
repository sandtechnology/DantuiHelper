package sandtechnology.data.bilibili.response.dynamic.lottery;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.StringUtil;
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
    //是否需要发货地址
    @SerializedName("need_post")
    int isPostNeeded;
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

        StringUtil.DelimitedStringBuilder stringBuilder = new StringUtil.DelimitedStringBuilder("并");
        if (isFeedNeeded == 1) {
            stringBuilder.append("关注");
        }
        if (type == 9) {
            stringBuilder.append("在本动态下评论");
        } else {
            stringBuilder.append("转发本动态");
        }
        if (requiredATCount != 0) {
            stringBuilder.append("同时@").append(requiredATCount).append("个人");
        }
        return stringBuilder.build();
    }

    private IWriteOnlyMessage getPrizeInfo() {
        WriteOnlyMessage writeOnlyMessage = new WriteOnlyMessage();
        if (firstPrizeCount != 0) {
            if (firstPrizePicURL != null && !firstPrizePicURL.isEmpty()) {
                writeOnlyMessage.add(ImageManager.getImageData(firstPrizePicURL));
            }
            writeOnlyMessage.add(" 一等奖：").add(firstPrizeName).add(" x").add(firstPrizeCount);
            if (lotteryResult != null) {
                writeOnlyMessage.add("\n 中奖者：").add(lotteryResult.firstPrizeResult.stream().map(LotteryUser::getUserName).collect(Collectors.joining("、")));
            }
        }
        if (secondPrizeCount != 0) {
            if (secondPrizePicURL != null && !secondPrizePicURL.isEmpty()) {
                writeOnlyMessage.add(ImageManager.getImageData(secondPrizePicURL));
            }
            writeOnlyMessage.add("\n 二等奖：").add(secondPrizeName).add(" x").add(secondPrizeCount);
            if (lotteryResult != null) {
                writeOnlyMessage.add("\n 中奖者：").add(lotteryResult.secondPrizeResult.stream().map(LotteryUser::getUserName).collect(Collectors.joining("、")));
            }
        }
        if (thirdPrizeCount != 0) {
            if (thirdPrizePicURL != null && !thirdPrizePicURL.isEmpty()) {
                writeOnlyMessage.add(ImageManager.getImageData(thirdPrizePicURL));
            }
            writeOnlyMessage.add("\n 三等奖：").add(thirdPrizeName).add(" x").add(thirdPrizeCount);
            if (lotteryResult != null) {
                writeOnlyMessage.add("\n 中奖者：\n").add(lotteryResult.thirdPrizeResult.stream().map(LotteryUser::getUserName).collect(Collectors.joining("、")));
            }
        }
        return writeOnlyMessage;
    }

    public Status getStatus() {
        return Status.fromID(status);
    }

    public IWriteOnlyMessage toWriteOnlyMessage() {
        return new WriteOnlyMessage("\n===\n互动抽奖信息：").newLine()
                .add("状态：").add(getStatus().getName()).newLine()
                .add("条件：").add(getJoinLotteryCondition()).newLine()
                .add(getStatus() == Status.Ended ? "" : "开奖时间：剩余" + TimeUtil.getFormattedTimeSec(endTime - Instant.now().getEpochSecond()) + "\n")
                .add("奖品：").newLine().add(getPrizeInfo());
    }

    @Override
    public String toString() {
        return "LotteryData{" +
                "status=" + status +
                ", endTime=" + endTime +
                ", requiredATCount=" + requiredATCount +
                ", isFeedNeeded=" + isFeedNeeded +
                ", isRePostNeeded=" + isPostNeeded +
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

        final int id;
        final String name;

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
