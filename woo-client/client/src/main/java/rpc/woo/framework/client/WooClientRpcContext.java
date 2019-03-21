package rpc.woo.framework.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import rpc.woo.framework.annotation.Remote;
import rpc.woo.framework.common.AbstractRpcContext;
import rpc.woo.framework.common.RpcContext;

import java.lang.reflect.Field;
import java.util.Map;

//@Component
public class WooClientRpcContext extends AbstractRpcContext {
    Logger logger=LoggerFactory.getLogger(WooClientRpcContext.class);
    private volatile static RpcContext rpcContext =null;
    private WooClientRpcContext(ApplicationContext context) {
        super(context);
    }

    @Override
    public void storeRemoteProxy(Class<?> type, Field field, Map<String, Object> map,ApplicationContext context) {
        if(field.getDeclaredAnnotation(Remote.class)!=null){
            try {
                Object proxy = RpcProxy.newInstance().createRpcProxy(type,context);
                map.put(type.getName(),proxy);
            } catch (Exception e) {
               logger.error(e.getMessage());
            }
        }
    }

    public static RpcContext getClientRpcContext(ApplicationContext context){
        if(rpcContext !=null){
            return rpcContext;
        }
        synchronized (WooClientRpcContext.class){
            if(rpcContext !=null){
                return rpcContext;
            }
            rpcContext = new WooClientRpcContext(context);
        }
        return rpcContext;
    }
}
