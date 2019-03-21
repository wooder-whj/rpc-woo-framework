package rpc.woo.framework.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.woo.framework.annotation.Caller;
import rpc.woo.framework.annotation.Client;
import rpc.woo.framework.annotation.Reference;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRpcContext implements RpcContext {
    private Logger logger=LoggerFactory.getLogger(AbstractRpcContext.class);
    private Map<String,Object> map = new ConcurrentHashMap<>();
    private final String CLASS_PATH =ClassLoader.getSystemResource("").getPath();

    public AbstractRpcContext(){
        initial();
    }
    private void initial() {
        scan();
    }
    @Override
    public Object getBeanByRefName(String beanName) {
        return map.get(beanName);
    }

    @Override
    public Object getBeanByType(Class type) {
        return map.get(type.getName());
    }

    private void scan() {
        File file=new File(CLASS_PATH);
        try{
            if(file.isFile()){
                scanFile(file);
            }else if(file.isDirectory()){
                scanDirectory(file);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    private void scanFile(File file) throws Exception {
        if(!file.getName().contains(".class")){
            return;
        }
        String className=convertToClassName(file.getPath()).replace(".class","");
        Class<?> type=Class.forName(className);
        if(type.getDeclaredAnnotation(Caller.class)!=null) {
            Arrays.asList(type.getDeclaredFields()).iterator().forEachRemaining(field -> {
                storeRemoteProxy(type,field,map);
            });
            if(!map.containsKey(type.getName())){
                addInstance(type);
            }
        }else if(type.getDeclaredAnnotation(Reference.class)!=null){
            map.put(type.getInterfaces()[0].getTypeName(),type.newInstance());
        }else if(type.getDeclaredAnnotation(Client.class)!=null){
            addInstance(type);
        }
    }

    private String convertToClassName(String path){
        String classPath =CLASS_PATH.contains(":")
                ?CLASS_PATH.substring(ClassLoader.getSystemResource("").getPath().lastIndexOf(":")+1)
                :CLASS_PATH;
        classPath=classPath.replace("/","\\");
        path=path.contains(":")?path.substring(ClassLoader.getSystemResource("").getPath().lastIndexOf(":")):path;
        return path.replace(classPath,"").replace("\\",".");
    }
    public abstract void storeRemoteProxy(Class<?> type,Field field,Map<String,Object> map);

    private void addInstance(Class<?> type){
        try {
            if(!type.isInterface()){
                Object instance = type.newInstance();
                map.put(type.getName(),instance);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    private void scanDirectory(File file) {
        Arrays.asList(file.listFiles()).iterator().forEachRemaining(file1 -> {
            try {
                if(file1.isFile()){
                    scanFile(file1);
                }else{
                    scanDirectory(file1);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }
}
