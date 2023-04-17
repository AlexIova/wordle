import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;

public class ExitHandler implements Runnable{
    
    UsrDatabase db;

    public ExitHandler(UsrDatabase db) {
        this.db = db;
    }

    @Override
    public void run() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String dbJson = gson.toJson(db);
        try (FileWriter fileWriter = new FileWriter("./WordleServer/utentiDB.json")) {
            fileWriter.write(dbJson);
            System.out.println("JSON successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
