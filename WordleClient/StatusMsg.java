public class StatusMsg extends Messaggio {

    private int code;
    private String msg;

    public StatusMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
        super.setType(MessageType.STATUS);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
