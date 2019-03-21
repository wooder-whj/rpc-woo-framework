package rpc.woo.framework.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.woo.framework.common.ProtocolBean;
import rpc.woo.framework.common.RpcContext;

import java.lang.reflect.Method;

public class WooServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private Logger logger= LoggerFactory.getLogger(WooServerChannelHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(StringUtil.isNullOrEmpty(msg)){
            return;
        }
        ProtocolBean bean = callService(msg, WooServer.context);
        String beanStr= JSON.toJSONString(bean);
        ctx.writeAndFlush(beanStr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.error(cause.getMessage());
    }

    private ProtocolBean callService(String msg, RpcContext context) throws Exception {
        if (StringUtil.isNullOrEmpty(msg)) {
            return null;
        }
        ProtocolBean bean = JSON.parseObject(msg, ProtocolBean.class);
        String[] argRefNames = bean.getArgRefNames();
        Class<?>[] argClasses = new Class<?>[argRefNames.length];
        for (int i = 0; i < argRefNames.length; i++) {
            argClasses[i] = Class.forName(argRefNames[i]);
        }
        Method declaredMethod = context
                .getBeanByRefName(bean.getInterfaceRefName())
                .getClass()
                .getDeclaredMethod(bean.getMethodName(), argClasses);
        Object invoke = declaredMethod.invoke(context.getBeanByRefName(bean.getInterfaceRefName()), bean.getArgs());
        ProtocolBean protocolBean = new ProtocolBean();
        protocolBean.setReturnObject(invoke);
        return protocolBean;
    }
}
