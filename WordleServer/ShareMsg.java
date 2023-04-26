public class ShareMsg extends Messaggio{

    private String username;

    public ShareMsg(String user){
        this.username = user;
        super.setType(MessageType.SHARE);
    }

    /**
     * Returns the username associated with this object.
     * 
     * @return the username as a String
     */
    public String getUsername() {
        return this.username;
    }

}
