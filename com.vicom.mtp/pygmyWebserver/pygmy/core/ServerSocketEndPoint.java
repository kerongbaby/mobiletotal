package pygmy.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
//import org.apache.log4j.Logger;
import javax.net.ServerSocketFactory;

public class ServerSocketEndPoint implements EndPoint, Runnable {

    protected ServerSocketFactory factory;
    protected ServerSocket socket;
    protected Server server;
    protected String endpointName;

    public ServerSocketEndPoint() {
        factory = ServerSocketFactory.getDefault();
    }

    public void initialize( String name, Server server ) throws IOException {
        this.endpointName = name;
        this.server = server;
    }

    public String getName() {
        return endpointName;
    }

    public void start() {
        try {
        	int port = Integer.parseInt( server.getHandlerProperty( endpointName, "port"));
            this.socket = factory.createServerSocket( port ) ;
            Thread thread = new Thread( this, endpointName + "[" + port + "] ServerSocketEndPoint" );
            thread.start();
        } catch (IOException e) {
            //log.debug("IOException ignored", e );
        } catch (NumberFormatException e) {
            //log.debug( "NumberFormatException ignored", e );
        }
    }

    public void run() {
        try {
            int connections = 0;
            while( true ) {
                Socket client = socket.accept();
                Hashtable config = new ChainableProperties( server.getConfig() );
                connections++;
                Runnable runnable = createRunnable(client, config);
                server.post( runnable );
            }
        } catch (IOException e) {
            //log.debug( "IOException ignored", e );
        }
    }

    protected String getProtocol() {
        return "http";
    }

    protected Runnable createRunnable(Socket client, Hashtable config) throws IOException {
        ConnectionRunnable runnable = new ConnectionRunnable( server, new SocketConnection( client ), config );
        return runnable;
    }

    public void shutdown(Server server) {
        if( socket != null ) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
