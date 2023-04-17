public class GameMsg extends Messaggio{
    
    String username;

    public GameMsg(String username) {
        super.setType(MessageType.GAME);
        this.username = username;
    }

}
