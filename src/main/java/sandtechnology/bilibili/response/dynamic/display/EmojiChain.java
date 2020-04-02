package sandtechnology.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.LinkedList;
import java.util.List;

public class EmojiChain {
    public static EmojiChain emptyEmojiChain = new EmojiChain(new LinkedList<>()) {
        @Override
        public WriteOnlyMessage format(WriteOnlyMessage out) {
            return out;
        }
    };
    @SerializedName("emoji_details")
    private List<Emoji> emojiList;

    public EmojiChain(List<Emoji> emojiList) {
        this.emojiList = emojiList;
    }

    public WriteOnlyMessage format(WriteOnlyMessage out) {
        WriteOnlyMessage result = out;
        for (Emoji emoji : emojiList) {
            result = emoji.format(result);
        }
        return result;
    }
}
