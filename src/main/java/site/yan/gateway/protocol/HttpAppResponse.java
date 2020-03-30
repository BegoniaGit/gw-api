package site.yan.gateway.protocol;

import site.yan.gateway.constant.ContentType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhao xubin
 * @date 2020-3-28
 */
public class HttpAppResponse implements HttpResponse {

    private String protocol;

    private int statusCode;

    private String msg;

    private ContentType contentType;

    private String content;

    private Map<String, Object> headers;

    private static final String PROTOCOL = "HTTP/1.1";
    private static final String STRING_OK = "OK";
    private static final int CODE_OK = 200;

    private void init() {
        this.protocol = PROTOCOL;
        this.statusCode = CODE_OK;
        this.msg = STRING_OK;
        this.headers = new HashMap(16);
    }

    public HttpAppResponse() {
        init();
    }

    public HttpAppResponse(String content, ContentType type) {
        init();
        this.content = content;
        this.contentType = type;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
        this.addHeader("Content-Type", contentType.getValue() + "; charset=UTF-8");
    }

    public void setContent(String content) {
        this.content = content;
        this.addHeader("Content-Length", content.length());
    }

    @Override
    public void addHeader(String name, Object value) {
        this.headers.put(name, value);
    }

    @Override
    public byte[] createBytes() {
        StringBuilder respStr = new StringBuilder();
        respStr.append(String.format("%s %d %s\r\n", PROTOCOL, CODE_OK, STRING_OK));

        if (this.headers.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry header = iterator.next();
                respStr.append(header.getKey() + ": " + header.getValue() + "\r\n");
            }
        }
        if (Objects.nonNull(this.content)) {
            respStr.append(String.format("\r\n%s", this.content));
        }
        return respStr.append("\0").toString().getBytes();
    }
}
