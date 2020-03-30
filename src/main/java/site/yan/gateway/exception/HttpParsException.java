package site.yan.gateway.exception;

/**
 * @author zhao xubin
 * @date 2020-3-28
 */
public class HttpParsException extends RuntimeException {

    public HttpParsException() {
        super();
    }

    public HttpParsException(String reason) {
        super(reason);
    }
}

