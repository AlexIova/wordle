import java.util.*;

public class DataGame {
    
    private String username;
    private List<Partita> partite = new ArrayList<Partita>();

    public DataGame(String username) {
        this.username = username;
    }

    /**
     * Add a new partita to the list of partite.
     *
     * @param partita the partita to be added
     */
    public void addPartita(Partita partita) {
        partite.add(partita);
    }


    /**
     * Gets the username associated with this object.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }


    /**
     * Returns the number of games played.
     * @return the number of games played
     */
    public int partiteGiocate() {
        return partite.size();
    }



    /**
     * Returns the percentage of games won by the player.
     *
     * @return the percentage of games won as an integer
     */
    public int getPercentualeWin() {
        int vinte = 0;
        int giocate = partiteGiocate();
        for (Partita partita : partite) {
            if (partita.getEsito()) {
                vinte++;
            }
        }
        return (vinte * 100) / giocate; 
    }


    /**
     * Returns the length of the last win streak in the list of games.
     *
     * @return the length of the last win streak
     */
    public int lastWinStreak() {
        int lastWinStreak = 0;

        for (int i = partite.size() - 1; i >= 0; i--) {
            // If the game was not a win, exit the loop
            if (!partite.get(i).getEsito()) {
                break;
            }
            // Increase the length of the win streak
            lastWinStreak++;
        }
        return lastWinStreak;
    }


    /**
     * Returns the length of the longest winning streak in the list of games.
     *
     * @return The length of the longest winning streak.
     */
    public int longestWinStreak(){
        // Create a list to store the length of each winning streak.
        List<Integer> streakList = new ArrayList<Integer>();
        int streak = 0;
        for(int i = 0; i < partite.size(); i++){
            if(partite.get(i).getEsito()){
                streak++;
            } else {
                streakList.add(streak);
                streak = 0;
            }
        }
        streakList.add(streak);
        return Collections.max(streakList);
    }


    /**
     * Returns an ArrayList containing the distribution of successful guesses.
     *
     * @return the guess distribution
     */
    public ArrayList<Integer> getGuessDistribution() {
        // Initialize the guess distribution with 12 zeros.
        ArrayList<Integer> guessDistribution = new ArrayList<Integer>(Collections.nCopies(12, 0));
        for (Partita partita : partite) {
            // If the game was won, increment the count for the corresponding guess.
            if (partita.getEsito()) {
                int guess = partita.getTentativo();
                guessDistribution.set(guess, guessDistribution.get(guess) + 1);
            }
        }
        return guessDistribution;
    }


    /**
     * Checks if a word has already been played in any ongoing game.
     * @param word the word to check
     * @return true if the word has already been played, false otherwise
     */
    public Boolean alreadyPlayed(String word){
        for(Partita partita : partite){
            if(partita.getSecretWord().equals(word)){
                return true;
            }
        }
        return false;
    }


}
