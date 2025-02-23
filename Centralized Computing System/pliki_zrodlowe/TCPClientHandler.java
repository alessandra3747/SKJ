import java.net.Socket;
import java.util.Arrays;

public class TCPClientHandler implements Runnable {

    TCPHandler handler;

    public TCPClientHandler(Socket socket) {
        this.handler = new TCPHandler(socket);
    }

    @Override
    public void run() {

        String[] message = handler.getMessage().trim().split(" ");

        //check format
        if (message.length != 3 || !isNumeric(message[1]) || !isNumeric(message[2])) {
            error();
            return;
        }

        Integer result = null;


        switch (message[0]) {
            case "ADD":
                result = Integer.parseInt(message[1]) + Integer.parseInt(message[2]);
                break;

            case "SUB":
                result = Integer.parseInt(message[1]) - Integer.parseInt(message[2]);
                break;

            case "MUL":
                result = Integer.parseInt(message[1]) * Integer.parseInt(message[2]);
                break;

            case "DIV":
                if (Integer.parseInt(message[2]) == 0) {
                    error();
                    return;
                }
                result = Integer.parseInt(message[1]) / Integer.parseInt(message[2]);
                break;
        }

        handler.sendMessage(String.valueOf(result));

        System.out.printf("Received operation: %s \t Result: %s \n", message[0], result == null ? "ERROR" : result);

        StatsReporter.incrementOperation(message[0], result);


        handler.close();

    }

    private void error() {
        handler.sendMessage("ERROR");
        StatsReporter.incrementError();
        System.out.printf("Received operation: %s \t Result: %s \n", "ERROR", "ERROR");
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
