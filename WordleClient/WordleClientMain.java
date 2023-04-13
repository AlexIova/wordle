package WordleClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        System.out.println("Lorem ipsum qui ci sono le tue opzioni:\n\t1. Register \n\t2. Login\n\t3. Logout\n\t4. Play\n\t5. sendMeStatistics \n\t6. share \n\t 7. showMeSharing \n\t 8. exit");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.close();
        Properties properties = initProp(pathProp);
        String SERVER_ADDRESS = properties.getProperty("SERVER_ADDRESS");
        int SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            switch (option) {
                case 1:
                    System.out.println("Register");
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

}