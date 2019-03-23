package rpc.woo.framework.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import rpc.woo.framework.annotation.Caller;
import rpc.woo.framework.annotation.Remote;
import java.util.Arrays;

public class WooSpringApplicationRunListener implements SpringApplicationRunListener {

    private Logger logger= LoggerFactory.getLogger(WooSpringApplicationRunListener.class);

    public WooSpringApplicationRunListener(SpringApplication application, String[] args){

    }
    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        Arrays.stream(context.getBeanNamesForAnnotation(Caller.class)).forEach(name->{
            try {
                Class<?> type = Class.forName(context.getBean(name).getClass().getTypeName());
                Arrays.stream(type.getDeclaredFields()).forEach(field -> {
                    if(field.getDeclaredAnnotation(Remote.class)!=null){
                        try{
                             RpcProxyFactory.newInstance().createRpcProxy(type,context);
                        }catch (Exception e){
                            logger.error(e.toString());
                        }
                    }
                });
            } catch (ClassNotFoundException e) {
                logger.error(e.toString());
            }
        });
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
