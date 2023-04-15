

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;
import com.google.gson.stream.*;

public class TestJson {
    
    public static void main(String[] args) {
        Database db = new Database();
        Utente giulio = new Utente("giulio", "pw");
        Utente giovanni = new Utente("giovanni", "pw2");
        Utente marco = new Utente("marco", "pw3");
        Utente paolo = new Utente("paolo", "umpa");
        Utente mirco = new Utente("mirco", "pwForte");
        giovanni.changeLogging(true);
        db.add(giulio);
        db.add(giovanni);
        db.add(marco);
        db.add(paolo);
        db.add(mirco);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String dbJson = gson.toJson(db);
        System.out.println(dbJson);

        // Now we write the string to a file
        try (FileWriter fileWriter = new FileWriter("./WordleServer/utentiDB.json")) {
            fileWriter.write(dbJson);
            System.out.println("JSON successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now we try to read from the JSON file in an efficient way
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader("./WordleServer/utentiDB.json"));
            reader.beginObject();
            reader.nextName();
            reader.beginArray();
            while(reader.hasNext()) {
                Utente ut = gson.fromJson(reader, Utente.class);
                if(ut.isLogged() == true) {
                    System.out.println(ut);
                }
            }
            reader.endArray();
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now we try to add to the JSON file in an efficient way
        Utente novizio = new Utente("novizo", "speranza");
        novizio.changeLogging(true);
        db.add(novizio);
        String db2Json = gson.toJson(db);
        try (FileWriter fileWriter = new FileWriter("./WordleServer/utentiDB.json")) {
            fileWriter.write(db2Json);
            System.out.println("JSON 2 successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
