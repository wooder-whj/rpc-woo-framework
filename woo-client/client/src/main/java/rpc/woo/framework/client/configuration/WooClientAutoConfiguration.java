package rpc.woo.framework.client.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rpc.woo.framework.client.WooClientRpcContext;
import rpc.woo.framework.client.WooRpcClient;
import rpc.woo.framework.common.RpcContext;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WooClientProperties.class)
public class WooClientAutoConfiguration {
    @Autowired
    private WooClientProperties wooClientProperties;
    @Bean
    public WooRpcClient wooRpcClient(){
        return new WooRpcClient(wooClientProperties);
    }

//    @Bean
//    public WooClientRpcContext wooClientRpcContext(){
//        return (WooClientRpcContext)WooClientRpcContext.getClientRpcContext();
//    }
}
