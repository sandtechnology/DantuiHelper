package sandtechnology.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;

public class DynamicDesc {


    int type;

    long timestamp;

    @SerializedName("dynamic_id_str")
    String dynamicID;

    @SerializedName("origin")
    DynamicDesc originDynamicDesc;

    public long getTimestamp() {
        return timestamp;
    }

    public DynamicDesc getOriginDynamicDesc() {
        return originDynamicDesc;
    }

    public String getDynamicID() {
        return dynamicID;
    }

    public int getType() {
        return type;
    }
}
