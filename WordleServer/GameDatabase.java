import java.util.*;

public class GameDatabase {

    List<DataGame> games = new ArrayList<DataGame>();

    public void add(DataGame game) {
        synchronized(this){
            games.add(game);
        }
    }

}
