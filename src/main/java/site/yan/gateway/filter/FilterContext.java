package site.yan.gateway.filter;

import java.nio.channels.SocketChannel;

/**
 * @author zhao xubin
 * @date 2020-3-28
 */
public class FilterContext {

    private SocketChannel socketChannel;

    public FilterContext(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
