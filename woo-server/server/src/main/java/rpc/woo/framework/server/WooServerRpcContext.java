package rpc.woo.framework.server;

import rpc.woo.framework.common.AbstractRpcContext;
import rpc.woo.framework.common.RpcContext;

import java.lang.reflect.Field;
import java.util.Map;

public class WooServerRpcContext extends AbstractRpcContext {
    private volatile static RpcContext context=null;
    private WooServerRpcContext() {
        super();
    }

    @Override
    public void storeRemoteProxy(Class<?> type, Field field, Map<String, Object> map) {

    }

    public static RpcContext getServerRpcContext(String basePackage){
        if(context!=null){
            return context;
        }
        synchronized(WooServerRpcContext.class) {
            if(context!=null){
                return context;
            }
            context=new WooServerRpcContext();
        }
        return context;
    }
}
