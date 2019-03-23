package rpc.woo.framework.client;

public interface RpcClient<P> {
     void send(P param);
     P receive() throws InterruptedException;
     void close();
}
