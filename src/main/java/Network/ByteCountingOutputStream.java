package main.java.Network;

import java.io.IOException;
import java.io.OutputStream;

public final class ByteCountingOutputStream extends OutputStream
{
    private long size;

    /**
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException
    {
        size++;
    }

    /**
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte b[], int off, int len) throws IOException
    {
        size += len;
    }

    /**
     * @return Number of bytes written to this stream
     */
    public long size()
    {
        return size;
    }
}
