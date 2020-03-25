package sandtechnology.holder;

public class MessageIn<T> {

    private T obj;

    public MessageIn(T obj) {
        this.obj = obj;
    }

    public T get() {
        return obj;
    }

}
