public class PlayMsg extends Messaggio{
    
    private String username;

    public PlayMsg(String user){
        this.username = user;
        super.setType(MessageType.PLAY);
    }

    public String getUsername() {
        return this.username;
    }

}