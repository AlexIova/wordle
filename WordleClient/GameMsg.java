public class GameMsg extends Messaggio{
    
    private String res;
    private int code;

    public GameMsg(String result, int code) {
        super.setType(MessageType.GAME);
        this.res = result;
        this.code = code;
    }

    /**
     * Returns the value of the 'res' variable.
     *
     * @return the value of the 'res' variable as a String.
     */
    public String getRes() {
        return res;
    }


    /**
     * Returns the value of the 'code' variable.
     *
     * @return the value of the 'code' variable
     */
    public int getCode() {
        return code;
    }

}
