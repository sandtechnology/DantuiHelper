import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sandtechnology.JCQ;
import sandtechnology.bilibili.POJOResponse;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.utils.JsonHelper;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@DisplayName("DataTest")
public class TestDataLoader {


    private static BiliBiliDynamicChecker checker = new BiliBiliDynamicChecker(1).setLastTimestamp(1);
    @SerializedName("typeSet")
    Map<String, DynamicData> testMap;
    @SerializedName("normalSet")
    private
    List<POJOResponse> testSet;

    @Test
    public void test() throws Exception {
        JCQ.main(new String[0]);
        System.out.println("===========Test Start==========");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        TestDataLoader loader = JsonHelper.getGsonInstance().fromJson(new InputStreamReader(new FileInputStream("testdata.json"), StandardCharsets.UTF_8), TestDataLoader.class);
        loader.testSet.forEach(
                data -> {
                    System.out.println("Test #" + atomicInteger.incrementAndGet());
                    checker.parse(data);
                }
        );
        loader.testMap.forEach((type, content) -> {
            System.out.println("===========" + "Test Type: " + type + "===========");
            System.out.println(content.getMessage().toCQString());
        });

        JCQ.getDemo().exit();
    }
}
