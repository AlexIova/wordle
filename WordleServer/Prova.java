import com.google.gson.*;

public class Prova {
    
    public static void main(String[] args) {
        Database db = new Database();
        Utente giulio = new Utente("giulio", "pw");
        Utente giovanni = new Utente("giovanni", "pw2");
        Utente marco = new Utente("marco", "pw3");
        db.add(giulio);
        db.add(giovanni);
        db.add(marco);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String dbJson = gson.toJson(db);
        System.out.println(dbJson);
    }

}
