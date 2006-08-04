package pygmy.core;

import java.io.EOFException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.io.HttpConnection;

public class ConnectionRunnable implements Runnable {
    protected Server server;
    protected Connection connection;
    protected Hashtable config;
    protected static Timer timer;
    protected ConnectionMonitor monitor;
    static{
    	timer = new Timer();
    }
    public ConnectionRunnable(Server aServer, Connection aConnection, Hashtable aConnectionConfig ) {
        this.server = aServer;
        this.connection = aConnection;
        this.config = aConnectionConfig;
    }

    public void run() {
        try {
        	/*在这里把socket超时打开*/
//        	connection.setSoTimeout(10000);
        	monitor = new ConnectionMonitor();
        	timer.schedule(monitor, 40000);
            HttpRequest request = createRequest();
            HttpResponse response = new HttpResponse( request, server.getResponseListeners() );
            if( !server.post( request, response ) ) {
            	response.sendError( HttpConnection.HTTP_NOT_FOUND, " 未在服务器中找到" );
            }
            monitor.cancel();
        } catch( EOFException eof ) {
        	monitor.cancel();
        } catch( IOException e ) {
        	if(monitor.isTimeout()){
        			System.out.println("Time out");
        		}else{
        			monitor.cancel();
        		}
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
            }
        }
    }

    protected HttpRequest createRequest() throws IOException {
        return new HttpRequest( config, connection );
    }
    
    private class ConnectionMonitor extends TimerTask {

    	private boolean timeout = false;
		public void run() {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public boolean isTimeout(){
			return timeout;
		}
    }
}
