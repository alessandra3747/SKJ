import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPservice implements Runnable {
    @Override
    public void run() {

        try (DatagramSocket socket = new DatagramSocket(CCS.port)){
            socket.setBroadcast(true);

            while(!Thread.interrupted()) {
                byte[] buffer = new byte[12]; //"CCS DISCOVER" has 12 bytes
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength()).trim();

                if(message.startsWith("CCS DISCOVER")) {
                    buffer = "CCS FOUND".getBytes();
                    DatagramPacket packetSending = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                    socket.send(packetSending);
                }

            }

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }
}