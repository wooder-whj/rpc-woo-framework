package rpc.woo.framework.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Mark on a component which will really perform a remote procedure call with operations of send and receive
 * when create a proxy for a {@link Remote} field.
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Client {
}
