package sandtechnology.config;

public class SimpleConverter implements StringConverter<Object> {
    @Override
    public Object convert(String input) {
        return new Object();
    }
}
