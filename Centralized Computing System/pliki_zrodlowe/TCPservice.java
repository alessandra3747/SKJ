import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TCPservice implements Runnable {

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(CCS.port)) {

            while (!Thread.interrupted()){

                Socket socket = serverSocket.accept();

                threadPool.submit(new TCPClientHandler(socket));

                StatsReporter.incrementClients();
            }

        } catch (IOException e) {
            System.out.println("Error while server startup.\n" + e.getMessage());
        }
        finally {
            threadPool.shutdown();
        }
    }

}
