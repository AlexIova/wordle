import java.io.*;
import java.net.*;

public class McListenerThread extends Thread {
 
    private MulticastSocket socket;
    private InetAddress group;
    private int BUFFER_SIZE = 2048;
    private NotificheDB nDB;

    public McListenerThread(String groupAddr, int port, NotificheDB nDB) throws IOException{
        this.group = InetAddress.getByName(groupAddr);
        this.socket = new MulticastSocket(port);
        this.group = InetAddress.getByName(groupAddr);
        this.socket.joinGroup(this.group);
        this.nDB = nDB;
    }

    /**
     * The run method for this thread continuously receives packets and adds them
     * to the nDB object.
     */
    @Override
    public void run() {
        while (true) {
            // Create a new datagram packet with a buffer of BUFFER_SIZE bytes
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                // Receive a packet from the socket
                socket.receive(packet);
                // Create a new object input stream to read the data from the packet
                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));

                StatisticMsg msg = (StatisticMsg) iStream.readObject();

                nDB.add(msg);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
