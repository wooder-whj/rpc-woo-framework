package rpc.woo.framework.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotate a service implementation of the common service interface declared both on server and client sides.
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {
    String value() default "";
}
