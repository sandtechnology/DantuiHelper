package sandtechnology.utils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Pair<V1, V2> implements Cloneable {
    private V1 v1;
    private V2 v2;
    private ReentrantReadWriteLock v1Lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock v2Lock = new ReentrantReadWriteLock();

    public Pair(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public V1 getFirst() {
        try {
            v1Lock.readLock().lock();
            return v1;
        } finally {
            v1Lock.readLock().unlock();
        }
    }

    public Pair<V1, V2> setFirst(V1 v1) {
        v1Lock.writeLock().lock();
        this.v1 = v1;
        v1Lock.writeLock().unlock();
        return this;

    }

    public V2 getLast() {
        try {
            v2Lock.readLock().lock();
            return v2;
        } finally {
            v2Lock.readLock().unlock();
        }
    }

    synchronized public Pair<V1, V2> setLast(V2 v2) {
        v2Lock.writeLock().lock();
        this.v2 = v2;
        v2Lock.writeLock().unlock();
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair && getFirst().equals(((Pair<?, ?>) obj).getFirst()) && getLast().equals(((Pair<?, ?>) obj).getLast());
    }

    @Override
    public int hashCode() {
        return 31 * (super.hashCode() + getFirst().hashCode() + getLast().hashCode());
    }

    @Override
    public String toString() {
        return super.toString() + "[V1=" + getFirst() + ",v2=" + getLast() + "]";
    }

}
