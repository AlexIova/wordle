import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;

public class ExitHandler implements Runnable{
    
    UsrDatabase usrDB;
    GameDatabase gameDB;
    String nameUsrDB;
    String nameGameDB;

    public ExitHandler(UsrDatabase usrDB, GameDatabase gameDB, String nameUsrDB, String nameGameDB) {
        this.usrDB = usrDB;
        this.gameDB = gameDB;
        this.nameUsrDB = nameUsrDB;
        this.nameGameDB = nameGameDB;
    }

    @Override
    public void run() {
        // Write usr database
        Gson gsonUsr = new GsonBuilder().setPrettyPrinting().create();
        String usrDbJson = gsonUsr.toJson(usrDB);
        try (FileWriter fileWriter = new FileWriter(nameUsrDB)) {
            fileWriter.write(usrDbJson);
            System.out.println("JSON successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Write game database
        Gson gsonGame = new GsonBuilder().setPrettyPrinting().create();
        String gameDbJson = gsonGame.toJson(gameDB);
        try (FileWriter fileWriter = new FileWriter(nameGameDB)) {
            fileWriter.write(gameDbJson);
            System.out.println("JSON successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
