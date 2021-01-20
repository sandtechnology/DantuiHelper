package sandtechnology.data.bilibili.response.dynamic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DynamicsDataList {

    @SerializedName("has_more")
    private
    int hasMore;

    @SerializedName("next_offset")
    private
    long nextPageOffsetById;

    @SerializedName("cards")
    private
    List<DynamicData> dynamics;

    public boolean hasMore() {
        return hasMore == 1;
    }

    public List<DynamicData> getDynamics() {
        return dynamics;
    }

    public long getNextPageOffsetById() {
        return nextPageOffsetById;
    }
}
