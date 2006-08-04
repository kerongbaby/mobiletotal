package pygmy.handlers;

import pygmy.core.*;

import java.io.IOException;
//import org.apache.log4j.Logger;
import javax.microedition.io.HttpConnection;
import com.risetek.webhandle.webHandler;
/**
 */
public class RisetekHandler extends AbstractHandler implements Handler {
    //private static final Logger log = Logger.getInstance( RisetekHandler.class);
    private	String prefix;
    public boolean initialize(String handlerName, Server server) {
        super.initialize( handlerName, server );
        prefix = getUrlPrefix();
        return true;
    }

    protected boolean handleBody(HttpRequest request, HttpResponse response) throws IOException {
        String resource = request.getUrl();
        if( resource.startsWith(prefix)){
        	resource = resource.substring(prefix.length());
        	try{
        		webHandler webhandler = (webHandler)(Class.forName("com.risetek.webhandle."+resource)).newInstance();
                ////log.info( "Execute..");
                //log.info( "Execute.." + resource);
        		
                response.sendResponse( HttpConnection.HTTP_OK, "text/html", webhandler.handle(request,response) );
                return true;
          }catch (IllegalAccessException e) {
        		////log.error("Could not access constructor.", e );
        	}catch (InstantiationException e) {
        		////log.error("Could not instantiate object.",e);
        	}catch (ClassNotFoundException e) {
        	}
            response.sendResponse( HttpConnection.HTTP_BAD_REQUEST, "text/html", "Not handled!" );
            return true;
        }
        return false;
    }

}
