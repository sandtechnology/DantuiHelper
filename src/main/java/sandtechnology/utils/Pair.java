package sandtechnology.utils;

public class Pair<V1,V2> {
    private V1 v1;
    private V2 v2;

    public Pair(V1 v1,V2 v2){
        this.v1=v1;
        this.v2=v2;
    }

    synchronized public V1 getFirst() {
        return v1;
    }

    synchronized public Pair<V1, V2> setFirst(V1 v1) {
        this.v1 = v1;
        return this;
    }

    synchronized public V2 getLast() {
        return v2;
    }

    synchronized public Pair<V1, V2> setLast(V2 v2) {
        this.v2 = v2;
        return this;
    }

    @Override
    synchronized public boolean equals(Object obj) {
        return obj instanceof Pair && v1.equals(((Pair) obj).getFirst()) && v2.equals(((Pair) obj).v2);
    }

    @Override
    synchronized public int hashCode() {
        return 31 * (super.hashCode() + v1.hashCode() + v2.hashCode());
    }

    @Override
    synchronized public String toString() {
        return super.toString() + "[V1=" + v1 + ",v2=" + v2 + "]";
    }
}
