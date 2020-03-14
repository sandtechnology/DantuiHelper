import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sandtechnology.BiliBiliDynamicChecker;
import sandtechnology.Dantui;
import sandtechnology.bilibili.POJOResponse;
import sandtechnology.utils.JsonHelper;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


@DisplayName("DataTest")
public class TestDataLoader {


    private static BiliBiliDynamicChecker checker = new BiliBiliDynamicChecker(1).setLastTimestamp(1);
    @SerializedName("test")
    private
    List<POJOResponse> testSet;

    @Test
    public void test() {
        try {
            Dantui.main(new String[0]);
            System.out.println("===========Test Start==========");
            JsonHelper.getGsonInstance().fromJson(new InputStreamReader(new FileInputStream("testdata.json"), StandardCharsets.UTF_8), TestDataLoader.class).testSet.forEach(checker::parse);
            System.out.println("===========Test End==========");
            Dantui.getDemo().exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
