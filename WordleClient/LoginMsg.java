public class LoginMsg extends Messaggio{
    
    private String username;
    private String password;

    public LoginMsg(String username, String password) {
        this.username = username;
        this.password = password;
        super.setType(MessageType.LOGIN);
    }

    /**
     * Returns the username associated with this object.
     *
     * @return a String representing the username
     */
    public String getUsername() {
        return this.username;
    }


    /**
    * Returns the password for this object.
    *
    * @return the password for this object.
    */
    public String getPassword() {
        return this.password;
    }


}
