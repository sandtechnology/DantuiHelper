package sandtechnology.utils;

import sandtechnology.bilibili.NormalResponse;
import sandtechnology.bilibili.SafeResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HTTPHelper{

   public enum State{
        Init,Success,BiliBiliError,NetworkError,Error
    }

    private final String url;
    private Consumer<NormalResponse> handler;
    private State state;
    private int retry = 0;
    private final Random random = new Random();

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

    public void setHandler(Consumer<NormalResponse> handler) {
        this.handler = handler;
    }

    public void execute() {
        String result = "";
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
            try (BufferedReader stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                result = stream.lines().collect(Collectors.joining("\n"));
            }
            SafeResponse safeResponse = JsonHelper.getGsonInstance().fromJson(result, SafeResponse.class);
            long code = safeResponse.getCode();
            if (code != 0) {
                if (code != 600005 && code != -22 && code != 19002003) {
                    MessageHelper.sendingErrorMessage(new RuntimeException("Unexpected BiliBili Error " + code), "content" + result);
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
            MessageHelper.sendingErrorMessage(e, "Network Error:\n");
        } catch (Exception e) {
            if (retry < 3) {
                ThreadHelper.sleep(2000);
                execute();
                retry++;
            } else {
                retry = 0;
                state = State.Error;
                e.printStackTrace();
                if (result.length() <= 500) {
                    MessageHelper.sendingErrorMessage(e, "Unknown Error:\ncontent:\n" + result);
                } else {
                    MessageHelper.sendingErrorMessage(e, "Unknown Error:\n");
                }
            }
        }
      }


    }
