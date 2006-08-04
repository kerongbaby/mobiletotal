package pygmy.core;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.OutputStream;

public class InternetOutputStream extends DataOutputStream {

    public InternetOutputStream(OutputStream out) {
        super(out);
    }

    public void print( String buffer ) throws IOException {
        print( buffer, 0, buffer.length() );
    }

    public void println() throws IOException {
        write( Http.CRLF );
    }

    public void print( String text, int offset, int len ) throws IOException {
        write( text.getBytes(), offset, len );
    }

    public void println( String text ) throws IOException {
        print( text );
        println();
    }
    /*
    public void print( int i ) throws IOException {
        print( String.valueOf( i ) );
    }
    */
    /*
    public void println( int i ) throws IOException {
        print( i );
        println();
    }
    */
}
