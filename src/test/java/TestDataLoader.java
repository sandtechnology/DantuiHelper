import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sandtechnology.JCQ;
import sandtechnology.bilibili.POJOResponse;
import sandtechnology.checker.BiliBiliDynamicChecker;
import sandtechnology.utils.JsonHelper;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@DisplayName("DataTest")
public class TestDataLoader {


    private static BiliBiliDynamicChecker checker = new BiliBiliDynamicChecker(1).setLastTimestamp(1);
    @SerializedName("test")
    private
    List<POJOResponse> testSet;

    public static void main(String[] args) {
        test();
    }

    @Test
    public static void test() {
        try {
            JCQ.main(new String[0]);
            System.out.println("===========Test Start==========");
            AtomicInteger atomicInteger = new AtomicInteger(0);
            JsonHelper.getGsonInstance().fromJson(new InputStreamReader(new FileInputStream("testdata.json"), StandardCharsets.UTF_8), TestDataLoader.class).testSet.forEach(
                    data -> {
                        System.out.println("Testing #" + atomicInteger.incrementAndGet());
                        checker.parse(data);
                    }
            );
            System.out.println("===========Test End==========");
            JCQ.getDemo().exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
