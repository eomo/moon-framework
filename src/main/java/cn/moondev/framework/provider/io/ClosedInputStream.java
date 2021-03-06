package cn.moondev.framework.provider.io;

import java.io.InputStream;

public class ClosedInputStream extends InputStream {

    public static final int EOF = -1;

    /**
     * A singleton.
     */
    public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @return always -1
     */
    @Override
    public int read() {
        return EOF;
    }

}
