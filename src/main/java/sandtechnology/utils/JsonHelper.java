package sandtechnology.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {
    private static final Gson gsonInstance = new GsonBuilder().create();

    private JsonHelper(){}
    public static <T>T fromJson(String str,Class<T> clazz){
        return getGsonInstance().fromJson(str,clazz);
    }
    public static Gson getGsonInstance() {
        return gsonInstance;
    }
}
