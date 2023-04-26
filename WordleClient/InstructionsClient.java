import java.io.*;
import java.util.*;

public class InstructionsClient {
    
    public static void handleRegistration(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner){
        System.out.println("You are now trying to register. \nPlease enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        RegisterMsg registerMsg = new RegisterMsg(username, password);
        try {
            objectOutputStream.writeObject(registerMsg);
            System.err.println(registerMsg);
            System.err.println("Registration request sent");
            Messaggio reply = (Messaggio) objectInputStream.readObject();
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
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
    }


    public static String handleLogin(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner){
        System.out.println("You are now trying to login. \nPlease enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        LoginMsg loginMsg = new LoginMsg(username, password);
        try {
            objectOutputStream.writeObject(loginMsg);
            System.err.println(loginMsg);
            System.err.println("Login request sent");
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
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
        return username;
    }


    public static Boolean handleLogout(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner){
        System.out.println("You are now trying to logout. \nPlease enter your username: ");
        String username = scanner.nextLine();
        LogoutMsg logoutMsg = new LogoutMsg(username);
        try {
            objectOutputStream.writeObject(logoutMsg);
            System.err.println(logoutMsg);
            System.err.println("Logout request sent");
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
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
        return false;
    }

    public static Boolean handleExit(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner, String username){
        System.out.println("Automatically logging out before exiting: ");
        LogoutMsg logoutMsg = new LogoutMsg(username);
        try {
            objectOutputStream.writeObject(logoutMsg);
            System.err.println(logoutMsg);
            System.err.println("Logout request sent");
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
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
        return false;
    }


    public static void playWordle(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner, String username) {
        System.out.println("You are now trying to play Wordle.");
        PlayMsg playReq = new PlayMsg(username);
        try {
            objectOutputStream.writeObject(playReq);
            System.err.println("playreq sent");
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
        } catch (WrongMessageException | ClassNotFoundException | IOException e) { e.printStackTrace(); }

    }

    private static void game(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, Scanner scanner, String username){
        System.out.println("You are now playing wordle:");
        Boolean end = false;
        String guess = null;
        Messaggio res = null;
        List<String> log = new ArrayList<String>();
        while(!end){
            System.out.print("> ");
            guess = scanner.nextLine();
            PlayMsg guessMsg = new PlayMsg(username, guess);
            log.add(guess);
            try {
                objectOutputStream.writeObject(guessMsg);
                res = (Messaggio) objectInputStream.readObject();
                if(!(res.getType() == MessageType.GAME)){
                    throw new WrongMessageException("Invalid reply");
                }
                GameMsg gameRes = (GameMsg) res;
                switch (gameRes.getCode()) {
                    case 0:             // All ok
                        log.add(gameRes.getRes());
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
            res = (Messaggio) objectInputStream.readObject();
            if(!(res.getType() == MessageType.STATISTICS)){
                throw new WrongMessageException("Invalid reply");
            }
            StatisticMsg stats = (StatisticMsg) res;
            System.out.println("Statistics: " + stats);
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
        System.out.println("Fine gioco");
    }


    public static void sendMeStatistics(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String username) {
        System.out.println("Now asking for statistics");
        StatReqMsg statReqMsg = new StatReqMsg(username);
        try {
            objectOutputStream.writeObject(statReqMsg);
            Messaggio res = (Messaggio) objectInputStream.readObject();
            if(!(res.getType() == MessageType.STATISTICS)){
                throw new WrongMessageException("Invalid reply");
            }
            StatisticMsg stats = (StatisticMsg) res;
            System.out.println("Statistics: " + stats);
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
                                            
    }


    public static void handleShare(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
            Scanner scanner, String username) {

        ShareMsg shareMsg = new ShareMsg(username);
        try {
            objectOutputStream.writeObject(shareMsg);
            System.err.println(shareMsg);
            System.err.println("Share request sent");
            Messaggio reply = (Messaggio) objectInputStream.readObject();
            if (reply.getType() == MessageType.STATUS) {
                StatusMsg statusMsg = (StatusMsg) reply;
                switch (statusMsg.getCode()) {
                    case 0:
                        System.out.println("Share successfull");
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
        } catch (ClassNotFoundException | IOException | WrongMessageException e) { e.printStackTrace(); }
        
        
    }


    public static void handleShowMeSharing(NotificheDB nDB) {
        System.err.println("Now asking for notification");
        while(!nDB.isEmpty()){
            System.out.println(nDB.pop());
        }
    }

}
