import org.junit.jupiter.api.Test;
import sandtechnology.utils.IndexIterator;

public class IndexIteratorTest {
    @Test
    public void test() {
        IndexIterator indexIterator = new IndexIterator.IndexIteratorBuilder(1, 600).build();
        int times = 600;
        while (times > 0) {
            indexIterator.next();
            times--;
        }
        if (indexIterator.next() != 1) {
            throw new IllegalStateException();
        }

    }
}
