package sandtechnology.utils;

import sandtechnology.bilibili.NormalResponse;
import sandtechnology.bilibili.SafeResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HTTPHelper {

    private static final Map<String, Long> bannedURL = new HashMap<>(2);

    protected String url;
    protected Consumer<NormalResponse> handler;
    private State state;
    private String originURL;
    private String referer;
    private final Random random = new Random();

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public HTTPHelper(String url, Consumer<NormalResponse> handler) {
        this.url = url;
        this.handler = handler;
        state = State.Init;
    }

    public Consumer<NormalResponse> getHandler() {
        return handler;
    }

    public State getState() {
        return state;
    }

    public void setOriginURL(String originURL) {
        this.originURL = originURL;
    }

    public void execute(int retry) {
        String result = "";
        if (bannedURL.containsKey(originURL)) {
            if (bannedURL.get(originURL) <= System.currentTimeMillis()) {
                bannedURL.remove(originURL);
            } else {
                return;
            }
        }
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:75.0) Gecko/20100101 Firefox/75.0");
            urlConnection.setRequestProperty("Origin", originURL);
            urlConnection.setRequestProperty("Referer", referer);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            try (BufferedReader stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                result = stream.lines().collect(Collectors.joining("\n"));
            }
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
                state = State.BiliBiliError;
                ThreadHelper.sleep(random.nextInt(10000) + 5000);
                return;
            }
            handler.accept(JsonHelper.getGsonInstance().fromJson(result, NormalResponse.class));
            state = State.Success;
        } catch (IOException e) {
            state = State.NetworkError;
            e.printStackTrace();
            if (e.getMessage().contains("412")) {
                DataContainer.getMessageHelper().sendingErrorMessage(e, "请求遭受Bilibili风控系统拦截，将休眠一小时\n");
                state = State.BiliBiliBanned;
                bannedURL.put(originURL, System.currentTimeMillis() + 3623333 + random.nextInt(8000));
            } else {
                DataContainer.getMessageHelper().sendingErrorMessage(e, "Network Error:\n");
                ThreadHelper.sleep(random.nextInt(5000) + 5000);
            }
        } catch (Exception e) {
            if (retry < 3) {
                ThreadHelper.sleep(random.nextInt(5000) + 5000);
                execute(++retry);
            } else {
                state = State.Error;
                e.printStackTrace();
                if (result.length() <= 500) {
                    DataContainer.getMessageHelper().sendingErrorMessage(e, "Unknown Error:\ncontent:\n" + result);
                } else {
                    DataContainer.getMessageHelper().sendingErrorMessage(e, "Unknown Error:\n");
                }
                ThreadHelper.sleep(random.nextInt(5000) + 5000);
            }
        } finally {
            if (state != State.Success) {
                DataContainer.getProcessDataFailedCount().incrementAndGet();
            } else {
                DataContainer.getProcessDataSuccessCount().incrementAndGet();
            }
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHandler(Consumer<NormalResponse> handler) {
        this.handler = handler;
    }

    public enum State {
        Init, Success, BiliBiliError, BiliBiliBanned, NetworkError, Error
    }

    public void execute() {
        execute(0);
    }


}
