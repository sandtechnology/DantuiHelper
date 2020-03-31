package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.MessageOut;

public class VoteAdapter implements IAdapter {

    @SerializedName("content")
    private String content;

    @Override
    public MessageOut addMessage(MessageOut out) {
        return out.add("发了一个投票：\n").add(content);
    }
}
