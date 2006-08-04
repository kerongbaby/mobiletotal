package pygmy.core;

import java.io.IOException;

/**
 * <p>
 * Objects that implement this interface handle the HttpRequests.  It processes
 * the request by returning true for the handler() method.
 * </p>
 */
public interface Handler {

    public boolean initialize( String handlerName, Server server );

    public String getName();

    public boolean handle( Request request, Response response ) throws IOException;

    public boolean shutdown( Server server );
}
