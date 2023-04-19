public class PlayMsg extends Messaggio{
    
    private String username;
    private String guessedWord;

    public PlayMsg(String user){
        this.username = user;
        super.setType(MessageType.PLAY);
    }

    public PlayMsg(String user, String word){
        this.username = user;
        this.guessedWord = word;
        super.setType(MessageType.PLAY);
    }

    public String getUsername() {
        return this.username;
    }

    public String getGuessedWord(){
        return this.guessedWord;
    }

}