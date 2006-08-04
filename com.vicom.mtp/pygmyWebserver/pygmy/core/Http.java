package pygmy.core;

import java.util.Hashtable;
//import java.text.SimpleDateFormat;
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.io.IOException;
//import java.io.FilePermission;
//import java.net.*;

/**
 * Utility class for HTTP error codes, and other command HTTP tasks.
 */
public class Http {

    private static Hashtable htmlCharacterEncodings = new Hashtable();

    public static final byte[] CRLF = {(byte)'\r',(byte)'\n'};
    public static final byte[] ChunkeEnd = {(byte)'0',(byte)'\r',(byte)'\n',(byte)'\r',(byte)'\n'};

    static {
        htmlCharacterEncodings.put( "&", "amp" );
        htmlCharacterEncodings.put( "<", "lt" );
        htmlCharacterEncodings.put( ">", "gt" );
        htmlCharacterEncodings.put( "\"", "quot" );
        htmlCharacterEncodings.put( "'", "apos" );
    }

    /**
     * Given a HTTP response code, this method will return the english phrase.
     *
     * @param code the HTTP response code to look up.
     * @return A string describing what the HTTP response code is.
     */
    public static String getStatusPhrase(int code) {
    	switch(code){
	    	case	100:	return "Continue";
	    	case	101:	return "Switching Protocols";
	        case	200:	return "OK";
	        case	201:	return "Created";
	        case	202:	return "Accepted";
	        case	203:	return "Non-Authoritative Information";
	        case	204:	return "No Content";
	        case	205:	return "Reset Content";
	        case	206:	return "Partial Content";
	        case	300:	return "Multiple Choices";
	        case	301:	return "Moved Permanently";
	        case	302:	return "Moved Temporarily";
	        case	303:	return "See Other";
	        case	304:	return "Not Modified";
	        case	305:	return "Use Proxy";
	        case	400:	return "Bad Request";
	        case	401:	return "访问未经授权";
	        case	402:	return "Payment Required";
	        case	403:	return "Forbidden";
	        case	404:	return "没有找到";
	        case	405:	return "Method Not Allowed";
	        case	406:	return "Not Acceptable";
	        case	407:	return "Proxy Authentication Required";
	        case	408:	return "Request Time-out";
	        case	409:	return "Conflict";
	        case	410:	return "Gone";
	        case	411:	return "Length Required";
	        case	412:	return "Precondition Failed";
	        case	413:	return "Request Entity Too Large";
	        case	414:	return "Request-URI Too Large";
	        case	415:	return "Unsupported Media Type";
	        case	500:	return "Server Error";
	        case	501:	return "Not Implemented";
	        case	502:	return "Bad Gateway";
	        case	503:	return "Service Unavailable";
	        case	504:	return "Gateway Time-out";
	        case	505:	return "HTTP Version not supported";
	        default:		return "Error";
    	}
    }

    /**
     * This encodes the message into Html.  It encodes characters '&', ''', '<', '>', and '"' into &amp;, &apos;,
     * &lt;, &gt;, and &quot;.
     *
     * @param message the message to encode.
     * @return the encoded message.
     */
    public static String encodeHtml( String message ) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < message.length(); i++) {
        	String tmp = String.valueOf( message.charAt(i) );
            if ( htmlCharacterEncodings.containsKey( tmp ) ) {
                result.append( "&" );
                result.append( htmlCharacterEncodings.get( tmp ) );
                result.append( ";" );
            } else {
                result.append( tmp );
            }
        }
        return result.toString();
    }

    /**
     * This creates the current time as a string conforming to a HTTP date following the format
     * <i>EEE, dd MMM yyyy HH:mm:ss z</i>.
     * @return The a HTTP formated string of the current time.
     */
    /*
    public static String getCurrentTime() {
        return formatTime( System.currentTimeMillis() );
    }
    */
    /**
     * Takes two paths a joins them together properly adding the '/' charater between them.
     *
     * @param path the first part of the path.
     * @param relativePath the second part to join to the first path.
     * @return the combined path of path and relativePath.
     */
    public static String join( String path, String relativePath ) {
        boolean pathEnds = path.endsWith("/");
        boolean relativeStarts = relativePath.startsWith("/");
        if( (pathEnds && !relativeStarts) || (!relativeStarts && pathEnds) ) {
            return path + relativePath;
        } else if( pathEnds && relativeStarts ) {
            return path + relativePath.substring( 1 );
        } else {
            return path + "/" + relativePath;
        }
    }

    /**
     * This translates a url into a path on the filesystem.  This decodes the url using the URLDecoder class, and
     * creates an absolute path beginning with root.
     *
     * @param root this is an absolute path to prepend onto the url.
     * @param url the url to resolve into the filesystem.
     * @return the a File object representing this URL.
     * @throws UnsupportedEncodingException
     */
    /*
    public static File translatePath( String root, String url ) throws UnsupportedEncodingException {
        String name = URLDecoder.decode(url, "UTF-8");
        name = name.replace( '/', File.separatorChar );
        File file = new File( root, name );
        return file;
    }
    */
    /**
     * This method tries to find a suitable InetAddress that is routable.  It calls {@link java.net.InetAddress#getLocalHost}
     * to find the local host.  If that address it a site local address (192.168.*.*, 10.*.*.*, or 172.16.*.*) or the
     * loopback address (127.0.0.1), it enumerates all the NetworkInterfaces, and tries to find an address that is
     * not site local or loopback address.  If it cannot it simply returns whatever {@link InetAddress#getLocalHost}.
     * @return the address of a non site local or non loopback address, unless there is only loopback or site local addresses.
     * @throws UnknownHostException
     * @throws SocketException
     */
    /*
    public static InetAddress findRoutableHostAddress() throws UnknownHostException, SocketException {
        InetAddress localAddress = InetAddress.getLocalHost();
        if( localAddress.isSiteLocalAddress() || localAddress.isLoopbackAddress() ) {
            for( Enumeration networkEnum = NetworkInterface.getNetworkInterfaces(); networkEnum.hasMoreElements(); ) {
                NetworkInterface netInterface = (NetworkInterface)networkEnum.nextElement();
                for( Enumeration inetAddressEnum = netInterface.getInetAddresses(); inetAddressEnum.hasMoreElements(); ) {
                    InetAddress address = (InetAddress)inetAddressEnum.nextElement();
                    if( !address.isSiteLocalAddress() && !address.isLoopbackAddress() ) {
                        return address;
                    }
                }
            }
        }
        return localAddress;
    }
    */
/*
    public static List findAllHostAddresses( boolean includeLoopback ) throws SocketException {
        List addresses = new ArrayList();
        for( Enumeration networkEnum = NetworkInterface.getNetworkInterfaces(); networkEnum.hasMoreElements(); ) {
            NetworkInterface netInterface = (NetworkInterface)networkEnum.nextElement();
            for( Enumeration inetAddressEnum = netInterface.getInetAddresses(); inetAddressEnum.hasMoreElements(); ) {
                InetAddress address = (InetAddress)inetAddressEnum.nextElement();
                if( includeLoopback || !address.isLoopbackAddress() ) {
                    addresses.add( address );
                }
            }
        }
        return addresses;
    }
*/    
}
