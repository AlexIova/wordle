public class ShareMsg extends Messaggio{

    private String username;

    public ShareMsg(String user){
        this.username = user;
        super.setType(MessageType.SHARE);
    }

    public String getUsername() {
        return this.username;
    }

}
