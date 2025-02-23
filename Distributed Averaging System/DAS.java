import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class DAS {

    private Integer inputPort;
    private Integer inputNumber;
    private static Mode currentMode;
    private DatagramSocket socket;
    private ArrayList<Integer> numberList;


    private enum Mode {
        MASTER , SLAVE;
    }


    public DAS(Integer inputPort, Integer inputNumber) {
        this.inputPort = inputPort;
        this.inputNumber = inputNumber;
        this.numberList = new ArrayList<>();

        try {
            this.socket = new DatagramSocket(inputPort);
            socket.setBroadcast(true);
            this.currentMode = Mode.MASTER;
            System.out.println("Started: Mode Master");
            runMaster();

        } catch(SocketException e) {
            this.currentMode = Mode.SLAVE;
            System.out.println("Started: Mode Slave");
            runSlave();
        }

    }


    private void runMaster() {
        this.numberList.add(inputNumber);
        byte[] buffer = new byte[129];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength()).trim();

                if (message.equals("ACK")) {
                    continue;
                }


                try {
                    Integer numberReceived = Integer.parseInt(message);

                    switch(numberReceived) {
                        case 0:
                            Integer avg = (int) numberList.stream()
                                    .filter(e -> e != 0)
                                    .mapToDouble(Integer::doubleValue)
                                    .average().orElse(0);

                            System.out.println(numberReceived);

                            sendMessage(String.valueOf(avg), InetAddress.getByName("255.255.255.255"), inputPort, true);

                            break;

                        case -1:
                            System.out.println(numberReceived);
                            sendMessage(String.valueOf(numberReceived), InetAddress.getByName("255.255.255.255"), inputPort, true);
                            sendMessage("ACK", packet.getAddress(), packet.getPort(), false);
                            socket.close();
                            return;

                        default:
                            System.out.println(numberReceived);
                            numberList.add(numberReceived);

                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid message received: " + message);
                }

                sendMessage("ACK", packet.getAddress(), packet.getPort(), false);


            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }



    private void sendMessage(String message, InetAddress address, int port, boolean isBroadcast) {
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.setBroadcast(isBroadcast);
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + message);
        }
    }


    private void runSlave() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(2000);

            byte[] data = String.valueOf(inputNumber).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), inputPort);

            boolean acknowledged = false;
            int attempts = 0;

            while (attempts < 3 && !acknowledged) {
                try {
                    socket.send(packet);

                    byte[] buffer = new byte[129];
                    DatagramPacket ackPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(ackPacket);

                    String response = new String(ackPacket.getData(), 0, ackPacket.getLength()).trim();
                    if (response.equals("ACK")) {
                        acknowledged = true;
                    }

                } catch (SocketTimeoutException e) {
                    System.out.println("No acknowledgment received. Retrying...");
                    attempts++;
                }
            }

            if (!acknowledged) {
                System.out.println("Failed to receive acknowledgment after 3 attempts.");
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {

        if(args.length != 2) {
            System.out.println("Not enough arguments, required: DAS <port> <number>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        int number = Integer.parseInt(args[1]);

        new DAS(port, number);
    }


}