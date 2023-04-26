public class StatReqMsg extends Messaggio{

    private String username;

    public StatReqMsg(String username) {
        super.setType(MessageType.REQ_STAT);
        this.username = username;
    }

    /**
     * Returns the username associated with this object.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    
}