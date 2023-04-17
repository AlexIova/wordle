import java.io.*;
import java.util.*;

public class InstructionsClient {
    
    public static void handleRegistration(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream){
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
