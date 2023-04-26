import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

/**
 * WordleClientMain
 */
public class WordleClientMain {

    private static String pathProp = "./WordleClient/client.properties";

    public static void main(String[] args) {
        Properties properties = initProp(pathProp);
        String SERVER_ADDRESS = properties.getProperty("SERVER_ADDRESS");
        int SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        String MC_ADDR = properties.getProperty("MC_ADDR");
        int MC_CLIENT_PORT = Integer.parseInt(properties.getProperty("MC_CLIENT_PORT"));

        /* Start listening MC thread */
        NotificheDB nDB = new NotificheDB();
        try {
            McListenerThread mcListenerThread = new McListenerThread(MC_ADDR, MC_CLIENT_PORT, nDB);
            mcListenerThread.start();
        } catch (IOException e) { e.printStackTrace(); }

        String username = null;         // if username=null it's almost like logged out
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);       // Is inportant to put 1st outStream to not get into a deadlock
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try (Scanner scanner = new Scanner(System.in)) {
                while(true){
                    System.out.println("Benvenuto in Wordle! Di seguito le opzioni per giocare:\n\t1. Register \n\t2. Login\n\t3. Logout\n\t4. Play\n\t5. sendMeStatistics \n\t6. share \n\t7. showMeSharing \n\t8. exit\n---------------------");
                    int option = scanner.nextInt();
                    scanner.nextLine();     // consuming the newline after the option
                    System.err.println("DEBUG option chosen:" + option);
                    switch (option) {
                        case 1:
                            System.err.println("DEBUG Initiating register handle");
                            InstructionsClient.handleRegistration(objectOutputStream, objectInputStream, scanner);
                            break;
                        case 2:
                            System.err.println("DEBUG Initiating login handle");
                            username = InstructionsClient.handleLogin(objectOutputStream, objectInputStream, scanner);
                            break;
                        case 3:
                            System.err.println("DEBUG Initiating logout handle");
                            if(InstructionsClient.handleLogout(objectOutputStream, objectInputStream, scanner)){
                                username = null;
                            }
                            break;
                        case 4:
                            System.err.println("DEBUG Initiating play handler");
                            InstructionsClient.playWordle(objectOutputStream, objectInputStream, scanner, username);
                            break;
                        case 5:
                            System.err.println("DEBUG Initiating sendMeStatistics handler");
                            InstructionsClient.sendMeStatistics(objectOutputStream, objectInputStream, username);
                            break;
                        case 6:
                            System.err.println("DEBUG Initiating share handler");
                            InstructionsClient.handleShare(objectOutputStream, objectInputStream, scanner, username);
                            break;
                        case 7:
                            System.err.println("DEBUG Initiating showMeSharing handler");
                            InstructionsClient.handleShowMeSharing(nDB);
                            break;
                        case 8:
                            System.err.println("DEBUG Initiating exit handler");
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