package pygmy.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ChunkedEncodingOutputStream extends DataOutputStream {

    static final int DEFAULT_CHUNK_SIZE = 4096;
    byte[] buf = null;
    int count = 0;

    public ChunkedEncodingOutputStream(OutputStream out, int maxChunkSize ) {
        super(out);
        this.buf = new byte[ maxChunkSize ];
    }

    public ChunkedEncodingOutputStream(OutputStream out) {
        this( out, DEFAULT_CHUNK_SIZE );
    }

    public void write(int b) throws IOException {
        if( count >= buf.length ) {
            flush();
        }
        buf[ count++ ] = (byte)b;
    }

    public void write(byte b[]) throws IOException {
        this.write( b, 0, b.length );
    }

    public void write(byte b[], int off, int len) throws IOException {
        for( int i = off; i < len; i++ ) {
            if( count < buf.length ) {
                buf[ count++ ] = b[i];
            } else {
                flush();
            }
        }
    }

    public void flush() throws IOException {
        writeChunkSize( count );
        writeChunkData( buf, count );
        count = 0;
        out.flush();
    }

    public void close() throws IOException {
        if( count > 0 ) {
            flush();
        }
        writeChunkEnding();
        out.close();
    }

    private void writeChunkSize(int count) throws IOException {
        if( count > 0 ) {
            out.write( (Integer.toHexString(count) ).getBytes() );
            out.write( Http.CRLF );
        }
    }

    private void writeChunkData(byte[] buf, int count) throws IOException {
        if( count > 0 ) {
            out.write( buf, 0, count );
            out.write( Http.CRLF );
        }
    }

    private void writeChunkEnding() throws IOException {
        out.write( Http.ChunkeEnd );
    }

}
