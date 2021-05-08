package sandtechnology.data.bilibili.response.dynamic.cardextension.vote;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.TimeUtil;

import java.util.List;

public class VoteCard implements Decodable {

    @SerializedName("uid")
    private int uid;

    @SerializedName("default_share")
    private int defaultShare;

    @SerializedName("default_text")
    private String defaultText;

    @SerializedName("options")
    private List<OptionsItem> optionList;

    @SerializedName("endtime")
    private int endTime;

    @SerializedName("join_num")
    private int count;

    @SerializedName("choice_cnt")
    private int highestOptionIndex;

    @SerializedName("vote_id")
    private int voteId;

    @SerializedName("type")
    private int type;

    @SerializedName("desc")
    private String text;

    @SerializedName("status")
    private int status;

    public int getUid() {
        return uid;
    }

    public int getDefaultShare() {
        return defaultShare;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public List<OptionsItem> getOptionList() {
        return optionList;
    }

    public int getEndtime() {
        return endTime;
    }

    public int getCount() {
        return count;
    }

    public int getHighestOptionIndex() {
        return highestOptionIndex;
    }

    public int getVoteId() {
        return voteId;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        return message.add("投票信息：").add(
                "\n名称：").add(text).add(
                "\n状态：").add(endTime >= TimeUtil.nowSec() ? "进行中，剩余" + TimeUtil.getFormattedTimeSec(TimeUtil.offsetFromNowSec(endTime)) : "已结束")
                .add("\n总投票人数：").add(count)
                .add("\n投票选项：\n").add(optionList.stream().map(OptionsItem::getContent).reduce(((writeOnlyMessage, writeOnlyMessage2) -> writeOnlyMessage.newLine().add(writeOnlyMessage2))).orElse(WriteOnlyMessage.emptyMessage()));
    }
}