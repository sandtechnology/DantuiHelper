package sandtechnology.bilibili;

import com.google.gson.JsonObject;
import sandtechnology.bilibili.response.dynamic.DynamicsDataList;
import sandtechnology.bilibili.response.live.LiveInfo;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

public class NormalResponse extends SafeResponse {

    private JsonObject data;

    public LiveInfo getLiveInfo() {
        return getGsonInstance().fromJson(data, LiveInfo.class);
    }

    public DynamicsDataList getDynamicsDataList() {
        return getGsonInstance().fromJson(data, DynamicsDataList.class);
    }

  }
