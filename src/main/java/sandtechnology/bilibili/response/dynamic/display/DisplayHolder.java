package sandtechnology.bilibili.response.dynamic.display;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.JsonHelper;

public class DisplayHolder {
    @SerializedName("emoji_info")
    private JsonObject emojiInfo;
    @SerializedName("origin")
    private DisplayHolder originDisplayHolder;
    @SerializedName("usr_action_txt")
    private String actionText;

    public DisplayHolder getOriginDisplayHolder() {
        return originDisplayHolder == null ? new DisplayHolder() : originDisplayHolder;
    }

    public String getActionText() {
        return actionText;
    }

    public EmojiChain getEmojiInfo() {
        if (emojiInfo != null) {
            return JsonHelper.getGsonInstance().fromJson(emojiInfo, EmojiChain.class);
        } else {
            return EmojiChain.emptyEmojiChain;
        }
    }
}
