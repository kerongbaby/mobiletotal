package pygmy.core;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Server {

    Hashtable config = new ChainableProperties();
    Hashtable endpoints = new Hashtable();
    Handler handler = null;
    ResponseListener responseListener = null;
    ThreadPool threadPool;

    private static final String CLAZZ = ".class";
    /**
     * This creates a server from commandline arguments.
     *
     * @param args an array of config parameters, the format of the list is a '-' followed by either 'config' or some
     * name of a parameter (i.e. http.port), and a space and a value for that property.  All -config will load a file
     * either from the filesystem or the class path if the file doesn't exist on the filesystem.
     */
    public Server() throws IOException {
        setDefaultProperty( config, "mime.html", "text/html" );
        setDefaultProperty( config, "mime.zip", "application/x-zip-compressed");
        setDefaultProperty( config, "mime.gif", "image/gif" );
        setDefaultProperty( config, "mime.jpeg", "image/jpeg" );
        setDefaultProperty( config, "mime.jpg", "image/jpeg" );
        setDefaultProperty( config, "mime.css", "text/css" );
        setDefaultProperty( config, "mime.txt", "text/plain" );
        setDefaultProperty( config, "mime.js",  "text/javascript" );
        setDefaultProperty( config, "http.port", "7776");
        
        setDefaultProperty( config, "handler", "chain" );
        setDefaultProperty( config, "chain.class", "pygmy.handlers.DefaultChainHandler" );
        setDefaultProperty( config, "chain.chain", "risetek,root" );

        // these are properties read by the ResourceHandler named 'root'
        setDefaultProperty( config, "root.class", "pygmy.handlers.ResourceHandler" );
        setDefaultProperty( config, "root.url-prefix", "/" );
        setDefaultProperty( config, "root.resourceMount", "/doc" );

        // these are properties read by the ResourceHandler named 'risetek'
        setDefaultProperty( config, "risetek.class", "pygmy.handlers.RisetekHandler" );
        setDefaultProperty( config, "risetek.url-prefix", "/risetek/" );
        setDefaultProperty( config, "risetek.resourceMount", "/web" );

        
        // sets a default endpoint for http
        setDefaultProperty( config, "endpoints", "http" );
        setDefaultProperty( config, "http.class", "pygmy.core.ServerSocketEndPoint");
    }
    /**
     * This method adds an {@link EndPoint} to this server.  It will be initialized once the {@link #start} method is called.
     * @param name The name of this EndPoint instance.
     * @param endpoint The instance of the endpoint to add.
     */
    public void addEndPoint( String name, EndPoint endpoint ) {
        endpoints.put( name, endpoint );
    }

    /**
     * Returns the property stored under the key.
     * @param key the configuration key to look up.
     * @return the value stored in the configuration at this key.
     */
    public String getProperty( String key ) {
        return (String)config.get( key );
    }

    public String getHandlerProperty( String handlerName, String key ) {
        return getHandlerProperty( handlerName, key, null );
    }

    public String getHandlerProperty( String handlerName, String key, String defaultValue ) {
        return getProperty( handlerName + "." + key, defaultValue );
    }

    /**
     * Returns the property stored under the key.  If there isn't a property called key, then it returns the defaultValue.
     * @param key the configuration key to look up.
     * @param defaultValue the defaultValue returned if nothing is found under the key.
     * @return the value stored in the configuration at this key.
     */
    public String getProperty(String key, String defaultValue) {
    	String val = (String)config.get(key);
    	if (val == null) return defaultValue;
    	return val;
    }

    /**
     * Returns the configuration for the server.
     * @return the configuration for the server.
     */
    public Hashtable getConfig() {
        return config;
    }

    public Object getRegisteredComponent( Class clazz ) {
        return config.get( clazz );
    }

    /**
     * This method is called to start the web server.  It will initialize the server's Handler and all the EndPoints
     * then call the {@link EndPoint#start} on each EndPoint.  This method will return after the above steps are
     * done.
     */
    public void start() {
        initializeThreads();
        initializeHandler();
        if ( handler == null ) {
            return;
        }

        constructEndPoints();

        Enumeration values = endpoints.elements();
        
        while( values.hasMoreElements()){
            EndPoint currentEndPoint = (EndPoint) values.nextElement();
            currentEndPoint.start();
        }
    }

    private void initializeThreads() {
        threadPool = new ThreadPool( 30 );
    }

    protected void initializeHandler() {
        if( handler == null ) {
            handler = (Handler)constructPygmyObject( getProperty("handler") );
        }
        handler.initialize( getProperty("handler"), this );
    }

    /**
     * This is the method used to construct pygmy objects.  Given the object name it appends .class onto the end and
     * looks for the classname in the server's configuration.  It then analyzes the class's constructor parameters for
     * objects that it depends on.  Then it looks those objects up by class in the registered object pool.  Finally, it
     * calls the constructor using reflection passing any registered objects as arguments.  It returns
     * the newly constructed object or null if there was a problem.
     *
     * @param objectName the name of the object defined in the server's configuration.
     * @return the newly constructed object, or null there was a problem instantiated the object.
     */
    public Object constructPygmyObject( String objectName ) {
        Object theObject = null;
        String objectClassname = getProperty( objectName + CLAZZ );
        try {
            if( objectClassname == null ) throw new ClassNotFoundException( objectName + CLAZZ + " configuration property not found." );
            Class handlerClass = Class.forName( objectClassname );
            theObject = handlerClass.newInstance();
        }
        catch (IllegalAccessException e) {
        }catch (InstantiationException e) {
        }catch (ClassNotFoundException e) {
        }
        return theObject;
    }

    private void constructEndPoints() {
        String val = getProperty("endpoints");
        if( val != null ) {
            StringTokenizer tokenizer = new StringTokenizer( val );
            while( tokenizer.hasMoreTokens() ) {
                String endPointName = tokenizer.nextToken();
                try {
                    EndPoint endPoint = (EndPoint)constructPygmyObject( endPointName );
                    endPoint.initialize( endPointName, this );
                    addEndPoint( endPointName, endPoint );
                } catch( IOException e ) {
                }
            }
        }
    }

    /**
     * This method will shutdown the Handler, and call {@link EndPoint#shutdown} on each EndPoint.
     */
    public void shutdown() {
        threadPool.shutdown();
        if( handler != null ) {
            handler.shutdown( this );
        }
        Enumeration values = endpoints.elements();
        while( values.hasMoreElements()){
            EndPoint currentEndPoint = (EndPoint) values.nextElement();
            currentEndPoint.shutdown( this );
        }
    }

    /**
     * This method is used to post a {@link HttpRequest} to the server's handler.  It will create a HttpResponse
     * for the EndPoint to send to the client.
     * @param request
     * @return A HttpResponse that corresponds to the HttpRequest being handled.
     * @throws IOException
     */
    public boolean post( Request request, Response response ) throws IOException {
        return handler.handle( request, response );
    }

    /**
     * This method posts a Runnable onto the Server's task queue.  The server's {@link ThreadPool} will service the
     * runnable once a thread is freed up.
     * @param runnable An instance of Runnable that the user wishes to run on the server's {@link ThreadPool}.
     */
    public void post(Runnable runnable) {
        threadPool.execute( runnable );
    }

    /**
     * Returns the instance of the ResponseListener for this Server.
     *
     * @return the ResponseListener for this Server, or null if there is none.
     */
    public ResponseListener getResponseListeners() {
        return responseListener;
    }

    /**
     * This sets the ResponseListener for entire server.  All replys being sent to any client will
     * be notified to this instance.
     *
     * @param listener the instance of a ResponseListener to use for this Server.
     */
    public void setResponseListener( ResponseListener listener ) {
        this.responseListener = listener;
    }

    private static void setDefaultProperty( Hashtable props, String key, String value ) {
        if ( props.get( key ) == null ) {
            props.put( key, value );
        }
    }

}
