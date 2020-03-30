package site.yan.gateway.delayed;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author zhao xubin
 * @date 2020-3-28
 */
public class CloseDelayedModel implements Delayed {

    private SocketChannel socketChannel;

    private long executeTime;


    public CloseDelayedModel(SocketChannel socketChannel) {

        this.socketChannel = socketChannel;
        this.executeTime = TimeUnit.NANOSECONDS.convert(4000, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.executeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }


    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }
}
