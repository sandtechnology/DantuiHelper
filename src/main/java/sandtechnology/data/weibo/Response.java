package sandtechnology.data.weibo;

import com.google.gson.annotations.SerializedName;

public class Response {
    private int ok;
    @SerializedName("msg")
    private String message;
    private ResponseData data;

    public boolean isOk() {
        return ok == 1;
    }

    public ResponseData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
