package sandtechnology.utils.http;

import sandtechnology.data.bilibili.NormalResponse;
import sandtechnology.data.bilibili.SafeResponse;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.JsonHelper;
import sandtechnology.utils.ThreadHelper;

import java.io.IOException;
import java.util.function.Consumer;

public class BiliBiliHTTPHelper extends AbstractHTTPHelper<NormalResponse> {
    public BiliBiliHTTPHelper(String url, Consumer<NormalResponse> handler) {
        super(url, handler);
    }

    protected boolean handleResult(String result) {
        SafeResponse safeResponse = JsonHelper.getGsonInstance().fromJson(result, SafeResponse.class);
        long code = safeResponse.getCode();
        if (code != 0) {
            if (code != 600005 && code != -22 && code != 19002003) {
                //请求过于频繁，请稍后再试
                if (code != -509) {
                    DataContainer.getMessageHelper().sendingErrorMessage(new RuntimeException("Unexpected BiliBili Error " + code), "content" + result);
                } else {
                    ThreadHelper.sleep(20000);
                }
            }
            ThreadHelper.sleep(random.nextInt(10000) + 5000);
            return false;
        } else {
            handler.accept(JsonHelper.getGsonInstance().fromJson(result, NormalResponse.class));
            return true;
        }
    }

    protected boolean handleException(Exception e) {
        if (e instanceof IOException) {
            e.printStackTrace();
            if (e.getMessage().contains("412")) {
                DataContainer.getMessageHelper().sendingErrorMessage(e, "请求遭受Bilibili风控系统拦截，将休眠一小时\n");
                state = State.Banned;
                bannedURL.put(originURL, System.currentTimeMillis() + 3623333 + random.nextInt(8000));
            } else {
                DataContainer.getMessageHelper().sendingErrorMessage(e, "Network Error:\n");
                ThreadHelper.sleep(random.nextInt(5000) + 5000);
            }
            return true;
        }
        return false;
    }
}
