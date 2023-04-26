public class Utente {
    
    private String username;
    private String password;
    private Boolean logged;
    
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.logged = false;
    }

    /**
     * Changes the logging setting for this object.
     *
     * @param logged What to set logging state to.
     */
    public void changeLogging(Boolean logged) {
        this.logged = logged;
    }


    /**
     * Returns a string representation of the current object.
     * The string contains the username, password, and logged status separated by tabs.
     *
     * @return a string representation of the current object
     */
    @Override
    public String toString() {
        return this.username + "\t " + this.password + "\t " + this.logged.toString();
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
     * Returns a Boolean indicating if the user is logged in.
     *
     * @return Boolean indicating if the user is logged in.
     */
    public Boolean isLogged() {
        return logged;
    }


    /**
     * Returns the password string.
     *
     * @return the password string
     */
    public String getPassword() {
        return password;
    }
    
}
