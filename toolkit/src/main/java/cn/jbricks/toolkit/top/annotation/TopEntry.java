package cn.jbricks.toolkit.top.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface TopEntry {
    String name() default "";

    String module() default "";

    boolean flat() default false;

    boolean ignore() default false;
}
