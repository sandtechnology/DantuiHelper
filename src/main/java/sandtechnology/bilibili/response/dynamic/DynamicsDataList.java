package sandtechnology.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class DynamicsDataList {

    @SerializedName("has_more")
    private
    int hasMore;

    @SerializedName("next_offset")
    private
    long nextPageOffsetById;

    @SerializedName("cards")
    private
    List<DynamicData> dynamics;

}
