import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class WordleClientMain {

    private static String pathProp = "./client.properties";

    /**
     * Main function that runs the Wordle game client.
     *
     * @param args None
     */
    public static void main(String[] args) {
        // Load properties file
        Properties properties = initProp(pathProp);

        // Retrieve info from properties file
        String SERVER_ADDRESS = properties.getProperty("SERVER_ADDRESS");
        int SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        String MC_ADDR = properties.getProperty("MC_ADDR");
        int MC_CLIENT_PORT = Integer.parseInt(properties.getProperty("MC_CLIENT_PORT"));

        // Start listening MC thread
        NotificheDB nDB = new NotificheDB();
        try {
            McListenerThread mcListenerThread = new McListenerThread(MC_ADDR, MC_CLIENT_PORT, nDB);
            mcListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize username to null, which means the user is not logged in
        String username = null;

        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // Set up input and output streams to communicate with server
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            try (Scanner scanner = new Scanner(System.in)) {
                while(true){
                    System.out.println("Benvenuto in Wordle! Di seguito le opzioni per giocare:\n\t1. Register \n\t2. Login\n\t3. Logout\n\t4. Play\n\t5. sendMeStatistics \n\t6. share \n\t7. showMeSharing \n\t8. exit\n---------------------");
                    int option = scanner.nextInt();
                    scanner.nextLine();     // Consuming the newline after the option
                    switch (option) {
                        case 1:
                            InstructionsClient.handleRegistration(objectOutputStream, objectInputStream, scanner);
                            break;
                        case 2:
                            username = InstructionsClient.handleLogin(objectOutputStream, objectInputStream, scanner);
                            break;
                        case 3:
                            if(InstructionsClient.handleLogout(objectOutputStream, objectInputStream, scanner)){
                                username = null;
                            }
                            break;
                        case 4:
                            if(username != null){
                                InstructionsClient.playWordle(objectOutputStream, objectInputStream, scanner, username);   
                            }
                            break;
                        case 5:
                            if(username != null){
                                InstructionsClient.sendMeStatistics(objectOutputStream, objectInputStream, username);
                            }
                            break;
                        case 6:
                            if(username != null){
                                InstructionsClient.handleShare(objectOutputStream, objectInputStream, scanner, username);
                            }
                            break;
                        case 7:
                            if(username != null){
                                InstructionsClient.handleShowMeSharing(nDB, username);
                            }
                            break;
                        case 8:
                            if(username != null){
                                if(!InstructionsClient.handleExit(objectOutputStream, objectInputStream, scanner, username)){
                                    break;
                                }
                            }
                            System.out.println("Now exiting");
                            objectInputStream.close();
                            inputStream.close();
                            scanner.close();
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    /**
     * Initializes a Properties object with the values loaded from the file at the given path.
     *
     * @param pathProp The path of the properties file to load.
     * @return A Properties object initialized with the loaded values.
     */
    private static Properties initProp(String pathProp) {
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

}