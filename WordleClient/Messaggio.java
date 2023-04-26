import java.io.Serializable;

public class Messaggio implements Serializable{

    private MessageType type;

    /**
     * Returns the message type associated with this message.
     *
     * @return the message type
     */
    public MessageType getType() {
        return type;
    }


    /**
     * Returns a string representation of this object. Used only for debug purposes.
     *
     * @return a string containing the type of this Messaggio object
     */
    @Override
    public String toString() {
        // Use string concatenation to build the result
        return "Messaggio{" +
                "type=" + type +
                '}';
    }


    /**
     * Sets the type of the message.
     *
     * @param tipo the MessageType to set
     */
    protected void setType(MessageType tipo) {
        this.type = tipo;
    }


}
