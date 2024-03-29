import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientHandler implements Runnable {

    private InputStream inputStream;
    private OutputStream outputStream;
    private UsrDatabase db;
    private WordPicker wp;
    private GameDatabase gameDB;
    private DatagramSocket dgSock;
    private InetAddress mcAddr;
    private int MC_SERVER_PORT;
    
    private String usrLogged = null;

    public ClientHandler(InputStream in, OutputStream out, UsrDatabase db, GameDatabase gameDB, WordPicker wp, DatagramSocket dgSock, InetAddress mcAddr, int MC_SERVER_PORT) {
        this.inputStream = in;
        this.outputStream = out;
        this.db = db;
        this.wp = wp;
        this.gameDB = gameDB;
        this.dgSock = dgSock;
        this.mcAddr = mcAddr;
        this.MC_SERVER_PORT = MC_SERVER_PORT;
    }

    /**
     * This method runs the server and listens for incoming messages from clients.
     * It handles each message according to its type and calls the appropriate
     * method to handle the message and respond to the client.
     */
    @Override
    public void run() {
        try {
            // Create input and output streams for the server
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            // Loop to listen for incoming messages
            while(true){
                // Read the incoming message from the client
                Messaggio msg = (Messaggio) objectInputStream.readObject();
                System.err.println(msg);

                // Handle the message based on its type
                switch (msg.getType()) {
                    case REGISTER:
                        InstructionsServer.handleRegistration(msg, db, objectOutputStream);
                        System.err.println("Registration finished!");
                        break;
                    case LOGIN:
                        usrLogged = InstructionsServer.handleLogin(msg, db, objectOutputStream);
                        break;
                    case LOGOUT:
                        if(InstructionsServer.handleLogout(msg, db, objectOutputStream)){
                            usrLogged = null;
                        }
                        break;
                    case PLAY:
                        if(db.usrExist(usrLogged) && db.getUser(usrLogged).isLogged()){
                            InstructionsServer.handlePlay(msg, db, gameDB, objectOutputStream, objectInputStream, wp);
                        }
                        break;
                    case REQ_STAT:
                        if(db.usrExist(usrLogged) && db.getUser(usrLogged).isLogged()){
                            InstructionsServer.handleReqStat(msg, gameDB, objectOutputStream);
                        }
                        break;
                    case SHARE:
                        if(db.usrExist(usrLogged) && db.getUser(usrLogged).isLogged()){
                            InstructionsServer.handleShare(msg, gameDB, db, objectOutputStream, dgSock, mcAddr, MC_SERVER_PORT);
                        }
                        break;
                    default:
                        throw new WrongMessageException("Invalid message type");
                }
            }
        } catch (IOException | ClassNotFoundException | WrongMessageException e) { 
            System.err.println("Error: " + e.getMessage());
        }
    }

}
