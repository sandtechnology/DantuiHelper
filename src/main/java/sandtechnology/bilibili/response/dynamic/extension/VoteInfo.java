package sandtechnology.bilibili.response.dynamic.extension;

import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.TimeUtil;

import java.util.List;
import java.util.stream.Collectors;

public class VoteInfo {
    @SerializedName("desc")
    String text;
    @SerializedName("endtime")
    long endTime;
    @SerializedName("vote_id")
    long voteID;
    @SerializedName("uid")
    long authorUID;
    @SerializedName("type")
    int type;
    //总投票数
    @SerializedName("join_num")
    private
    int count;
    //选择
    @SerializedName("options")
    private
    List<Option> optionList;
    //0=正常 4=未找到对应动态？
    @SerializedName("status")
    private
    int status;

    public String getTitle() {
        return text;
    }

    @Override
    public String toString() {
        return "\n===\n投票信息：" +
                "\n名称：" + text +
                "\n状态：" + (endTime >= TimeUtil.nowSec() ? "进行中，剩余" + TimeUtil.getFormattedTimeSec(TimeUtil.offsetFromNowSec(endTime)) : "已结束")
                + "\n总投票人数：" + count
                + "\n投票选项：\n" +
                optionList.stream().map(Option::toString).collect(Collectors.joining("\n"));
    }

    public static class Option {
        @SerializedName("desc")
        String text;
        @SerializedName("idx")
        int index;

        @Override
        public String toString() {
            return index + "." + text;
        }
    }
}
