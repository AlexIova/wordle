import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.*;

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
        UsrDatabase usrDB = restoreUsrSession(nameUsrDB);
        assert(usrDB != null);

        /* Restore user's game data from JSON file */
        String nameGameData = properties.getProperty("NAME_GAME_DB");
        GameDatabase gameDB = restoreGameSession(nameGameData);
        assert(gameDB != null);

        /* Hook for saving before exiting */
        Runtime.getRuntime().addShutdownHook(
                new Thread(new ExitHandler(usrDB, gameDB, nameUsrDB, nameGameData))
            );
        
        /* Start thread for changing words periodically */
        int time = Integer.parseInt(properties.getProperty("TIME_WORD"));
        String pathWords = properties.getProperty("PATH_WORDS_DB");
        WordPicker wp = new WordPicker(pathWords);
        threadPool.execute(new ChangeThread(time, wp));        

        /* Take incoming connections */
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("TCP Server started on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress().getHostAddress());
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                threadPool.execute(new ClientHandler(inputStream, outputStream, usrDB, gameDB, wp));
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
    private static UsrDatabase restoreUsrSession(String nameUsrDB) {
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

    private static GameDatabase restoreGameSession(String nameGameDB){
        GameDatabase gameDB = new GameDatabase();
        if (!new File(nameGameDB).exists()) {
            return gameDB;
        }
        try {
            System.out.println("Restoring session from: " + nameGameDB);
            Gson gson = new Gson();
            gameDB = gson.fromJson(new FileReader(nameGameDB), GameDatabase.class);
            return gameDB;
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

}