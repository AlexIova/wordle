import java.util.ArrayList;
import java.util.List;

public class UsrDatabase {

    private List<Utente> utenti = new ArrayList<Utente>(); 

    /**
     * Adds a new Utente to the list of utenti, with thread-safety.
     * 
     * @param ut The Utente to add to the list.
     */
    public void add(Utente ut) {
        // Synchronize the block of code to ensure thread-safety
        synchronized(this){
            utenti.add(ut);
        }
    }

    /**
     * Checks if the given username exists in the list of users.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    public Boolean usrExist(String username) {
        synchronized(this){
            for (Utente ut : utenti) {
                if (ut.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * Returns the user associated with the given username,
     * or null if no such user exists.
     *
     * @param username the username to look up
     * @return the user associated with the given username,
     *         or null if no such user exists
     */
    public Utente getUser(String username) {
        // synchronize on 'this' to ensure thread safety
        synchronized(this){
            for (Utente ut : utenti) {
                if (ut.getUsername().equals(username)) {
                    return ut;
                }
            }
            // return null if no matching user was found
            return null;
        }
    }


    /**
     * Returns a string representation of the list of Utente objects.
     * Each Utente object's string representation is separated by a newline character.
     * 
     * @return a string representation of the list of Utente objects
     */
    @Override
    public String toString() {
        String s = "";
        for (Utente ut : utenti) {
            s += ut.toString() + "\n";
        }
        return s;
    }

}
