package sandtechnology.data.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.display.DisplayHolder;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.AdapterSelector;
import sandtechnology.data.bilibili.response.dynamic.extension.ExtensionHolder;
import sandtechnology.data.bilibili.response.dynamic.rich.RichMessageInfo;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.JsonHelper;

public class DynamicData {
    @SerializedName("desc")
    private final
    DynamicDesc desc;
    //主信息
    @SerializedName("card")
    private final
    String card;
    //存储额外扩展应用信息：如投票
    @SerializedName("extension")
    private ExtensionHolder extension;
    //存储额外富文本信息，如@
    @SerializedName("extend_json")
    private String rawRichMessageInfo;
    private RichMessageInfo richMessageInfo;
    //存储显示用的信息，如B站的表情显示
    @SerializedName("display")
    private DisplayHolder displayContent;

    public DynamicData(DynamicDesc desc, String card, ExtensionHolder extension, String richMessageInfo, DisplayHolder displayContent) {
        this.desc = desc;
        this.card = card;
        this.extension = extension;
        this.richMessageInfo = JsonHelper.getGsonInstance().fromJson(richMessageInfo, RichMessageInfo.class);
        this.displayContent = displayContent;
    }

    public RichMessageInfo getRichMessageInfo() {
        if (richMessageInfo == null) {
            richMessageInfo = JsonHelper.getGsonInstance().fromJson(rawRichMessageInfo, RichMessageInfo.class);
        }
        return richMessageInfo;
    }

    public String getCard() {
        return card;
    }

    public DisplayHolder getDisplayContent() {
        return displayContent == null ? displayContent = new DisplayHolder() : displayContent;
    }


    public ExtensionHolder getExtension() {
        return extension == null ? extension = new ExtensionHolder() : extension;
    }

    public DynamicDesc getDesc() {
        return desc;
    }

    public WriteOnlyMessage getMessage() {
        return AdapterSelector.getString(this);
    }
}
