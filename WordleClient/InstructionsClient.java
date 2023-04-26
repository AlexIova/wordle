import java.io.*;
import java.util.*;

public class InstructionsClient {
    
    /**
     * Sends a registration message to the server and handles the response.
     * @param objectOutputStream The output stream used to send messages to the server
     * @param objectInputStream The input stream used to receive messages from the server
     * @param scanner The scanner used to get input from the user
     */
    public static void handleRegistration(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner) {
        System.out.println("You are now trying to register. \nPlease enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

        RegisterMsg registerMsg = new RegisterMsg(username, password);

        try {
            objectOutputStream.writeObject(registerMsg);

            // Receive response from server
            Messaggio reply = (Messaggio) objectInputStream.readObject();

            // Handle response based on message code
            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;
                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("Registration successfull");
                        break;
                    case 1:
                        System.out.println("Username already exists");
                        break;
                    case 2:
                        System.out.println("Empty password field");
                        break;
                    default:
                        System.out.println("Unknown error code");
                        break;
                }
                return;
            } else {
                throw new WrongMessageException("Invalid reply");
            }
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { 
            e.printStackTrace(); 
        }
    }



    /**
     * This function handles the login process for the user.
    * @param objectOutputStream - The stream to write the login message to the server
    * @param objectInputStream - The stream to read the server's response
    * @param scanner - The scanner to read user input
    * @return The username of the user who successfully logged in
    */
    public static String handleLogin(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner){
        // Ask for username and password
        System.out.println("You are now trying to login. \nPlease enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
    
        LoginMsg loginMsg = new LoginMsg(username, password);
        try {
            // Send the LoginMsg object to the server
            objectOutputStream.writeObject(loginMsg);
    
            // Read the server's response
            Messaggio reply = (Messaggio) objectInputStream.readObject();
            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;
                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("Login successfull");
                        return username;
                    case 1:
                        System.out.println("Already logged in");
                        break;
                    case 2:
                        System.out.println("Wrong password");
                        break;
                    case 3:
                        System.out.println("Username not found");
                        break;
                    default:
                        System.out.println("Unknown error code");
                        break;
                }
            } else {
                throw new WrongMessageException("Invalid reply");
            }
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { 
            e.printStackTrace(); 
        }
        return username;
    }



    /**
     * Sends a logout message to the server and handles the response.
     *
     * @param objectOutputStream the output stream to send the message through
     * @param objectInputStream the input stream to receive the response from
     * @param scanner the scanner to read user input from
     * @return true if the logout was successful, false otherwise
     */
    public static Boolean handleLogout(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner) {
        // Prompt the user for their username
        System.out.println("You are now trying to logout.\nPlease enter your username: ");
        String username = scanner.nextLine();

        // Create a logout message with the username
        LogoutMsg logoutMsg = new LogoutMsg(username);

        try {
            // Send the logout message to the server
            objectOutputStream.writeObject(logoutMsg);

            // Receive and handle the server's response
            Messaggio reply = (Messaggio) objectInputStream.readObject();
            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;
                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("Logout successfull");
                        return true;
                    case 1:
                        System.out.println("Not logged in");
                        break;
                    default:
                        System.out.println("Unknown error code");
                        break;
                }
            } else {
                throw new WrongMessageException("Invalid reply");
            }
        } catch (ClassNotFoundException | IOException | WrongMessageException e) {
            e.printStackTrace();
        }
        // The logout was not successful
        return false;
    }


    /**
     * Sends a logout message to the server and waits for a reply.
     *
     * @param objectOutputStream the output stream to write the message to
     * @param objectInputStream the input stream to read the reply from
     * @param scanner            a scanner to read user input from
     * @param username           the username of the user to log out
     * @return true if the logout was successful, false otherwise
     */
    public static Boolean handleExit(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner, String username) {
        System.out.println("Automatically logging out before exiting: ");

        // Create a logout message with the given username
        LogoutMsg logoutMsg = new LogoutMsg(username);

        try {
            // Send the logout message to the server
            objectOutputStream.writeObject(logoutMsg);

            // Wait for the server to reply with a message
            Messaggio reply = (Messaggio) objectInputStream.readObject();

            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;
                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("Logout successful");
                        return true;
                    case 1:
                        System.out.println("Not logged in");
                        break;
                    default:
                        System.out.println("Unknown error code");
                        break;
                }
            } else {
                throw new WrongMessageException("Invalid reply");
            }
        } catch (ClassNotFoundException | IOException | WrongMessageException e) {
            e.printStackTrace();
        }
        // Return false if the logout was not successful
        return false;
    }



    /**
     * Plays the game Wordle.
     * 
     * @param objectOutputStream The ObjectOutputStream to send the play request to the server.
     * @param objectInputStream The ObjectInputStream to receive the server's response.
     * @param scanner The Scanner object to read user input.
     * @param username The username of the player.
     */
    public static void playWordle(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner, String username) {
        System.out.println("You are now trying to play Wordle.");

        // Create a PlayMsg object with the given username and send it to the server
        PlayMsg playReq = new PlayMsg(username);
        try {
            objectOutputStream.writeObject(playReq);

            // Wait for a response from the server
            Messaggio reply = (Messaggio) objectInputStream.readObject();

            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;
                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("We are ready to play!");
                        game(objectOutputStream, objectInputStream, scanner, username);
                        break;
                    case 1:
                        System.out.println("Username does not exist");
                        break;
                    case 2:
                        System.out.println("User not logged in");
                        break;
                    case 3:
                        System.out.println("Word already played try again later");
                        break;
                    default:
                        System.out.println("Unknown error code");
                        break;
                }
            } else {
                throw new WrongMessageException("Invalid reply");
            }
        } catch (WrongMessageException | ClassNotFoundException | IOException e) { 
            e.printStackTrace(); 
        }

    }

    /**
     * Plays the Wordle game.
     * 
     * @param objectOutputStream The output stream used to send messages to the server.
     * @param objectInputStream The input stream used to receive messages from the server.
     * @param scanner The scanner used to read user input.
     * @param username The username of the player.
     */
    private static void game(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner, String username) {
        System.out.println("You are now playing wordle:");

        Boolean end = false;
        String guess = null;
        Messaggio res = null;
        List<String> log = new ArrayList<String>();

        while(!end){
            // Prompt user for input
            System.out.print("> ");
            guess = scanner.nextLine();
            PlayMsg guessMsg = new PlayMsg(username, guess);
            log.add(guess);
            try {
                // Send guess to server and receive response
                objectOutputStream.writeObject(guessMsg);
                res = (Messaggio) objectInputStream.readObject();
                if(!(res.getType() == MessageType.GAME)){
                    throw new WrongMessageException("Invalid reply");
                }
                GameMsg gameRes = (GameMsg) res;
                // Handle response based on code
                switch (gameRes.getCode()) {
                    case 0:             // All ok
                        log.add(gameRes.getRes());
                        // Print log of guesses
                        System.out.println("\n---------------");
                        for(String s : log){
                            System.out.println(s);
                        }
                        System.out.println("---------------");
                        break;
                    case 1:             // Game won
                        System.out.println("Game won");
                        end = true;
                        break;
                    case 2:             // Word must be of 10 characters
                        System.out.println("Word must be of 10 characters");
                        break;
                    case 3:             // Word not in dictionary
                        System.out.println("Word not in dictionary");
                        break;
                    case 4:
                        System.out.println("Game lost");
                        end = true;
                        break;
                    default:
                        System.out.println("Unknown code");
                        break;
                }
            } catch (ClassNotFoundException | IOException | WrongMessageException e) {
                e.printStackTrace();
            }
        }
        try {
            // Receive statistics from server
            res = (Messaggio) objectInputStream.readObject();
            if(!(res.getType() == MessageType.STATISTICS)){
                throw new WrongMessageException("Invalid reply");
            }
            StatisticMsg stats = (StatisticMsg) res;
            System.out.println("Statistics: " + stats);
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { 
            e.printStackTrace(); 
        }
        // Print final message
        System.out.println("Fine gioco");
    }


    /**
     * Sends a statistics request message to the server and prints the response.
     * @param objectOutputStream the output stream to write the request message to
     * @param objectInputStream the input stream to read the response message from
     * @param username the username of the user requesting the statistics
     */
    public static void sendMeStatistics(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String username) {
        System.out.println("Now asking for statistics");

        // Create request message with given username
        StatReqMsg statReqMsg = new StatReqMsg(username);

        try {
            // Send request message to server
            objectOutputStream.writeObject(statReqMsg);

            // Read response message from server
            Messaggio res = (Messaggio) objectInputStream.readObject();

            if(!(res.getType() == MessageType.STATISTICS)){
                throw new WrongMessageException("Invalid reply");
            }

            StatisticMsg stats = (StatisticMsg) res;
            System.out.println("Statistics: " + stats);
        } catch (ClassNotFoundException | IOException | WrongMessageException e) {
            e.printStackTrace();
        }
    }



    /**
     * Sends a ShareMsg to the server with the specified username and waits for a reply.
     * Prints an appropriate message based on the reply received.
     *
     * @param objectOutputStream the ObjectOutputStream to send the ShareMsg to the server
     * @param objectInputStream  the ObjectInputStream to read the reply from the server
     * @param scanner            a Scanner object to read input from the user
     * @param username           the username to share the file with
     */
    public static void handleShare(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                Scanner scanner, String username) {

        // Create a new ShareMsg with the specified username
        ShareMsg shareMsg = new ShareMsg(username);
        try {
            // Send the ShareMsg to the server
            objectOutputStream.writeObject(shareMsg);

            // Wait for a reply from the server
            Messaggio reply = (Messaggio) objectInputStream.readObject();

            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;

                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("Share successful");
                        break;
                    case 1:
                        System.out.println("Username does not exist");
                        break;
                    case 2:
                        System.out.println("User not logged in");
                        break;
                    default:
                        System.out.println("Unknown error code");
                        throw new WrongMessageException("Unknown error code");
                }
            }
        } catch (ClassNotFoundException | IOException | WrongMessageException e) {
            e.printStackTrace();
        }
    }



    /**
     * This method handles the showing of notifications for a given user.
     * It prints out all the notifications that are found in the nDB, except for those
     * that belong to the given username.
     * 
     * @param nDB the NotificheDB object containing the notifications to be shown
     * @param username the username of the user for whom the notifications are being shown
     */
    public static void handleShowMeSharing(NotificheDB nDB, String username) {

        // loop through all the notifications in the nDB
        while(!nDB.isEmpty()){
            // get the next notification
            StatisticMsg notif = nDB.pop();

            // check if the notification belongs to the given user
            if(!notif.getUsername().equals(username)){
                // if it doesn't, print it out
                System.out.println(notif);
            }
        }
    }


}
