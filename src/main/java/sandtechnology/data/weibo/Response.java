package sandtechnology.data.weibo;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import sandtechnology.data.IResponse;
import sandtechnology.utils.JsonHelper;

public class Response implements IResponse {
    private int ok;
    @SerializedName("msg")
    private String message;
    private JsonObject data;

    public boolean isOk() {
        return ok == 1;
    }

    public ResponseData getData() {
        return JsonHelper.getGsonInstance().fromJson(data, ResponseData.class);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public JsonObject getRawData() {
        return data;
    }
}
