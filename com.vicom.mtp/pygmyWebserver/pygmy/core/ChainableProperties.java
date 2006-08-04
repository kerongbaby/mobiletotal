package pygmy.core;

import java.util.Hashtable;

public class ChainableProperties extends Hashtable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2065522878164465205L;
	Hashtable parent;

    public ChainableProperties() {
        parent = null;
    }

    public ChainableProperties( Hashtable parent ) {
        this.parent = parent;
    }
    public synchronized Object get(Object key) {
        Object value = super.get( key );
        if( value == null && parent != null ) {
            value = parent.get( key );
        }
        return value;
    }

    public synchronized boolean containsKey(Object key) {
        boolean contains = super.containsKey( key );
        if( !contains && parent != null ) {
            contains = parent.containsKey( key );
        }
        return contains;
    }

}
