package pygmy.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnection implements Connection {

    Socket sock;

    InputStream in;
    OutputStream out;

    public SocketConnection(Socket client) throws IOException {
        sock = client;

        in = sock.getInputStream();
        out = sock.getOutputStream();
    }

    public InputStream getInputStream() {
        return in;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public void close() throws IOException {
        out.flush();
        sock.close();
    }
}
