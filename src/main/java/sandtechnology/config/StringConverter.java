package sandtechnology.config;

public interface StringConverter<T> {
    T convert(String input);
}
