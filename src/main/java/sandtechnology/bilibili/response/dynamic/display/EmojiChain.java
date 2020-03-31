package sandtechnology.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.MessageOut;

import java.util.LinkedList;
import java.util.List;

public class EmojiChain {
    public static EmojiChain emptyEmojiChain = new EmojiChain(new LinkedList<>()) {
        @Override
        public MessageOut format(MessageOut out) {
            return out;
        }
    };
    @SerializedName("emoji_details")
    private List<Emoji> emojiList;

    public EmojiChain(List<Emoji> emojiList) {
        this.emojiList = emojiList;
    }

    public MessageOut format(MessageOut out) {
        MessageOut result = out;
        for (Emoji emoji : emojiList) {
            result = emoji.format(result);
        }
        return result;
    }
}
