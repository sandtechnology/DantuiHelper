package sandtechnology.utils.http;

import com.google.gson.reflect.TypeToken;
import sandtechnology.data.IResponse;
import sandtechnology.utils.JsonHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DataGetter<R extends IResponse, T> {


    AbstractHTTPHelper<R> httpHelper;
    private final String[] perms;
    private final String originURL;
    private T data;

    public DataGetter(AbstractHTTPHelper<R> httpHelper, TypeToken<T> typeToken, String... perms) {
        this.httpHelper = httpHelper;
        originURL = httpHelper.getUrl();
        this.perms = perms;
        httpHelper.setHandler(normalResponse -> data = JsonHelper.getGsonInstance().fromJson(normalResponse.getRawData(), typeToken.getType()));
    }

    public AbstractHTTPHelper<R> getHttpHelper() {
        return httpHelper;
    }

    public T getData() {
        return data;
    }

    public void query(String... values) {
        if (perms.length < values.length) {
            throw new IllegalStateException("Perm length mismatch!");
        }
        StringBuilder stringBuilder = new StringBuilder(originURL).append('?');
        for (int i = 0; i < values.length; i++) {
            try {
                stringBuilder.append(perms[i]).append('=').append(URLEncoder.encode(values[i], "UTF-8"));
                if (i != 0 || i != values.length - 1) {
                    stringBuilder.append('&');
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Error when encoding URL", e);
            }
        }

        httpHelper.setUrl(stringBuilder.toString());
        httpHelper.execute();
    }
}
