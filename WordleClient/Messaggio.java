import java.io.Serializable;

public class Messaggio implements Serializable{

    private MessageType type;

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Messaggio{" +
                "type=" + type +
                '}';
    }

    protected void setType(MessageType tipo){
        this.type = tipo;
    }

}
