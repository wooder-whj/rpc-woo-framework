package rpc.woo.framework.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotate a component which a {@link Remote} field
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Caller {
}
