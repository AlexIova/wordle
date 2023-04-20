public class StatReqMsg extends Messaggio{

    private String username;

    public StatReqMsg(String username) {
        super.setType(MessageType.REQ_STAT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
}