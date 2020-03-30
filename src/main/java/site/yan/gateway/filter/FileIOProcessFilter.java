package site.yan.gateway.filter;

import site.yan.gateway.constant.ContentType;
import site.yan.gateway.protocol.HttpAppResponse;
import site.yan.gateway.protocol.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * @author zhao xubin
 * @date 2020-3-28
 */
public class FileIOProcessFilter implements HttpProcessFilter {

    private Random random = new Random();

    @Override
    public void preProcess(HttpRequest httpRequest, site.yan.gateway.protocol.HttpResponse httpResponse, FilterContext filterContext) {
        final long timeStamp = System.currentTimeMillis();
        String path = httpRequest.getPath();

        // Rename the file, so multiple requests for the same uri will create the file.
        String fileName = path + (timeStamp & random.nextInt());
        String filePath = "." + fileName;
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException exc1) {
            exc1.printStackTrace();
        }
        HttpAppResponse httpAppResponse = (HttpAppResponse) httpResponse;
        httpAppResponse.setContentType(ContentType.JSON);

        // To return information faster, use simple string concatenation。
        httpAppResponse.setContent("{\"code\":0,\"data\":{\"fileName\":\"" + fileName + "\",\"timeStamp\":" + System.currentTimeMillis() + "}}");
        httpAppResponse.addHeader("Connection", "close");
        ByteBuffer byteBuffer = ByteBuffer.wrap(httpAppResponse.createBytes());
        if (byteBuffer.hasRemaining()) {
            try {
                filterContext.getSocketChannel().write(byteBuffer);
            } catch (IOException exc1) {
                exc1.printStackTrace();
            }
        }
    }

    @Override
    public void postProcess(HttpRequest httpRequest, site.yan.gateway.protocol.HttpResponse httpResponse, FilterContext filterContext) {
    }
}
