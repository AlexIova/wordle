import java.io.*;

public class ClientHandler implements Runnable {
    private InputStream inputStream;
    private OutputStream outputStream;
    private UsrDatabase db;

    public ClientHandler(InputStream in, OutputStream out, UsrDatabase db) {
        this.inputStream = in;
        this.outputStream = out;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            Messaggio msg = (Messaggio) objectInputStream.readObject();
            System.out.println(msg);
            switch (msg.getType()) {
                case REGISTER:
                    InstructionsServer.handleRegistration(msg, db, objectOutputStream);
                    System.out.println("Registration finished!");
                    break;
                case LOGIN:
                    InstructionsServer.handleLogin(msg, db, objectOutputStream);
                    break;
                default:
                    throw new WrongMessageException("Invalid message type");
            }
        } catch (IOException | ClassNotFoundException | WrongMessageException e) { 
            System.err.println("Error: " + e.getMessage());
        }
    }

}
