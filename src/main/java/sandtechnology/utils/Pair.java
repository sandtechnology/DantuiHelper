package sandtechnology.utils;

public class Pair<V1,V2> {
    private V1 v1;
    private V2 v2;
    public Pair(V1 v1,V2 v2){
        this.v1=v1;
        this.v2=v2;
    }

    public V1 getFirst() {
        return v1;
    }

    public Pair<V1, V2> setFirst(V1 v1) {
        this.v1 = v1;
        return this;
    }

    public Pair<V1, V2> setLast(V2 v2) {
        this.v2 = v2;
        return this;
    }

    public V2 getLast() {
        return v2;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair && v1.equals(((Pair) obj).getFirst()) && v2.equals(((Pair) obj).v2);
    }

    @Override
    public int hashCode() {
        return 31*(super.hashCode()+v1.hashCode()+v2.hashCode());
    }

    @Override
    public String toString() {
        return super.toString()+"[V1="+v1+",v2="+v2+"]";
    }
}
