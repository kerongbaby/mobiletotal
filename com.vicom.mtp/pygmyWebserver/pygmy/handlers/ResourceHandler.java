package pygmy.handlers;

import pygmy.core.*;

import java.io.IOException;
import java.io.InputStream;
//import org.apache.log4j.Logger;
import javax.microedition.io.HttpConnection;
/**
 * <p>
 * This serves up files from the class path.  Usually this is used to serve files from within a jar file that is
 * within the classpath of the application running.  However, you could serve up files from anywhere in the classpath,
 * no just jar files.  This makes it easy to package part of your web content inside a jar file for easy distribution,
 * but make it transparently available without the user needing to extract the content from the jar file.  This is
 * great for static or default content that you don't want users to have to manage.  By putting a FileHandler and a
 * ResourceHandler in a chain filtering on the same url-prefix, you can allow users to override or augment content
 * from the jar file by placing the same file on the file system, but load the default from the jar file if the file
 * not on the file system.
 * </p>
 *
 * <table class="inner">
 * <tr class="header"><td>Parameter Name</td><td>Explanation</td><td>Default Value</td><td>Required</td></tr>
 * <tr class="row"><td>url-prefix</td><td>The prefix to filter request urls.</td><td>None</td><td>Yes</td></tr>
 * <tr class="altrow"><td>resourceMount</td><td>A path within the classpath to the root of the folder to share.
 * The requested url minus the url-prefix will be added to this path to yield the path loaded from the classpath.
 * </td><td>None</td><td>Yes</td></tr>
 * <tr class="row"><td>default</td><td>The name of the default resource that should be used if no file is specified in the URL. ( like index.html )</td><td>index.html</td><td>No</td></tr>
 * </table>
 */
public class ResourceHandler extends AbstractHandler implements Handler {
    //private static final Logger log = Logger.getInstance( ResourceHandler.class);
    public static final String RESOURCE_MOUNT = ".resourceMount";
    public static final String DEFAULT_RESOURCE = ".default";

    private String resourceMount;
    private String defaultResource;

    public boolean initialize(String handlerName, Server server) {
        super.initialize( handlerName, server );
        this.resourceMount = getHandlerProperty( RESOURCE_MOUNT, "/" );
        this.defaultResource = getHandlerProperty( DEFAULT_RESOURCE, "index.html" );
        return true;
    }

    protected boolean handleBody(HttpRequest request, HttpResponse response) throws IOException {
        String resource = Http.join( resourceMount, request.getUrl().substring( getUrlPrefix().length() ) );
        if( resource.endsWith("/") ) {
            resource += defaultResource;
        } else if( resource.lastIndexOf(46) < 0 ) {	// 46 is '.'
            resource += "/" + defaultResource;
        }
        //log.info( "Loading resource: " + resource );
        String mimeType = getMimeType( resource );
        InputStream is = getClass().getResourceAsStream( resource );

        if( mimeType == null || is == null ) {
            //log.warn( "Resource was not found or the mime type was not understood. (Found file=" + (is != null) + ") (Found mime-type=" + ( mimeType != null ) + ")" );
            return false;
        }
        response.sendResponse( HttpConnection.HTTP_OK, mimeType, is, -1 );
        return true;
    }

}
