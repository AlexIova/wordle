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

    public static void handleLogin(Messaggio msg, UsrDatabase db, ObjectOutputStream objectOutputStream) {
        LoginMsg loginMsg = (LoginMsg) msg;
        String username = loginMsg.getUsername();
        String password = loginMsg.getPassword();
        try{
            if (db.usrExist(username)){
                Utente user = db.getUser(username);
                if (user.getPassword().equals(password)){
                    StatusMsg statusMsg = new StatusMsg(0, "Login successfull");
                    user.changeLogging(true);
                    objectOutputStream.writeObject(statusMsg);
                } else {
                    StatusMsg statusMsg = new StatusMsg(1, "Wrong password");
                    objectOutputStream.writeObject(statusMsg);
                }
            } else {
                StatusMsg statusMsg = new StatusMsg(1, "Username not found");
                objectOutputStream.writeObject(statusMsg);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
