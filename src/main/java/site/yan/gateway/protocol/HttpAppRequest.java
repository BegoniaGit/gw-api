package site.yan.gateway.protocol;

import site.yan.gateway.exception.HttpParsException;

/**
 * The {@code HttpRequest} class is mainly used to
 * parse byte arrays into HTTP protocol objects.
 *
 * <p>For this requirement, in order to improve program performance,
 * information such as request headers and content are not parsed.</p>
 *
 * @author zhao xubin
 * @date 2020-3-28
 */
public class HttpAppRequest implements HttpRequest {

    private String method;

    private String path;

    private String protocol;

    private static final int ROW_MIN = 0XA;
    private static final String PARAM_ROW_DELIMITER = "\r\n";
    private static final String PARAM_PART_DELIMITER = " ";

    @Override
    public void parse(byte[] rawByte) {
        if (rawByte.length < 5) {
            throw new HttpParsException("An exception occurred in the length of the original http byte array");
        }
        String rowStr = new String(rawByte).split(PARAM_ROW_DELIMITER)[0];
        String[] params = rowStr.split(PARAM_PART_DELIMITER);
        if (params.length < 3) {
            throw new HttpParsException("An exception occurred in Parsing HTTP Basic Request Information");
        }
        this.method = params[0];
        this.path = params[1];
        this.protocol = params[2];
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }
}
