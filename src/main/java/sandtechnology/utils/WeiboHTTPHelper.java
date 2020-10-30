package sandtechnology.utils;

import sandtechnology.weibo.Response;

import java.util.function.Consumer;

public class WeiboHTTPHelper extends AbstractHTTPHelper<Response> {


    public WeiboHTTPHelper(String url, Consumer<Response> handler) {
        super(url, handler);
    }

    @Override
    void handleResult(String result) {
        Response response = JsonHelper.fromJson(result, Response.class);
        if (response.isOk()) {
            handler.accept(response);
        } else {
            DataContainer.getMessageHelper().sendingErrorMessage(new IllegalStateException("请求失败，错误信息：" + response.getMessage()), "");
        }
    }

    @Override
    boolean handleException(Exception e) {
        return false;
    }
}
