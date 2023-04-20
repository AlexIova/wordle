import java.util.*;

public class GameDatabase {

    List<DataGame> games = new ArrayList<DataGame>();

    public void add(DataGame game) {
        synchronized(this){
            games.add(game);
        }
    }

    public Boolean usrExist(String username) {
        synchronized(this){
            for(DataGame game : games){
                if(game.getUsername().equals(username)){
                    return true;
                }
            }
            return false;
        }
    }

    public DataGame getDataGame(String username) {
        synchronized(this){
            for(DataGame game : games){
                if(game.getUsername().equals(username)){
                    return game;
                }
            }
            return null;
        }
    }

    @Override
    public String toString() {
        synchronized(this){
            String s = "";
            for (DataGame dg : games) {
                s += dg.toString() + "\n";
            }
            return s;
        }
    }

}
