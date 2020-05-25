package sandtechnology.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Ask {
    Class<? extends StringConverter> converter() default SimpleConverter.class;

    String defaultValue() default "";

    boolean miraiOnly() default false;

    String text() default "请输入%s的值";
}
