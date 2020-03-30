package site.yan.gateway.server;

import site.yan.gateway.delayed.CloseDelayedModel;
import site.yan.gateway.filter.FilterContext;
import site.yan.gateway.filter.HttpProcessFilter;
import site.yan.gateway.protocol.HttpAppRequest;
import site.yan.gateway.protocol.HttpAppResponse;
import site.yan.gateway.protocol.HttpRequest;
import site.yan.gateway.protocol.HttpResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.DelayQueue;

/**
 * @author zhao xubin
 * @date 2020-3-28
 */
public class ServerHandler implements Runnable {

    private ArrayList<SelectionKey> list;
    private Selector selector;

    private List<HttpProcessFilter> processFilters;
    private DelayQueue<CloseDelayedModel> channelCloseQueue;

    ByteBuffer myBuffer = ByteBuffer.allocate(1024);

    public ServerHandler(ArrayList<SelectionKey> list,
                         Selector selector,
                         List<HttpProcessFilter> processFilters,
                         DelayQueue<CloseDelayedModel> channelCloseQueue) {
        this.list = list;
        this.selector = selector;
        this.processFilters = processFilters;
        this.channelCloseQueue = channelCloseQueue;
    }

    @Override
    public void run() {

        try {
            for (SelectionKey selectionKey : list) {
                if (!selectionKey.isValid()) {
                    continue;
                } else if (selectionKey.isAcceptable()) {
                    acceptable(selectionKey);
                } else if (selectionKey.isReadable()) {
                    readable(selectionKey);
                }
            }
        } catch (Exception exc1) {
            exc1.printStackTrace();
        }
    }

    private void acceptable(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (Objects.nonNull(socketChannel)) {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    private void readable(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        myBuffer.clear();
        if (socketChannel.read(myBuffer) < 1) {
            selectionKey.channel().close();
            return;
        }
        myBuffer.flip();
        byte[] bufferTemp = new byte[myBuffer.limit()];
        myBuffer.get(bufferTemp);
        if (bufferTemp.length < 1) {
            return;
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        FilterContext filterContext = new FilterContext(socketChannel);
        HttpRequest httpAppRequest = new HttpAppRequest();
        HttpResponse httpAppResponse = new HttpAppResponse();
        httpAppRequest.parse(bufferTemp);
        for (HttpProcessFilter filter : processFilters) {
            filter.preProcess(httpAppRequest, httpAppResponse, filterContext);
        }
        Collections.reverse(processFilters);
        for (HttpProcessFilter filter : processFilters) {
            filter.postProcess(httpAppRequest, httpAppResponse, filterContext);
        }
        Collections.reverse(processFilters);
        socketChannel.shutdownOutput();
        channelCloseQueue.offer(new CloseDelayedModel(socketChannel));
    }
}
