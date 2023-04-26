import java.util.*;

public class StatisticMsg extends Messaggio{

    private String username;
    private int numPartite;
    private int percVinte;
    private int lastWinStreak;
    private int longestWinStreak;
    private List<String> guessDistribution;
    
    public StatisticMsg(DataGame dg){
        super.setType(MessageType.STATISTICS);
        this.username = dg.getUsername();
        this.numPartite = dg.partiteGiocate();
        this.percVinte = dg.getPercentualeWin();
        this.lastWinStreak = dg.lastWinStreak();
        this.longestWinStreak = dg.longestWinStreak();
        this.guessDistribution = buildGuessDistribution(dg);
    }

    /**
     * Builds a guess distribution list for a given DataGame object.
     * The guess distribution list contains strings that represent the number of guesses
     * that have occurred for each number in the game's code.
     *
     * @param dg the DataGame object to build the guess distribution for
     * @return a List of Strings representing the guess distribution
     */
    private List<String> buildGuessDistribution(DataGame dg){
        List<Integer> gd = dg.getGuessDistribution();
        List<String> guessDistribution = new ArrayList<String>(); // this will store the guess distribution strings
        for(int i = 0; i < gd.size(); i++){
            String s = "";
            for(int j = 0; j < gd.get(i); j++){
                s += "*"; // add a "*" character for each guess that has occurred for this esito
            }
            guessDistribution.add(s);
        }
        return guessDistribution;
    }


    /**
     * Returns the username associated with this object.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Returns a string representation of the user's statistics for a game.
     * Includes username, number of games played, percentage won, longest and most recent win streaks, and guess distribution.
     *
     * @return a string representation of the user's statistics for a game
     */
    @Override
    public String toString() {
        String s = "user: " + username
                + "\n" + "numero partite: " + numPartite 
                + "\n" + "percentuale vinte: " + percVinte 
                + "\n" + "ultimo streak: " + lastWinStreak 
                + "\n" + "longest streak: " + longestWinStreak 
                + "\n" + "guess distribution:\n";
        int i = 1;
        for(String g : guessDistribution){
            s += String.format("%-2s |%s\n", i, g);
            i++;
        }

        return s;
    }


}
