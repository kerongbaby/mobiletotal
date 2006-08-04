package pygmy.core;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
public class HttpHeaders {
    private Hashtable map;

    public HttpHeaders() {
        this.map = new Hashtable();
    }

    /**
     * 对于HTTP报头的解析
     * @param stream
     * @throws IOException
     */
    public HttpHeaders( InternetInputStream stream ) throws IOException {
        this();
        String currentKey = null;
        while (true) {
            String line = stream.readline();
            if ((line == null) || (line.length() == 0)) {
                break;
            }

            char c = line.charAt(0);
            if( c != '\r' && c!= '\n' && c != '\t' && c !=' ' ){
                int index = line.indexOf(':');
                if (index >= 0) {
                    currentKey = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    put( currentKey, value );
                }
            } else if ( currentKey != null ) {
            	System.out.println("HttpHeaders find multiLine key values");
                String value = get( currentKey );
                put( currentKey, value + "\r\n\t" + line.trim() );
            }
        }
    }

    public String get( String key ) {
        return (String) map.get( key );
    }

    public String get( String key, String defaultValue ) {
        String value = get( key );
        return ( value == null ) ? defaultValue : value;
    }

    public void put( String key, String value ) {
        map.put( key, value );
    }

    public void clear() {
        map.clear();
    }

    public Enumeration enumeration(){
    	return map.keys();
    }

    public void print( InternetOutputStream stream ) throws IOException {
    	Enumeration i = enumeration();
    	while( i.hasMoreElements()){
            String key = (String) i.nextElement();
            stream.println( key + ": " + get( key ) );
    	}
        stream.println();
        stream.flush();
    }
}
