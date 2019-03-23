package rpc.woo.framework.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import rpc.woo.framework.server.configuration.WooServerProperties;

public class WooServer implements ApplicationRunner {
    public static ApplicationContext context=null;
    @Autowired
    WooServerProperties wooServerProperties;

    public WooServer(ApplicationContext applicationContext){
        this.context=applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WooServerChannelInitializer())
                    .bind(wooServerProperties.getPort()==null?8899:wooServerProperties.getPort())
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
