package rpc.woo.framework.client;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import rpc.woo.framework.annotation.Remote;
import rpc.woo.framework.common.ProtocolBean;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class RpcProxyFactory {
    private static volatile RpcProxyFactory rpcProxyFactory =null;

    private RpcProxyFactory(){}

    public static RpcProxyFactory newInstance(){
        if(rpcProxyFactory !=null){
            return rpcProxyFactory;
        }
        synchronized (RpcProxyFactory.class){
            if(rpcProxyFactory !=null){
                return rpcProxyFactory;
            }
            rpcProxyFactory =new RpcProxyFactory();
            return rpcProxyFactory;
        }
    }

    /**
     * Create and set a rpc proxy for a {@link Remote} field of a {@link rpc.woo.framework.annotation.Caller} component
     * @param type a {@link rpc.woo.framework.annotation.Caller} component
     * @param context the {@link ApplicationContext}
     * @throws Exception
     */
    public void createRpcProxy(Class<?> type, ApplicationContext context) throws Exception {
        for(Field field:type.getDeclaredFields()){
            Remote annotation = field.getDeclaredAnnotation(Remote.class);
            if(annotation!=null){
                Class<?> fieldType = field.getType();
                if(!fieldType.isInterface()){
                    return;
                }
                Object proxyInstance=Proxy.newProxyInstance(fieldType.getClassLoader(),
                        new Class<?>[]{fieldType}, new InvocationHandler() {
                            @Override
                            @SuppressWarnings("unchecked")
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                ProtocolBean bean=new ProtocolBean();
                                String typeName=fieldType.getTypeName();
                                bean.setInterfaceRefName(typeName);
                                bean.setMethodName(method.getName());
                                bean.setArgs(args);
                                bean.setReturnType(method.getReturnType().getName());
                                bean.setArgRefNames(getMethodArgsReferenceNames(method));
                                RpcClient<ProtocolBean> wooRpcClient = context.getBean(WooRpcClient.class);
                                ProtocolBean protocolBean=null;
                                /**
                                 * an atomic operation group of send and receive for concurrency scenario
                                 */
                                synchronized (wooRpcClient){
                                    wooRpcClient.send(bean);
                                    protocolBean = wooRpcClient.receive();
                                }
                                if(protocolBean==null){
                                    return null;
                                }
                                Class<?> returnType = method.getReturnType();
                                if(returnType.isAssignableFrom(List.class)){
                                    /**
                                     * handle List return object, we hope return the exact type same as the declaration
                                     * of the method of the interface rather than JSONArray<JSONObject>.
                                     */
                                    List returnObj=null;
                                    if(returnType.isInterface()){
                                        returnObj=new ArrayList();
                                    }else{
                                        returnObj=(List)returnType.newInstance();
                                    }
                                    for(Object obj:((List)protocolBean.getReturnObject())){
                                        returnObj.add(JSON.parseObject(JSON.toJSONString(obj), getParameterizedType(method)));
                                    }
                                    return returnObj;
                                }else{
                                    return returnType.isInterface()?protocolBean.getReturnObject():
                                            JSON.parseObject(JSON.toJSONString(protocolBean.getReturnObject()),returnType);
                                }
                            }
                            private Class<?> getParameterizedType(Method method){
                                Type returnType = method.getGenericReturnType();
                                if(returnType instanceof ParameterizedType){
                                    ParameterizedType type = (ParameterizedType) returnType;
                                    Type[] typeArguments = type.getActualTypeArguments();
                                    return (Class<?>)typeArguments[0];
                                }
                                return (Class<?>)returnType;
                            }
                        });
                field.setAccessible(true);
                //set the proxy to the {@link Remote} field.
                field.set(context.getBean(type),proxyInstance);
            }
        }
    }

    private String[] getMethodArgsReferenceNames(Method method){
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[]argRefNames=new String[parameterTypes.length];
        for(int i=0;i<parameterTypes.length;i++){
            argRefNames[i] = parameterTypes[i].getName();
        }
        return argRefNames;
    }
}
