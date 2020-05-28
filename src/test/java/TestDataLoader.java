import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sandtechnology.JCQ;
import sandtechnology.bilibili.NormalResponse;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.checker.DynamicChecker;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.JsonHelper;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@DisplayName("DataTest")
public class TestDataLoader {


    private static final DynamicChecker checker = new DynamicChecker(1, new HashSet<>()).setLastTimestamp(1);
    @SerializedName("typeSet")
    Map<String, DynamicData> testMap;
    @SerializedName("normalSet")
    private
    List<NormalResponse> testSet;
    @SerializedName("kaomojiTest")
    String kaomojiTest;

    @Test
    public void test() throws Exception {
            JCQ.main(new String[0]);
            //https://cloud.tencent.com/developer/ask/174364
            //如何在java中输出unicode字符
        ImageManager.setNoImageMode(true);
            PrintWriter printWriter = new PrintWriter(System.out, true);
            System.out.println("===========Test Start==========");
            AtomicInteger atomicInteger = new AtomicInteger(0);
            TestDataLoader loader = JsonHelper.getGsonInstance().fromJson(new InputStreamReader(new FileInputStream("testdata.json"), StandardCharsets.UTF_8), TestDataLoader.class);
            loader.testSet.forEach(
                    data -> {
                        printWriter.println("Test #" + atomicInteger.incrementAndGet());
                        checker.parse(data);
                    }
            );
            loader.testMap.forEach((type, content) -> {
                printWriter.println("===========" + "Test Type: " + type + "===========");
                printWriter.println(content.getMessage().toCQString());
            });
            JCQ.getDemo().exit();
    }
}
