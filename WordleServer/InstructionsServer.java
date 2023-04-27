import java.io.*;
import java.net.*;
import java.util.*;

public class InstructionsServer {
    
/**
 * Handles user registration by adding a new user to the database.
 * Sends a status message back to the client indicating the success or failure of the registration.
 *
 * @param msg the incoming message from the client
 * @param db the database of registered users
 * @param objectOutputStream the output stream to send the status message back to the client
 */
public static void handleRegistration(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream) {
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
            db.add(new Utente(username, password));     // Add the new user
            StatusMsg statusMsg = new StatusMsg(0, "Registration successful");
            objectOutputStream.writeObject(statusMsg);
        }
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
    }
    return;
}


    /**
     * Handles the login process for a user.
     * @param msg The login message received from the user.
     * @param db The user database to check for the user's existence and credentials.
     * @param objectOutputStream The output stream to write back the status message.
     * @return The username of the user who successfully logged in, or null if login failed.
     */
    public static String handleLogin(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream) {
        LoginMsg loginMsg = (LoginMsg) msg;
        String username = loginMsg.getUsername();
        String password = loginMsg.getPassword();
        try{
            if (db.usrExist(username)){
                Utente user = db.getUser(username);
                if (user.getPassword().equals(password)){
                    if(!user.isLogged()){
                        StatusMsg statusMsg = new StatusMsg(0, "Login successfull");
                        user.changeLogging(true);
                        objectOutputStream.writeObject(statusMsg);
                        return username;
                    } else {
                        StatusMsg statusMsg = new StatusMsg(1, "Already logged in");
                        objectOutputStream.writeObject(statusMsg);
                    }
                } else {
                    StatusMsg statusMsg = new StatusMsg(2, "Wrong password");
                    objectOutputStream.writeObject(statusMsg);
                }
            } else {
                StatusMsg statusMsg = new StatusMsg(3, "Username not found");
                objectOutputStream.writeObject(statusMsg);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Handles the logout request of a user.
     *
     * @param msg The message containing the logout request.
     * @param db The database of users.
     * @param objectOutputStream The output stream to send the response back to the client.
     *
     * @return Returns true if the logout was successful, false otherwise.
     */
    public static Boolean handleLogout(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream) {
        LogoutMsg logoutMsg = (LogoutMsg) msg;
        String username = logoutMsg.getUsername();
        try{
            if(db.usrExist(username)){
                Utente user = db.getUser(username);
                user.changeLogging(false);

                StatusMsg statusMsg = new StatusMsg(0, "Logout successful");
                objectOutputStream.writeObject(statusMsg);

                return true;
            } else {
                StatusMsg statusMsg = new StatusMsg(1, "Username not found");
                objectOutputStream.writeObject(statusMsg);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * This method handles a play message.
     * @param msg The message to handle.
     * @param db The user database.
     * @param gameDB The game database.
     * @param objectOutputStream The output stream to write messages to.
     * @param objectInputStream The input stream to read messages from.
     * @param wp The word picker.
     */
    public static void handlePlay(Messaggio msg, UsrDatabase db, GameDatabase gameDB, 
                                    ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, 
                                    WordPicker wp) {
        PlayMsg playMsg = (PlayMsg) msg;
        String username = playMsg.getUsername();
        try{
            if(!db.usrExist(username)){
                StatusMsg statusMsg = new StatusMsg(1, "Username not found");
                objectOutputStream.writeObject(statusMsg);
                return;             
            }
            if(!db.getUser(username).isLogged()){
                StatusMsg statusMsg = new StatusMsg(2, "Not logged in");
                objectOutputStream.writeObject(statusMsg);
                return;
            }
            DataGame dataGame = gameDB.getDataGame(username);
            if (dataGame != null && dataGame.alreadyPlayed(wp.getSecretWord())){        // the && short-circuits
                StatusMsg statusMsg = new StatusMsg(3, "You already played with this word");
                objectOutputStream.writeObject(statusMsg);
                return;
            }
            // If all checks pass, send a status message with a success code and message
            StatusMsg statusMsg = new StatusMsg(0, "Play successful");
            objectOutputStream.writeObject(statusMsg);
            playWordle(db, gameDB, objectOutputStream, objectInputStream, wp);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }



    /**
     * Plays a game of Wordle between a user and the computer.
     * @param usrDb a database of users
     * @param gameDB a database of games
     * @param objectOutputStream the output stream to send messages to the user
     * @param objectInputStream the input stream to receive messages from the user
     * @param wp a WordPicker object to generate the secret word
     */
    private static void playWordle(UsrDatabase usrDb, GameDatabase gameDB, ObjectOutputStream objectOutputStream,
                                    ObjectInputStream objectInputStream, WordPicker wp) {

        // Pick a secret word
        String secretWord = wp.getSecretWord();

        // Initialize variables
        String guessedWord = null;
        int iter = 0;
        GameMsg resGame = null;
        String username = null;
        String row = null;

        // Initialize a list to keep track of the game matrix
        List<String> gameMatrix = new ArrayList<String>();

        while(!secretWord.equals(guessedWord) && iter < 12){
            try{
                iter++;
                Messaggio msg = (Messaggio) objectInputStream.readObject();
                if(!(msg.getType() == MessageType.PLAY)){
                    throw new WrongMessageException("Invalid message type");
                }

                PlayMsg playMsg = (PlayMsg) msg;
                username = usrDb.getUser(playMsg.getUsername()).getUsername();
                assert(playMsg.getUsername().equals(username));
                guessedWord = playMsg.getGuessedWord();
                
                if(guessedWord.equals(secretWord)){              // Won
                    resGame = new GameMsg("", 1);
                    row = buildRes(secretWord, guessedWord);
                    gameMatrix.add(row);
                    System.err.println("PLAYER WON!");
                } 
                
                else if(guessedWord.length() != 10){             // Word must have 10 characters
                    resGame = new GameMsg("", 2);
                    iter--;
                } 
                
                else if(!wp.inDb(guessedWord)){                  // Word not in dictionary
                    resGame = new GameMsg("", 3);
                    iter--;
                } 
                
                else if (iter == 12){
                    resGame = new GameMsg("", 4);    // Game lost
                }
                
                else {
                    row = buildRes(secretWord, guessedWord);
                    gameMatrix.add(row);
                    resGame = new GameMsg(row, 0);    // Normal game
                }

                objectOutputStream.writeObject(resGame);
            } catch (IOException | ClassNotFoundException | WrongMessageException e) { System.err.println("Error: " + e.getMessage()); }
        }
        // Now we add the data to the game database
        // First we check if the username is already in the list of games of gameDB
        DataGame dg = null;
        if(!gameDB.usrExist(username)){
            dg = new DataGame(username);
        } else {
            dg = gameDB.getDataGame(username);
        }

        Partita partita = new Partita(gameMatrix, secretWord);
        dg.addPartita(partita);
        gameDB.add(dg);

        StatisticMsg statMsg = new StatisticMsg(dg);
        try {
            objectOutputStream.writeObject(statMsg);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Build the result string for a given secret word and guessed word.
     * Each character in the secret word is compared against the character
     * in the same position in the guessed word. A "+" is added to the result
     * string for each character that matches, a "?" is added for each character
     * that appears elsewhere in the guessed word but not in that position, 
     * and an "X" is added for each character that does not appear in the guessed word at all.
     *
     * @param secretWord The secret word to compare against.
     * @param guessedWord The guessed word to compare with.
     * @return The result string.
     */
    private static String buildRes(String secretWord, String guessedWord) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guessedWord.charAt(i)) {
                result.append("+");
            } else if (guessedWord.indexOf(secretWord.charAt(i)) != -1) {
                result.append("?");
            } else {
                result.append("X");
            }
        }
        return result.toString();
    }

    /**
     * Handles a StatReqMsg by retrieving the user's game data and sending it back as a StatisticMsg.
     * @param msg The StatReqMsg to handle.
     * @param db The GameDatabase to retrieve data from.
     * @param objectOutputStream The ObjectOutputStream to send the StatisticMsg back through.
     */
    public static void handleReqStat(Messaggio msg, GameDatabase db, ObjectOutputStream objectOutputStream) {
        StatReqMsg reqStatMsg = (StatReqMsg) msg;
        String username = reqStatMsg.getUsername();

        DataGame dg = db.getDataGame(username);

        StatisticMsg statMsg = new StatisticMsg(dg);
        try {
            objectOutputStream.writeObject(statMsg);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * This method handles sharing of data from the game database with a specific user.
     *
     * @param msg The message containing the data to be shared
     * @param gameDB The game database containing the data to be shared
     * @param usrDB The user database containing information about the user
     * @param objectOutputStream The stream to write objects to
     * @param dgSock The datagram socket to use for sending data
     * @param mcAddr The multicast address to send data to
     * @param MC_SERVER_PORT The port number to use for the multicast socket
     */
    public static void handleShare(Messaggio msg, GameDatabase gameDB, UsrDatabase usrDB, ObjectOutputStream objectOutputStream,
                                DatagramSocket dgSock, InetAddress mcAddr, int MC_SERVER_PORT) {

        ShareMsg shareMsg = (ShareMsg) msg;

        try {

            if (!gameDB.usrExist(shareMsg.getUsername())) {
                StatusMsg statusMsg = new StatusMsg(1, "Username not found");
                objectOutputStream.writeObject(statusMsg);
                return;
            }

            if (!usrDB.getUser(shareMsg.getUsername()).isLogged()) {
                StatusMsg statusMsg = new StatusMsg(2, "Not logged in");
                objectOutputStream.writeObject(statusMsg);
                return;
            }

            DataGame dg = gameDB.getDataGame(shareMsg.getUsername());

            StatisticMsg statMsg = new StatisticMsg(dg);

            // Serialize the StatisticMsg object
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteStream);
            objectOutput.writeObject(statMsg);
            objectOutput.close();
            byte[] oggSerial = byteStream.toByteArray();

            // Create a DatagramPacket with the data and send it to the multicast address
            DatagramPacket dp = new DatagramPacket(oggSerial, oggSerial.length, mcAddr, MC_SERVER_PORT);
            dgSock.send(dp);

            StatusMsg statusMsg = new StatusMsg(0, "Dati condivisi");
            objectOutputStream.writeObject(statusMsg);

        } catch (IOException e) { 
            System.err.println("Error: " + e.getMessage()); 
        }
    }

}
