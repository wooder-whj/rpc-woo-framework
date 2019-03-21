package rpc.woo.framework.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.woo.framework.annotation.Client;
import rpc.woo.framework.client.configuration.WooClientProperties;
import rpc.woo.framework.common.ProtocolBean;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Client
public class WooRpcClient implements RpcClient<ProtocolBean> {
    Logger logger=LoggerFactory.getLogger(WooRpcClient.class);

    WooClientProperties wooClientProperties;

    private EventLoopGroup group=new NioEventLoopGroup();
    private Bootstrap bootstrap=null;
    private Channel channel=null;
    private volatile ProtocolBean bean=null;
    private final Lock lock=new ReentrantLock();
    private Condition received =lock.newCondition();

    public WooRpcClient(WooClientProperties wooClientProperties){
        this.wooClientProperties=wooClientProperties;
        initialClient();
    }

    private void initialClient() {
         bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new NettyRpcChannelInitializer());
         channel=connect();
    }

    private Channel connect(){
        String host = wooClientProperties.getHost();
        host=host==null?"localhost":host;
        Integer port=wooClientProperties.getPort();
        port=port==null?8899:port;
        return bootstrap.connect(host, port).channel();
    }
    @Override
    public void send(ProtocolBean param){
        if(!this.channel.isOpen()){
            this.channel.close();
            this.channel=connect();
        }
        if(this.channel.isActive()){
            this.bean=null;
            this.channel.writeAndFlush(JSON.toJSONString(param));
        }
    }

    @Override
    public ProtocolBean receive() throws InterruptedException {
        ProtocolBean retBean=null;
        lock.lock();
        try{
            if (bean==null) received.await(5, TimeUnit.SECONDS);
            retBean=bean;
            bean=null;
            return retBean;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void close(){
        if(group!=null){
            group.shutdownGracefully();
        }
    }

    private class NettyRpcChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(4096,0,4096));
            pipeline.addLast("jsonObjectDecoder",new JsonObjectDecoder());
            pipeline.addLast("stringDecoder",new StringDecoder(CharsetUtil.UTF_8));
            pipeline.addLast("stringEncoder",new StringEncoder(CharsetUtil.UTF_8));
            pipeline.addLast("nettyRpcChannelHandler",new NettyRpcChannelHandler());
        }
    }

    private class NettyRpcChannelHandler extends ChannelDuplexHandler {
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.info(cause.getMessage());
            ctx.close();
        }
      @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            lock.lock();
            try{
                if(!StringUtil.isNullOrEmpty((String)msg)){
                    bean = JSON.parseObject((String)msg, ProtocolBean.class);
                    received.signal();
                }
            }finally {
                lock.unlock();
            }
        }
    }
}
