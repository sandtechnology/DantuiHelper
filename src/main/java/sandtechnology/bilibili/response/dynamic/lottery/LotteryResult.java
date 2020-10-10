package sandtechnology.bilibili.response.dynamic.lottery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LotteryResult {
    @SerializedName("first_prize_result")
    List<LotteryUser> firstPrizeResult;
    @SerializedName("second_prize_result")
    List<LotteryUser> secondPrizeResult;
    @SerializedName("third_prize_result")
    List<LotteryUser> thirdPrizeResult;
}
