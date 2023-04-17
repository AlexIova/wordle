import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.*;

/**
 * WordleServerMain
 */
public class WordleServerMain {

    private static String pathProp = "./WordleServer/server.properties";

    public static void main(String[] args) {

        /* Get properties */
        Properties properties = inProperties(pathProp);
        int SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        int THREAD_POOL_SIZE = Integer.parseInt(properties.getProperty("THREAD_POOL_SIZE"));
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        /* Restore previous session from JSON file */
        String nameUsrDB = properties.getProperty("NAME_USR_DB");
        UsrDatabase usrDB = restoreSession(nameUsrDB);
        assert(usrDB != null);

        /* Take incoming connections */
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("TCP Server started on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress().getHostAddress());
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                threadPool.execute(new ClientHandler(inputStream, outputStream, usrDB));
            }
        } catch (IOException e) {
            System.err.println("Error starting TCP Server: " + e.getMessage());
        }
    }

    /* Parse and return properties object */
    private static Properties inProperties(String pathProp) {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(pathProp);
            properties.load(input);
            input.close();
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
        }
        return properties;        
    }

    /* Restore previous session from JSON file */
    private static UsrDatabase restoreSession(String nameUsrDB) {
        UsrDatabase usrDB = new UsrDatabase();
        if (!new File(nameUsrDB).exists()) {
            return usrDB;
        }
        try {
            System.out.println("Restoring session from: " + nameUsrDB);
            Gson gson = new Gson();
            usrDB = gson.fromJson(new FileReader(nameUsrDB), UsrDatabase.class);
            return usrDB;
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    /* Handling of the clients */
    public static class ClientHandler implements Runnable {
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
                        handleRegistration(msg, db, objectOutputStream);
                        System.out.println("Registration finished!");
                        break;
                    default:
                        throw new WrongMessageException("Invalid message type");
                }
            } catch (IOException | ClassNotFoundException | WrongMessageException e) { 
                System.err.println("Error: " + e.getMessage());
            }
        }

    }

    private static void handleRegistration(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream) {
        RegisterMsg registerMsg = (RegisterMsg) msg;
        String password = registerMsg.getPassword();
        try{
            if (password.isEmpty()){
                StatusMsg statusMsg = new StatusMsg(2, "Password field is empty");
                objectOutputStream.writeObject(statusMsg);
            }
            String username = registerMsg.getUsername();
            if(db.usrExist(username)){
                StatusMsg statusMsg = new StatusMsg(1, "Username already exists");
                objectOutputStream.writeObject(statusMsg);
            } else {
                db.add(new Utente(username, password));
                StatusMsg statusMsg = new StatusMsg(0, "Registration successfull");
                objectOutputStream.writeObject(statusMsg);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return;
    }

}