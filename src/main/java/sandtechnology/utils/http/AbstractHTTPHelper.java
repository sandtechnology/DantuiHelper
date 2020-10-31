package sandtechnology.utils.http;

import sandtechnology.utils.DataContainer;
import sandtechnology.utils.ThreadHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractHTTPHelper<T> {

    protected static final Map<String, Long> bannedURL = new ConcurrentHashMap<>(2);

    protected String url;
    protected final Random random = new Random();
    protected Consumer<T> handler;
    protected State state;
    protected String originURL;
    protected String referer;

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public AbstractHTTPHelper(String url, Consumer<T> handler) {
        this.url = url;
        try {
            this.originURL = new URL(url).getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.handler = handler;
        state = State.Init;
    }

    public Consumer<T> getHandler() {
        return handler;
    }

    public State getState() {
        return state;
    }

    public void setOriginURL(String originURL) {
        this.originURL = originURL;
    }

    public void setHandler(Consumer<T> handler) {
        this.handler = handler;
    }

    /**
     * 处理返回的结果
     *
     * @param result 结果
     */
    abstract void handleResult(String result);

    /***
     * 处理异常
     * @param e 异常
     * @return 是否已处理，未处理时使用默认逻辑
     */
    abstract boolean handleException(Exception e);

    public void setUrl(String url) {
        this.url = url;
    }

    public void execute(int retry) {
        if (bannedURL.containsKey(originURL)) {
            if (bannedURL.get(originURL) <= System.currentTimeMillis()) {
                bannedURL.remove(originURL);
            } else {
                return;
            }
        }
        String result = "";
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
            state = State.QuerySuccess;
            handleResult(result);
        } catch (Exception e) {
            state = e instanceof IOException ? State.NetworkError : State.Error;
            if (!handleException(e)) {
                if (retry < 3) {
                    ThreadHelper.sleep(random.nextInt(5000) + 5000);
                    e.printStackTrace();
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
            }
        } finally {
            if (state != State.DecodeSuccess) {
                DataContainer.getProcessDataFailedCount().incrementAndGet();
            } else {
                DataContainer.getProcessDataSuccessCount().incrementAndGet();
            }
        }
    }

    public enum State {
        Init, QuerySuccess, DecodeSuccess, BiliBiliError, BiliBiliBanned, NetworkError, Error
    }

    public void execute() {
        execute(0);
    }


}
