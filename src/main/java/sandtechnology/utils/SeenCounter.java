package sandtechnology.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SeenCounter {

    private final AtomicInteger now = new AtomicInteger(0);


    public SeenCounter firstSeen() {
        now.set(1);
        return this;
    }

    public SeenCounter reset() {
        now.set(0);
        return this;
    }

    public SeenCounter seenAgain() {
        now.incrementAndGet();
        return this;
    }

    public int now() {
        return now.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeenCounter that = (SeenCounter) o;
        return Objects.equals(that, this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(now) * 31;
    }

    @Override
    public String toString() {
        return now.toString();
    }
}
