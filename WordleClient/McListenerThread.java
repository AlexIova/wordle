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

    @Override
    public void run() {
        while(true){
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                socket.receive(packet);
                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                DataGame dg = (DataGame) iStream.readObject();
                nDB.add(dg);
            } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
            finally {
                try {
                    socket.leaveGroup(group);
                    socket.close();
                } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

}
