package pygmy.handlers;

import pygmy.core.*;

import java.io.IOException;
import java.util.StringTokenizer;
//import org.apache.log4j.Logger;

import java.util.Vector;
import java.util.Enumeration;
/**
 * This is the default implementation of a chain of handlers.  The .chain parameter defines the names of the
 * handlers in the chain, and it defines the order in which those handlers will be called.  Each handler name is
 * seperated by either a ' ' (space) or a ',' (comma).  This handler will then try to create a handler for each of
 * the handler names by looking at configuration property {handler-name}.class.  This handler also has a .url-prefix
 * parameter it uses to know when this handler should pass the request to the chain.
 *
 * <table class="inner">
 * <tr class="header"><td>Parameter Name</td><td>Explanation</td><td>Default Value</td><td>Required</td></tr>
 * <tr class="row"><td>url-prefix</td><td>The prefix to filter request urls.</td><td>None</td><td>Yes</td></tr>
 * <tr class="altrow"><td>chain</td><td>A space or comma seperated list of the names of the handlers within the chain.</td><td>None</td><td>Yes</td></tr>
 * <tr class="row"><td>class</td><td>For each of the names in the chain property, this is appended the name to find the classname to instatiate.</td><td>None</td><td>Yes</td></tr>
 * </table>
 */
public class DefaultChainHandler extends AbstractHandler implements Handler {
    //private static final Logger log = Logger.getInstance( DefaultChainHandler.class);

    public static String CHAIN = ".chain";
    public static String CLAZZ = ".class";

    private Vector chain;

    public boolean initialize(String handlerName, Server server) {
        super.initialize( handlerName, server );
        this.chain = new Vector();
        initializeChain(server);
        return true;
    }

    private void initializeChain(Server server) {
        StringTokenizer tokenizer = new StringTokenizer( getHandlerProperty( CHAIN ), " ," );
        while( tokenizer.hasMoreTokens() ) {
            String chainChildName = tokenizer.nextToken();
            try {
                Handler handler = (Handler)server.constructPygmyObject( chainChildName );
                if( handler.initialize( chainChildName, server ) ) {
                    chain.addElement( handler );
                } else {
                    //log.debug( chainChildName + " was not initialized" );
                }
            } catch (ClassCastException e) {
                //log.debug( chainChildName + " class does not implement the Handler interface.", e );
            }
        }
    }

    public boolean handle(Request request, Response response) throws IOException {
        boolean hasBeenHandled = false;
        Enumeration i = chain.elements();
        while( i.hasMoreElements() && !hasBeenHandled){
            Handler handler = (Handler) i.nextElement();
            try{
            	hasBeenHandled = handler.handle(request, response );
            }catch(Exception e){
            	hasBeenHandled = true;
            }
        }
        return hasBeenHandled;
    }

    public boolean shutdown(Server server) {
        boolean success = true;
        if( chain != null ) {
            Enumeration i = chain.elements();
            while( i.hasMoreElements()){
                Handler current = (Handler)i.nextElement();
                boolean currentSuccess = current.shutdown( server );
                success = success && currentSuccess;
            }
        }
        return success;
    }
}
