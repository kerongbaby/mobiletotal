package pygmy.core;

import java.util.Hashtable;

public abstract class Request {

    private Hashtable requestProperties;

    public Request(Hashtable serverConfig) {
        this.requestProperties = new ChainableProperties( serverConfig );
    }

    public String getProperty(String key, String defaultValue ) {
    	String value = (String)requestProperties.get( key);
    	if( value == null ) return defaultValue;
    	return value;
    }

    public void putProperty(String key, String value) {
        requestProperties.put( key, value );
    }
}
