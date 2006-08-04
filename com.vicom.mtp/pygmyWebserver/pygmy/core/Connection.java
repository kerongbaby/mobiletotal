package pygmy.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Connection {

    public InputStream getInputStream();

    public OutputStream getOutputStream();

    public void close() throws IOException;

}