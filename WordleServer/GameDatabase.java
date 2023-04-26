import java.util.*;

public class GameDatabase {

    List<DataGame> games = new ArrayList<DataGame>();

    /**
     * Adds a game to the list of games, synchronizing access to the list to ensure thread safety.
     * @param game The game to add to the list.
     */
    public void add(DataGame game) {
        // Synchronize access to the list to ensure that only one thread is modifying it at a time.
        synchronized(this){
            games.add(game);
        }
    }


    /**
     * Checks if a given username exists in the list of games.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public Boolean usrExist(String username) {
        // synchronize to prevent race conditions
        synchronized(this){
            for(DataGame game : games){
                if(game.getUsername().equals(username)){
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * Retrieves the DataGame object associated with the given username.
     * If no such object exists, returns null.
     *
     * @param username the username associated with the DataGame object to retrieve
     * @return the DataGame object associated with the given username, or null if not found
     */
    public DataGame getDataGame(String username) {
        // Synchronize on this object to ensure thread safety
        synchronized(this){
            for(DataGame game : games){
                if(game.getUsername().equals(username)){
                    return game;
                }
            }
            return null;
        }
    }


    /**
     * Returns a formatted String representation of the object.
     *
     * @return a String representation of the object.
     */
    @Override
    public String toString() {
        // Synchronizes on the instance of the object to avoid concurrent modification
        synchronized(this){
            String s = "";
            // Iterates over each DataGame object in the 'games' list
            for (DataGame dg : games) {
                // Appends the String representation of the DataGame object to the 's' variable
                s += dg.toString() + "\n";
            }
            // Returns the formatted String representation of the object
            return s;
        }
    }


}
