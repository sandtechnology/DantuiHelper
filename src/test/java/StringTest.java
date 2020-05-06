import org.junit.jupiter.api.Test;
import sandtechnology.utils.StringUtil;

public class StringTest {

    @Test
    public void test() {
        assert StringUtil.delete("\u200D这\u200D个\u3000是测\u200D试的转发\u200D删\u3000除\u3000", '\u200D', '\u3000').equals("这个是测试的转发删除");
    }
}
