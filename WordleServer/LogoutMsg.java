public class LogoutMsg extends Messaggio{
 
    private String username;

    public LogoutMsg(String username) {
        this.username = username;
        super.setType(MessageType.LOGOUT);
    }

    public String getUsername() {
        return this.username;
    }

}
