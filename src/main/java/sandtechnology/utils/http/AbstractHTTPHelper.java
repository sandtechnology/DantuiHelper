package sandtechnology.utils.http;

import sandtechnology.utils.DataContainer;
import sandtechnology.utils.ThreadHelper;

import java.io.*;
import java.net.HttpURLConnection;
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
    protected Map<String, String> header;
    protected Consumer<T> handler;
    protected State state;
    protected String originURL;
    private RequestMethod requestMethod = RequestMethod.GET;
    private String requestData = "";

    public AbstractHTTPHelper(String url, Consumer<T> handler) {
        this.url = url;
        try {
            originURL = new URL(url).getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        header = new ConcurrentHashMap<>(2);
        header.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:75.0) Gecko/20100101 Firefox/75.0");
        header.put("Origin", originURL);
        this.handler = handler;
        state = State.Init;
    }

    public void setReferer(String referer) {
        header.put("Referer", referer);
    }

    public Consumer<T> getHandler() {
        return handler;
    }

    /**
     * 设置请求UA
     *
     * @param userAgent 目标UA
     */
    public void setUserAgent(String userAgent) {
        header.put("User-Agent", userAgent);
    }

    /**
     * 自定义请求头，将会覆盖所有信息
     *
     * @param header 请求头
     */
    public void setHeader(Map<String, String> header) {
        this.header.clear();
        this.header.putAll(header);
    }

    public State getState() {
        return state;
    }

    public void setOriginURL(String originURL) {
        header.put("Origin", originURL);
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public void setHandler(Consumer<T> handler) {
        this.handler = handler;
    }

    /**
     * 设置请求方式（如果可能）
     *
     * @param requestMethod 请求方式
     */
    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
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
            for (Map.Entry<String, String> entry : header.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if (requestMethod == RequestMethod.POST && urlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) urlConnection).setRequestMethod("POST");
            }

            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            if (!requestData.isEmpty() && requestMethod == RequestMethod.POST) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Length", Integer.toString(requestData.length()));
                urlConnection.connect();
                try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()))) {
                    out.write(requestData);
                    out.flush();
                }
            }
            try (BufferedReader stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                result = stream.lines().collect(Collectors.joining("\n"));
            }
            state = handleResult(result) ? State.DecodeSuccess : State.DecodeFailed;
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

    /**
     * 处理返回的结果
     *
     * @param result 结果
     * @return 是否处理成功
     */
    abstract boolean handleResult(String result);

    /***
     * 处理异常
     * @param e 异常
     * @return 是否已处理，未处理时使用默认逻辑
     */
    abstract boolean handleException(Exception e);

    public void setUrl(String url) {
        this.url = url;
    }

    public enum RequestMethod {
        POST, GET
    }

    public enum State {
        Init, DecodeFailed, DecodeSuccess, Banned, NetworkError, Error
    }

    public void execute() {
        execute(0);
    }


}
