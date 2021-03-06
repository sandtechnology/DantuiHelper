package sandtechnology.data.bilibili;

import com.google.gson.JsonObject;
import sandtechnology.data.IResponse;
import sandtechnology.data.bilibili.response.dynamic.DynamicData;
import sandtechnology.data.bilibili.response.dynamic.DynamicsDataList;
import sandtechnology.data.bilibili.response.live.LiveInfo;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

/**
 * B站请求POJO类，有的时候B站出错不会返回数据，因此优先使用SafeResponse类
 */
public class NormalResponse extends SafeResponse implements IResponse {

    private JsonObject data;

    public JsonObject getRawData() {
        return data;
    }

    public LiveInfo getLiveInfo() {
        return getGsonInstance().fromJson(data, LiveInfo.class);
    }

    public DynamicsDataList getDynamicsDataList() {
        return getGsonInstance().fromJson(data, DynamicsDataList.class);
    }

    public DynamicData getDynamicData() {
        return getGsonInstance().fromJson(data.getAsJsonObject("card"), DynamicData.class);
    }

}
