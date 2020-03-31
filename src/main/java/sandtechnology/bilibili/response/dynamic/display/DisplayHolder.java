package sandtechnology.bilibili.response.dynamic.display;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.JsonHelper;

public class DisplayHolder {
    @SerializedName("emoji_info")
    private JsonObject emojiInfo;

    public EmojiChain getEmojiInfo() {
        if (emojiInfo != null) {
            return JsonHelper.getGsonInstance().fromJson(emojiInfo, EmojiChain.class);
        } else {
            return EmojiChain.emptyEmojiChain;
        }
    }
}
