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

    /**
     * Returns the username associated with this object.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the current guessed word.
     *
     * @return the current guessed word as a String.
     */
    public String getGuessedWord() {
        return this.guessedWord;
    }


}