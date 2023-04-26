public class LogoutMsg extends Messaggio{
 
    private String username;

    public LogoutMsg(String username) {
        this.username = username;
        super.setType(MessageType.LOGOUT);
    }

    /**
     * Returns the username associated with this object.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }


}
