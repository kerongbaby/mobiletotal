package pygmy.core;

import java.io.IOException;

public class HttpProtocolException extends IOException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6732542087142929218L;
	int statusCode;

    public HttpProtocolException( int statusCode, String message ) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusPhrase() {
        return Http.getStatusPhrase( statusCode );
    }

    public String toString() {
        return getClass().getName() + ": " + statusCode + " " + getMessage();
    }
}
