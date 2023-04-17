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
        System.out.println("Lorem ipsum qui ci sono le tue opzioni:\n\t1. Register \n\t2. Login\n\t3. Logout\n\t4. Play\n\t5. sendMeStatistics \n\t6. share \n\t7. showMeSharing \n\t8. exit\n---------------------");
        Properties properties = initProp(pathProp);
        String SERVER_ADDRESS = properties.getProperty("SERVER_ADDRESS");
        int SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);       // Is inportant to put 1st outStream to not get into a deadlock
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try (Scanner scanner = new Scanner(System.in)) {
                int option = scanner.nextInt();
                scanner.nextLine();     // consuming the newline after the option
                System.out.println("option chosen: " + option);
                switch (option) {
                    case 1:
                        System.out.println("Initiating register handle");
                        handleRegistration(objectOutputStream, objectInputStream);
                        break;
                    case 2:
                        System.out.println("Login");
                        break;
                    case 3:
                        System.out.println("Logout");
                        break;
                    case 4:
                        System.out.println("Play");
                        break;
                    case 5:
                        System.out.println("sendMeStatistics");
                        break;
                    case 6:
                        System.out.println("share");
                        break;
                    case 7:
                        System.out.println("showMeSharing");
                        break;
                    case 8:
                        System.out.println("exit");
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
            inputStream.close();
            outputStream.close();
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


    private static void handleRegistration(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream){
        System.out.println("You are now trying to register. \nPlease enter your username: ");
        try (Scanner scanner = new Scanner(System.in)) {
            String username = scanner.nextLine();
            System.out.println("Please enter your password: ");
            String password = scanner.nextLine();
            RegisterMsg registerMsg = new RegisterMsg(username, password);
            try {
                objectOutputStream.writeObject(registerMsg);
                System.out.println(registerMsg);
                System.out.println("Registration request sent");
                Messaggio reply = (Messaggio) objectInputStream.readObject();
                if (reply.getType() == MessageType.STATUS) {
                    StatusMsg statusMsg = (StatusMsg) reply;
                    switch (statusMsg.getCode()) {
                        case 0:
                            System.out.println("Registration successfull");
                            break;
                        case 1:
                            System.out.println("Username already exists");
                            return;
                        case 2:
                            System.out.println("Empty password field");
                        default:
                            System.out.println("Unknown error code");
                            break;
                    }
                } else {
                    throw new WrongMessageException("Invalid reply");
                }
            } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
        }
    }

}