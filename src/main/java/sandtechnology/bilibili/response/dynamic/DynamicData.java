package sandtechnology.bilibili.response.dynamic;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DynamicData {
    @SerializedName("desc")
    private
    DynamicDesc desc;
    //主信息
    @SerializedName("card")
    private
    JsonObject card;
    //存储额外扩展应用信息：如投票
    @SerializedName("extension")
    private JsonObject extension;
    //存储额外富文本信息，如@
    @SerializedName("extend_json")
    private JsonObject extend;
    //存储显示用的信息，如B站的表情显示
    @SerializedName("display")
    private JsonObject displayContent;


    public JsonObject getCard() {
        return card;
    }

    public DynamicDesc getDesc() {
        return desc;
    }
}
