package rpc.woo.framework.client;

import rpc.woo.framework.common.ProtocolBean;

public interface RpcClient<P> {
     void send(P param);
     P receive() throws InterruptedException;
     void close();
}
