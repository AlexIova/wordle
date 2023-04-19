import java.io.*;

public class InstructionsServer {
    
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
                db.add(new Utente(username, password));
                StatusMsg statusMsg = new StatusMsg(0, "Registration successfull");
                objectOutputStream.writeObject(statusMsg);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return;
    }

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

    public static Boolean handleLogout(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream) {
        LogoutMsg logoutMsg = (LogoutMsg) msg;
        String username = logoutMsg.getUsername();
        try{
            if(db.usrExist(username)){
                Utente user = db.getUser(username);
                user.changeLogging(false);
                StatusMsg statusMsg = new StatusMsg(0, "Logout successfull");
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

    public static void handlePlay(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, WordPicker wp) {
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
            StatusMsg statusMsg = new StatusMsg(0, "Play successfull");
            objectOutputStream.writeObject(statusMsg);
            playWordle(db, objectOutputStream, objectInputStream, wp);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    private static void playWordle(UsrDatabase db, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, WordPicker wp) {
        System.out.println("WE ARE READY!");
        String secretWord = wp.getSecretWord();
        String guessedWord = null;
        int iter = 0;
        GameMsg resGame = null;
        System.out.println("SECRET WORD: " + secretWord);       // Just for debug, delete after
        while(!secretWord.equals(guessedWord) || iter < 12){
            try{
                iter++;
                Messaggio msg = (Messaggio) objectInputStream.readObject();
                if(!(msg.getType() == MessageType.PLAY)){
                    throw new WrongMessageException("Invalid message type");
                }
                PlayMsg playMsg = (PlayMsg) msg;
                assert(playMsg.getUsername().equals(db.getUser(playMsg.getUsername()).getUsername()));
                guessedWord = playMsg.getGuessedWord();
                if(guessedWord.equals(secretWord)){                                 // Won
                    resGame = new GameMsg("", 1);
                    System.out.println("PLAYER WON!");
                } else if(guessedWord.length() != 10){                              // Word must have 10 characters
                    resGame = new GameMsg("", 4);
                    iter--;
                } else if(!wp.inDb(guessedWord)){                                   // Word not in dictionary
                    resGame = new GameMsg("", 3);
                    iter--;
                } else {
                    resGame = new GameMsg(buildRes(secretWord, guessedWord), 0);    // Normal game
                }
                objectOutputStream.writeObject(resGame);
            } catch (IOException | ClassNotFoundException | WrongMessageException e) { System.err.println("Error: " + e.getMessage()); }
        }
    }

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

}
