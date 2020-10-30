package sandtechnology.utils;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BiliBiliDataGetter<T> extends BiliBiliHTTPHelper {


    private final String[] perms;
    private final String originURL;
    private T data;

    public BiliBiliDataGetter(String url, TypeToken<T> typeToken, String... perms) {
        super(url, null);
        originURL = url;
        this.perms = perms;
        handler = normalResponse -> data = JsonHelper.getGsonInstance().fromJson(normalResponse.getRawData(), typeToken.getType());
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

        url = stringBuilder.toString();
        execute();
    }
}
