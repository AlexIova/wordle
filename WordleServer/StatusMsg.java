public class StatusMsg extends Messaggio {

    private int code;
    private String msg;

    public StatusMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
        super.setType(MessageType.STATUS);
    }

    /**
     * Returns the code value used to indicate the type of reply
     *
     * @return The code value.
     */
    public int getCode() {
        return code;
    }

    
    /**
     * Returns the message stored in the object. Never used in code, just in case of debug.
     *
     * @return the message
     */
    public String getMsg() {
        return msg;
    }

}
