public class Utente {
    
    private String username;
    private String password;
    private Boolean logged;
    
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.logged = false;
    }

    public void changeLogging(Boolean logged) {
        this.logged = logged;
    }

    public String toString(){
        return this.username + this.password + this.logged.toString();
    }

    public String getUsername() {
        return username;
    }

    public Boolean isLogged() {
        return logged;
    }
}
