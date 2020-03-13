package sandtechnology.bilibili.response.dynamic;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DynamicData {
    @SerializedName("desc")
    DynamicDesc desc;
    @SerializedName("card")
    JsonObject card;

    public JsonObject getCard() {
        return card;
    }

    public DynamicDesc getDesc() {
        return desc;
    }
}
