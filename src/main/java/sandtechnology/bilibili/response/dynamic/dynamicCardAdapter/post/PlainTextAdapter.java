package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.MessageOut;

public class PlainTextAdapter implements IAdapter {

    @SerializedName("content")
    private String content;

    public MessageOut addMessage(MessageOut out) {
        return out.add("发了一条文字动态：\n").add(content);
    }
}
