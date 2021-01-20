package sandtechnology.data.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.LinkedList;
import java.util.List;

public class EmojiChain {
    public static final EmojiChain emptyEmojiChain = new EmojiChain(new LinkedList<>()) {
        @Override
        public IWriteOnlyMessage format(IWriteOnlyMessage out) {
            return out;
        }
    };
    @SerializedName("emoji_details")
    private final List<Emoji> emojiList;

    public EmojiChain(List<Emoji> emojiList) {
        this.emojiList = emojiList;
    }

    public IWriteOnlyMessage format(String str) {
        return format(new WriteOnlyMessage(str));
    }

    public IWriteOnlyMessage format(IWriteOnlyMessage out) {
        IWriteOnlyMessage result = out;
        for (Emoji emoji : emojiList) {
            result = emoji.format(result);
        }
        return result;
    }
}
