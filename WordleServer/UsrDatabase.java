import java.util.ArrayList;
import java.util.List;

public class UsrDatabase {

    private List<Utente> utenti = new ArrayList<>(); 

    public void add(Utente ut) {
        synchronized(this){
            utenti.add(ut);
        }
    }

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

    @Override
    public String toString() {
        String s = "";
        for (Utente ut : utenti) {
            s += ut.toString() + "\n";
        }
        return s;
    }

}
