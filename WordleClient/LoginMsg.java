public class LoginMsg extends Messaggio{
    
    private String username;
    private String password;

    public LoginMsg(String username, String password) {
        this.username = username;
        this.password = password;
        super.setType(MessageType.LOGIN);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}
