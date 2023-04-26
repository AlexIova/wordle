public class RegisterMsg extends Messaggio {
    
    private String username;
    private String password;

    public RegisterMsg(String username, String password) {
        this.username = username;
        this.password = password;
        super.setType(MessageType.REGISTER);
    }

    /**
     * Returns the username associated with this object.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }


    /**
     * This method returns the password.
     * @return The password as a String.
     */
    public String getPassword() {
        return password;
    }

}
