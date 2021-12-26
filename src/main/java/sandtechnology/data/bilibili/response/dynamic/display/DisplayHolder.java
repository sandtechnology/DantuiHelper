package sandtechnology.data.bilibili.response.dynamic.display;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.cardextension.CardExtension;
import sandtechnology.data.bilibili.response.dynamic.display.contentLink.ContentLinkList;
import sandtechnology.utils.JsonHelper;

import java.util.List;

public class DisplayHolder {
    @SerializedName("emoji_info")
    private JsonObject emojiInfo;
    @SerializedName("origin")
    private DisplayHolder originDisplayHolder;
    @SerializedName("usr_action_txt")
    private String actionText;
    @SerializedName("rich_text")
    private ContentLinkList contentLinkList;
    @SerializedName("topic_info")
    private TopicInfo topicInfo;
    @SerializedName("add_on_card_info")
    private List<CardExtension> cardExtensionList;

    public TopicInfo.NewTopic getNewTopicInfo() {
        return topicInfo == null ? null : topicInfo.getNewTopic();
    }

    public DisplayHolder getOriginDisplayHolder() {
        return originDisplayHolder == null ? new DisplayHolder() : originDisplayHolder;
    }

    public List<CardExtension> getCardExtensionList() {
        return cardExtensionList;
    }

    public ContentLinkList getContentLinkList() {
        return contentLinkList;
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
