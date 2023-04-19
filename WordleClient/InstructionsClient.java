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
            System.out.println(loginMsg);
            System.out.println("Login request sent");
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
            System.out.println(logoutMsg);
            System.out.println("Logout request sent");
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
            System.out.println("playreq sent");
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
        System.out.println("WE ARE READY!");
        Boolean end = false;
        String guess = null;
        String res = null;
        while(!end){
            guess = scanner.nextLine();
            PlayMsg guessMsg = new PlayMsg(username, guess);
            try {
                objectOutputStream.writeObject(guessMsg);
                res = (String) objectInputStream.readObject();
                System.out.println(res);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
