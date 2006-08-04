package pygmy.core;

import java.io.IOException;
////import org.apache.log4j.Logger;
public abstract class AbstractHandler implements Handler {

    //private static final Logger log = Logger.getInstance(AbstractHandler.class);

    protected Server server;
    protected String handlerName;
    protected String urlPrefixKey;

    public static final String URL_PREFIX = "url-prefix";

    public AbstractHandler() {
        this( URL_PREFIX );
    }

    public AbstractHandler(String urlPrefixKey) {
        this.urlPrefixKey = urlPrefixKey;
    }

    public boolean initialize( String handlerName, Server server ) {
        this.server = server;
        this.handlerName = handlerName;
        return true;
    }

    public String getName() {
        return handlerName;
    }

    public boolean handle( Request aRequest, Response aResponse) throws IOException {
        if( aRequest instanceof HttpRequest ) {
            HttpRequest request = (HttpRequest) aRequest;
            HttpResponse response = (HttpResponse) aResponse;
            if( urlPrefixKey == null || request.getUrl().startsWith( getUrlPrefix() ) ) {
                return handleBody( request, response );
            }
            //log.info( "'" + request.getUrl() + "' does not start with prefix '" + getUrlPrefix() + "'" );
        }
        return false;
    }

    protected boolean handleBody(HttpRequest request, HttpResponse response) throws IOException {
        return false;
    }

    public boolean shutdown(Server server) {
        return true;
    }

    public String getUrlPrefix() {
        return getHandlerProperty( urlPrefixKey, "/" );
    }

    public String getHandlerProperty(String property) {
        return getHandlerProperty( property, null );
    }

    public String getHandlerProperty(String property, String defaultValue ) {
        if( property.charAt(0) != 46 ) {	// 46 is '.'
            return server.getProperty( handlerName + "." + property, defaultValue );
        } else {
            return server.getProperty( handlerName + property, defaultValue );
        }
    }

    protected String getMimeType( String filename ) {
        int index = filename.lastIndexOf(46);		// 46 is ×Ö·û:"."
        String mimeType = null;
        if( index > 0 ) {
            mimeType = server.getProperty( "mime" + filename.substring( index ).toLowerCase() );
        }
        return mimeType;
    }

}
