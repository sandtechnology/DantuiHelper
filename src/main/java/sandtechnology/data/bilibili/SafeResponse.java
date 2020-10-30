package sandtechnology.data.bilibili;

/**
 * 偷懒用的POJO类，有的时候B站出错不会返回数据，会导致序列化出错
 */
public class SafeResponse {
    private int code;
    private String msg;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMessage() {
        return message;
    }

}
