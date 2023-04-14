import java.util.ArrayList;
import java.util.List;

public class Database {

    private List<Utente> utenti = new ArrayList<>(); 

    public void add(Utente ut) {
        utenti.add(ut);
    }

}
