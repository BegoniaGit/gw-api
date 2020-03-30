package site.yan.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.yan.gateway.delayed.CloseDelayed;
import site.yan.gateway.delayed.CloseDelayedModel;
import site.yan.gateway.filter.FileIOProcessFilter;
import site.yan.gateway.server.HttpServer;

import java.util.concurrent.*;

/**
 * The {@code Boot} class is a entrance.
 *
 * @author zhao xubin
 * @date 2020-3-28
 */
public class Boot {
    private ExecutorService executor = new ThreadPoolExecutor(2, 2,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(200000),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("http-server-thread");
                return thread;
            });
    private static Logger logger = LoggerFactory.getLogger(Boot.class);

    public static void main(String[] args) throws Exception {
        logger.info("Http Server boot...");

        Boot boot = new Boot();

        // ChannelCloseDelayed start at first must.
        DelayQueue<CloseDelayedModel> channelCloseQueue = CloseDelayed.start(boot.executor);
        HttpServer httpServer = new HttpServer(boot.executor, channelCloseQueue);

        // Full implementation of HttpProcessFilter can be added to handle more personalized calculations.
        httpServer.addProcess(new FileIOProcessFilter());

        // Start server.
        httpServer.start();
    }
}
