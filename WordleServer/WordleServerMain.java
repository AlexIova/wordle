import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.*;

public class WordleServerMain {

    private static String pathProp = "server.properties";

    /**
     * This is the main function that starts the server.
     * @param args None
     */
    public static void main(String[] args) {

        /* Get properties */
        Properties properties = inProperties(pathProp);
        int SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        int THREAD_POOL_SIZE = Integer.parseInt(properties.getProperty("THREAD_POOL_SIZE"));
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int MC_SERVER_PORT = Integer.parseInt(properties.getProperty("MC_SERVER_PORT"));
        String MC_ADDR = properties.getProperty("MC_ADDR");
        String nameUsrDB = properties.getProperty("NAME_USR_DB");
        String nameGameData = properties.getProperty("NAME_GAME_DB");
        int time = Integer.parseInt(properties.getProperty("TIME_WORD"));
        String pathWords = properties.getProperty("PATH_WORDS_DB");

        /* Restore previous session from JSON file */
        UsrDatabase usrDB = restoreUsrSession(nameUsrDB);
        assert(usrDB != null);

        /* Restore user's game data from JSON file */
        GameDatabase gameDB = restoreGameSession(nameGameData);
        assert(gameDB != null);

        /* Hook for saving before exiting */
        Runtime.getRuntime().addShutdownHook(
                new Thread(new ExitHandler(usrDB, gameDB, nameUsrDB, nameGameData))
            );

        /* Start thread for changing words periodically */
        WordPicker wp = new WordPicker(pathWords);
        threadPool.execute(new ChangeThread(time, wp));      

        /* Take incoming connections */
        try (
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        DatagramSocket dgSock = new DatagramSocket() ) {
            InetAddress mcAddr = InetAddress.getByName(MC_ADDR);
            if (!mcAddr.isMulticastAddress()) {
                throw new IllegalArgumentException(
                "Indirizzo multicast non valido: " + mcAddr.getHostAddress());
            }
            System.err.println("TCP Server started on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();            // Accepting TCP connection
                System.err.println("Connection accepted from " + clientSocket.getInetAddress().getHostAddress());
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                threadPool.execute(new ClientHandler(inputStream, outputStream, usrDB, gameDB, wp, dgSock, mcAddr, MC_SERVER_PORT));
            }
        } catch (IOException e) {
            System.err.println("Error starting TCP Server: " + e.getMessage());
        }
    }

    /**
     * Parses a properties file and returns a Properties object.
     * @param pathProp the path to the properties file
     * @return the Properties object containing the properties from the file
     */
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


    /**
     * Restores the previous session from a JSON file.
     *
     * @param nameUsrDB The name of the user database file.
     * @return The restored user database or null.
     */
    private static UsrDatabase restoreUsrSession(String nameUsrDB) {
        UsrDatabase usrDB = new UsrDatabase();

        // Check if the file exists
        if (!new File(nameUsrDB).exists()) {
            return usrDB;
        }

        try {
            System.err.println("Restoring session from: " + nameUsrDB);

            // Use Gson to deserialize the JSON file into a UsrDatabase object
            Gson gson = new Gson();
            usrDB = gson.fromJson(new FileReader(nameUsrDB), UsrDatabase.class);

            return usrDB;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Restores a game session from a file.
     *
     * @param nameGameDB the name of the file containing the game database
     * @return The restored game database or null
     */
    private static GameDatabase restoreGameSession(String nameGameDB){
        GameDatabase gameDB = new GameDatabase();
        if (!new File(nameGameDB).exists()) {
            return gameDB;
        }
        try {
            System.err.println("Restoring session from: " + nameGameDB);
            Gson gson = new Gson();
            gameDB = gson.fromJson(new FileReader(nameGameDB), GameDatabase.class);
            return gameDB;
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

}