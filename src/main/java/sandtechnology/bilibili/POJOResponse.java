package sandtechnology.bilibili;

import com.google.gson.JsonObject;
import sandtechnology.bilibili.dynamic.POJODynamic;
import sandtechnology.bilibili.response.live.LiveInfo;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

public class POJOResponse {


    private int code;
    private String msg;
    private String message;
    private JsonObject data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMessage() {
        return message;
    }

    public LiveInfo getLiveInfo(){return getGsonInstance().fromJson(data,LiveInfo.class);}

    public POJODynamic getDynamicData() {
        return getGsonInstance().fromJson(data,POJODynamic.class);
    }
}
