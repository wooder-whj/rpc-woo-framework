package rpc.woo.framework.annotation;

import java.lang.annotation.*;

/**
 * A {@link Remote} field means a service interface which has no local implementation but a remote one.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
    String value() default "";
}
