package sandtechnology.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.display.DisplayHolder;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.AdapterSelector;
import sandtechnology.bilibili.response.dynamic.extension.ExtensionHolder;
import sandtechnology.holder.WriteOnlyMessage;

public class DynamicData {
    @SerializedName("desc")
    private
    DynamicDesc desc;
    //主信息
    @SerializedName("card")
    private
    String card;
    //存储额外扩展应用信息：如投票
    @SerializedName("extension")
    private ExtensionHolder extension = new ExtensionHolder();
    //存储额外富文本信息，如@
    @SerializedName("extend_json")
    private String extend;
    //存储显示用的信息，如B站的表情显示
    @SerializedName("display")
    private DisplayHolder displayContent = new DisplayHolder();


    public DynamicData() {
    }

    public DynamicData(DynamicDesc desc, String card, ExtensionHolder extension, String extend, DisplayHolder displayContent) {
        this.desc = desc;
        this.card = card;
        this.extension = extension;
        this.extend = extend;
        this.displayContent = displayContent;
    }

    public String getCard() {
        return card;
    }

    public DisplayHolder getDisplayContent() {
        return displayContent;
    }

    public ExtensionHolder getExtension() {
        return extension;
    }

    public DynamicDesc getDesc() {
        return desc;
    }

    public WriteOnlyMessage getMessage() {
        return AdapterSelector.getString(this);
    }
}
