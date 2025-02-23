
public class CCS {

    public static Integer port = null;


    public CCS(int port) {
        CCS.port = port;

        new Thread(new UDPservice()).start();
        new Thread(new TCPservice()).start();
        new Thread(new StatsReporter()).start();
    }



    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Not valid arguments, required: CCS <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        new CCS(port);
    }


}