package pygmy.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This holds the response data for the http response.
 */
public class HttpResponse extends Response {

    private HttpHeaders responseHeaders;

    private boolean headersSent;

    private InternetOutputStream stream;

    private HttpRequest request;

    private ResponseListener responseListener;

    private static final int SEND_BUFFER_SIZE = 4096;

    public HttpResponse(HttpRequest request) {
        this( request, null );
    }

    public HttpResponse( HttpRequest request, ResponseListener listener ) {
        this.stream = new InternetOutputStream( request.getConnection().getOutputStream() );
        this.request = request;
        this.headersSent = false;
        this.responseHeaders = new HttpHeaders();
        this.responseListener = listener;
    }

    public void addHeader(String key, String value) {
        responseHeaders.put( key, value );
    }

    public void sendError( int statusCode, String errorMessage ) {
        if (headersSent) {
            return;
        }

        String body = "<html>\n<head>\n"
                + "<title>错误: " + statusCode + "</title>\n"
                + "<body>\n<h1>" + statusCode + " <b>"
                + Http.getStatusPhrase( statusCode )
                + "</b></h1><br>\n访问的资源 <B>"
                + ((request.getUrl() == null) ? "<i>unknown URL</i>" : Http.encodeHtml( request.getUrl() ))
                + "</b>\n " + Http.encodeHtml( errorMessage )
                + "\n<hr>"
                + "</body>\n</html>";

        try {
            sendResponse( statusCode, "text/html", body );
        } catch (IOException e) {
        }
    }

    public void sendResponse( int code, String mimeType, String body  ) throws IOException {
        sendResponse( code, mimeType, body.getBytes("utf-8") );
    }

    public void sendResponse( int code, String mimeType, byte[] body  ) throws IOException {
        try {
        	sendHttpReply( code );
            sendHeaders( mimeType, body.length );
            if ( !isHeadMethod() ) {
                stream.write( body );
            }
        } finally {
        	stream.flush();
        }
    }

    public void sendResponse( int code, String mimeType, InputStream is, int length ) throws IOException {
        try {
            sendHttpReply( code );
            sendHeaders( mimeType, length );
            if( !isHeadMethod() ) {
                if( length >= 0 || request.isProtocolVersionLessThan( 1, 1 ) ) {
                    sendToStream( stream, is, length );
                } else {
                    sendToStream( new ChunkedEncodingOutputStream( stream ), is, length );
                }
            }
        } finally {
            is.close();
            stream.flush();
        }
    }

    public void sendResponse( int code, String mimeType, InputStream is, long beginning, long ending ) throws IOException {
        is.skip( beginning );
        sendResponse( code, mimeType, is, (int)(ending - beginning) );
    }

    private void sendHttpReply( int code ) throws IOException {
        StringBuffer buffer = new StringBuffer( request.getProtocol() );
        buffer.append(" ");
        buffer.append( code );
        buffer.append( " ");
        buffer.append( Http.getStatusPhrase( code ) );
        buffer.append( "\r\n");
        stream.write( buffer.toString().getBytes() );
    }

    private void sendHeaders(  String mimeType, int contentLength ) throws IOException {
    	responseHeaders.put( "Date", "2006" );
        responseHeaders.put( "Server", "risetek web" );
        responseHeaders.put( request.getConnectionHeader(), "close" );
        if ( contentLength >= 0) {
            responseHeaders.put( "Content-Length", Integer.toString(contentLength) );
        } else if( !request.isProtocolVersionLessThan( 1, 1 ) ) {
            responseHeaders.put("Transfer-Encoding", "chunked");
        }

        if (mimeType != null) {
            responseHeaders.put( "Content-Type", mimeType );
        }
        responseHeaders.print( stream );
    }

    private boolean isHeadMethod() {
        return "HEAD".equalsIgnoreCase( request.getMethod() );
    }

    protected void sendToStream( OutputStream os, InputStream is, int length ) throws IOException {
        byte[] buffer = new byte[ Math.min( SEND_BUFFER_SIZE, (length > 0 ? length : Integer.MAX_VALUE) ) ];
        int totalSent = 0;
        try {
            startTransfer();
            while( true ) {
                int bufLen = is.read( buffer );
                if( bufLen < 0 ) {
                    break;
                }
                notifyListeners( totalSent, length );
                os.write( buffer, 0, bufLen );
                totalSent += bufLen;
            }
            endTransfer();
        } catch( IOException e ) {
            endTransfer( e );
            throw e;
        } finally {
            os.flush();
            os.close();
        }
    }

    private void startTransfer() {
        if( responseListener != null ) {
            responseListener.startTransfer( request );
        }
    }

    private void notifyListeners(int bytesSent, int length) throws IOException {
        if( responseListener != null ) {
            responseListener.notify( request, bytesSent, length );
        }
    }

    private void endTransfer() {
        endTransfer( null );
    }

    private void endTransfer(Exception e) {
        if( responseListener != null ) {
            responseListener.endTransfer( request, e );
        }
    }
}
