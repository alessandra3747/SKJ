import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPHandler {
    private Socket socket;

    public TCPHandler(Socket socket) {
        this.socket = socket;
    }


    public String getMessage() {
        byte[] buffer = new byte[1];
        StringBuilder sb = new StringBuilder();
        try {
            InputStream stream = socket.getInputStream();
            while (stream.read(buffer, 0 ,1) != -1) {
                String c = new String(buffer, 0, 1);
                sb.append(c);
                if(c.equals("\n"))
                    break;
            }
        } catch (IOException ignored) {}
        return sb.toString();
    }

    public void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        try {
            OutputStream stream = socket.getOutputStream();
            stream.write(buffer);
            stream.flush();
        } catch (IOException ignored) {}
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
