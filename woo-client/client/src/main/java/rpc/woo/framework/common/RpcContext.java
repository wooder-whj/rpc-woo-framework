package rpc.woo.framework.common;

public interface RpcContext<T> {
    Object getBeanByRefName(String beanName);
    T getBeanByType(Class<T> T);
}
