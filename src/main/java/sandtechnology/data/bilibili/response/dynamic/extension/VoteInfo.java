package sandtechnology.data.bilibili.response.dynamic.extension;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.TimeUtil;

import java.util.List;

public class VoteInfo implements Decodable {
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
        return "VoteInfo{" +
                "text='" + text + '\'' +
                ", endTime=" + endTime +
                ", voteID=" + voteID +
                ", authorUID=" + authorUID +
                ", type=" + type +
                ", count=" + count +
                ", optionList=" + optionList +
                ", status=" + status +
                '}';
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        return message.add("\n===\n投票信息：").add(
                "\n名称：").add(text).add(
                "\n状态：").add(endTime >= TimeUtil.nowSec() ? "进行中，剩余" + TimeUtil.getFormattedTimeSec(TimeUtil.offsetFromNowSec(endTime)) : "已结束")
                .add("\n总投票人数：").add(count)
                .add("\n投票选项：\n").add(optionList.stream().map(Option::getContent).reduce(((writeOnlyMessage, writeOnlyMessage2) -> writeOnlyMessage.newLine().add(writeOnlyMessage2))).orElse(WriteOnlyMessage.emptyMessage()));

    }

    public static class Option implements Decodable {
        @SerializedName("desc")
        String text;
        @SerializedName("idx")
        int index;
        @SerializedName("img_url")
        String imageUrl;

        @Override
        public String toString() {
            return "VoteOption{" +
                    "text='" + text + '\'' +
                    ", index=" + index +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }

        @Override
        public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
            message.add(index).add(".").add(text);
            if (imageUrl != null) {
                message.newLine().add(ImageManager.getImageData(imageUrl));
            }
            return message;
        }
    }
}
