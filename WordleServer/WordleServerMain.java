import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        /* Take incoming connections */
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("TCP Server started on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress().getHostAddress());
                threadPool.execute(new ClientHandler(clientSocket));
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

    public static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received message from " + clientSocket.getInetAddress().getHostAddress() + ": " + message);
                    writer.println("Echo: " + message);
                }
            } catch (IOException e) {
                System.err.println("Error handling client request: " + e.getMessage());
            }
        }
    }
}