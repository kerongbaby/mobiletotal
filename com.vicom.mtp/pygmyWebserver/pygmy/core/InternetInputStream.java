package pygmy.core;

import java.io.IOException;
import java.io.InputStream;

public class InternetInputStream extends InputStream {

	private int unreadChar = -1; 
	InputStream 	in;
	public InternetInputStream(InputStream in, int size) {
		this.in = in;
    }

    public InternetInputStream(InputStream in) {
		this.in = in;
    }
    public String readline() throws IOException {
        StringBuffer buf = readBuffer();
        if( buf == null ) return null;
        return buf.toString();
    }

    public int readlineLen(byte[] out, int maxLen) throws IOException 
    {
        int i =0;
        int ch = -1;
        while(maxLen >3 && ( ch = read() ) >= 0 ){
            out[i]=(byte)ch;
            i++;
            maxLen--;
            if( ch == '\r' ){
                ch = read();
                out[i]=(byte)ch;
                i++;
                maxLen--;
                if( ch > 0 && ch != '\n' ){
                    unread( ch );
                    i--;
                    maxLen++;
                }
                break;
            } else if( ch == '\n' ){
                break;
            }
        }
        return i;
    }

    private void unread(int ch){
    	if( unreadChar == -1)
    		unreadChar = ch;
    	else
    		// 	FIXME: 正常情况下,不应该出现多次unread的情况。
    		System.out.println("FIXME!!!, unread");
    }
    
    public int read() throws IOException{
    	int val;
    	if( unreadChar != -1){
    		val = unreadChar;
    		unreadChar = -1;
    	} else val = in.read();
    	return val;
    }
    
    public StringBuffer readBuffer() throws IOException {
        StringBuffer buffer = null;

        int ch = -1;
        while( ( ch = read() ) >= 0 ) {
            if( buffer == null ) {
                buffer = new StringBuffer();
            }
            if( ch == '\r' ) {
                ch = read();
                if( ch > 0 && ch != '\n' ) {
                    unread( ch );
                }
                break;
            } else if( ch == '\n' ) {
                break;
            }
            buffer.append( (char)ch );
        }
        return buffer;
    }

}
