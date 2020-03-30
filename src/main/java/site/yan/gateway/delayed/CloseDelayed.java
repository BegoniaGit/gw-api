package site.yan.gateway.delayed;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;

/**
 * The {@code ChannelCloseDelayed} class is for delayed close channel
 * <p>Because JMeter test tool can't even receive all requests</p>
 *
 * @author zhao xubin
 * @date 2020-2-28
 */
public class CloseDelayed {

    private static DelayQueue<CloseDelayedModel> channelCloseQueue = new DelayQueue();

    public static DelayQueue<CloseDelayedModel> start(Executor executor) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        CloseDelayedModel closeDelayedModel = channelCloseQueue.take();
                        Channel channel = closeDelayedModel.getSocketChannel();
                        channel.close();
                    } catch (InterruptedException exc1) {
                        exc1.printStackTrace();
                    } catch (IOException exc2) {
                        exc2.printStackTrace();
                    }
                }
            }
        });
        return channelCloseQueue;
    }
}
