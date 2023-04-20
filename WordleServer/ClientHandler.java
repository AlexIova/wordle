import java.io.*;

public class ClientHandler implements Runnable {
    private InputStream inputStream;
    private OutputStream outputStream;
    private UsrDatabase db;
    private WordPicker wp;
    
    private String usrLogged = null;

    public ClientHandler(InputStream in, OutputStream out, UsrDatabase db, WordPicker wp) {
        this.inputStream = in;
        this.outputStream = out;
        this.db = db;
        this.wp = wp;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            while(true){
                Messaggio msg = (Messaggio) objectInputStream.readObject();
                System.out.println(msg);
                switch (msg.getType()) {
                    case REGISTER:
                        InstructionsServer.handleRegistration(msg, db, objectOutputStream);
                        System.out.println("Registration finished!");
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
                            InstructionsServer.handlePlay(msg, db, objectOutputStream, objectInputStream, wp);
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
