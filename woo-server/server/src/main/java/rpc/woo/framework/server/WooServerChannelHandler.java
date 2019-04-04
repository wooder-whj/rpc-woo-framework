package rpc.woo.framework.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.woo.framework.common.ProtocolBean;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WooServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private Logger logger= LoggerFactory.getLogger(WooServerChannelHandler.class);
    private final ExecutorService executor= Executors.newCachedThreadPool();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(StringUtil.isNullOrEmpty(msg)){
            return;
        }
        executor.submit(()->{
            ProtocolBean bean = null;
            try {
                bean = callService(msg);
                String beanStr= JSON.toJSONString(bean);
                ctx.writeAndFlush(beanStr);
            } catch (Exception e) {
                logger.error(e.getMessage(),e );
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.error(cause.toString());
    }

    private ProtocolBean callService(String msg) throws Exception {
        if (StringUtil.isNullOrEmpty(msg)) {
            return null;
        }
        ProtocolBean bean = JSON.parseObject(msg, ProtocolBean.class);
        String[] argRefNames = bean.getArgRefNames();
        Class<?>[] argClasses = new Class<?>[argRefNames.length];
        for (int i = 0; i < argRefNames.length; i++) {
            argClasses[i] = Class.forName(argRefNames[i]);
        }
        Object service=WooServer.context.getBean(Class.forName(bean.getInterfaceRefName()));
        Method declaredMethod = Class.forName(bean.getInterfaceRefName()).getDeclaredMethod(bean.getMethodName(), argClasses);
        Object invoke = declaredMethod.invoke(service, bean.getArgs());
        ProtocolBean protocolBean = new ProtocolBean();
        protocolBean.setReturnObject(invoke);
        return protocolBean;
    }
}
