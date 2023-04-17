public class RegisterMsg extends Messaggio {
    
    private String username;
    private String password;

    public RegisterMsg(String username, String password) {
        this.username = username;
        this.password = password;
        super.setType(MessageType.REGISTER);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
