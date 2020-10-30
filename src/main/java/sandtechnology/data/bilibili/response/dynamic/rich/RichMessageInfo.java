package sandtechnology.data.bilibili.response.dynamic.rich;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.extension.VoteInfo;

import java.util.List;

public class RichMessageInfo {

    //可点击文本信息
    @SerializedName("ctrl")
    List<CtrlMark> ctrlMarkList;
    //抽奖信息
    @SerializedName("lott")
    LotteryInfo lotteryInfo;
    //话题信息
    @SerializedName("topic")
    TopicInfo topicInfo;
    //投票信息
    @SerializedName("vote")
    VoteInfo voteInfo;
    //警告信息
    @SerializedName("dispute")
    DisputeInfo disputeInfo;

    public DisputeInfo getDisputeInfo() {
        return disputeInfo;
    }

    public LotteryInfo getLotteryInfo() {
        return lotteryInfo;
    }
}
