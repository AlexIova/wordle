public class GameMsg extends Messaggio{
    
    private String res;
    private int code;

    /*
     * Code explaination:
     * 0 - OK
     * 1 - Game won
     * 2 - Game lost
     * 3 - Word not in dictionary
     * 4 - word not with 10 characters
     */

    public GameMsg(String result, int code) {
        super.setType(MessageType.GAME);
        this.res = result;
        this.code = code;
    }


    public String getRes() {
        return res;
    }

    public int getCode() {
        return code;
    }

}
