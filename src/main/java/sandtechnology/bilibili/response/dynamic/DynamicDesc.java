package sandtechnology.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;

public class DynamicDesc {


    private int type;

    private long timestamp;

    @SerializedName("dynamic_id_str")
    private
    String dynamicID;

    @SerializedName("origin")
    private
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
