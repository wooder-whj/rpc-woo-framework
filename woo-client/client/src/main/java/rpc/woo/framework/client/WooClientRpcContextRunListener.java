package rpc.woo.framework.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import rpc.woo.framework.common.RpcContext;

public class WooClientRpcContextRunListener implements SpringApplicationRunListener {

    public WooClientRpcContextRunListener(SpringApplication application, String[] args){

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
        RpcContext rpcContext = WooClientRpcContext.getClientRpcContext(context);
        context.getBeanFactory().registerSingleton(RpcContext.class.getSimpleName(),rpcContext);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
