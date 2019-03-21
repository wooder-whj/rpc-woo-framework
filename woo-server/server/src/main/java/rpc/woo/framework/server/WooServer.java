package rpc.woo.framework.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import rpc.woo.framework.common.RpcContext;
import rpc.woo.framework.server.configuration.WooServerProperties;

public class WooServer implements ApplicationRunner {
    static RpcContext context=null;
    @Autowired
    WooServerProperties wooServerProperties;

    private void initialContext() {
        context = WooServerRpcContext.getServerRpcContext();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initialContext();
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
