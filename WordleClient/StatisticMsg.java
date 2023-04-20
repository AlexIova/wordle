import java.util.*;

public class StatisticMsg extends Messaggio{

    private int numPartite;
    private int percVinte;
    private int lastWinStreak;
    private int longestWinStreak;
    private List<String> guessDistribution;
    
    public StatisticMsg(DataGame dg){
        super.setType(MessageType.STATISTICS);
        this.numPartite = dg.partiteGiocate();
        this.percVinte = dg.getPercentualeWin();
        this.lastWinStreak = dg.lastWinStreak();
        this.longestWinStreak = dg.longestWinStreak();
        this.guessDistribution = buildGuessDistribution(dg);
    }

    private List<String> buildGuessDistribution(DataGame dg){
        List<Integer> gd = dg.getGuessDistribution();
        List<String> guessDistribution = new ArrayList<String>();
        for(int i = 0; i < gd.size(); i++){
            String s = "";
            for(int j = 0; j < gd.get(i); j++){
                s += "*";
            }
            guessDistribution.add(s);
        }
        return guessDistribution;
    }

    // private int getNumPartite() {
    //     return numPartite;
    // }

    // private int getPercVinte() {
    //     return percVinte;
    // }

    // private int getLastWinStreak() {
    //     return lastWinStreak;
    // }

    // private int getLongestWinStreak() {
    //     return longestWinStreak;
    // }

    // private List<String> getGuessDistribution() {
    //     return guessDistribution;
    // }

    @Override
    public String toString() {
        String s = "numero partite: " + numPartite 
                + "\n" + "percentuale vinte: " + percVinte 
                + "\n" + "ultimo streak: " + lastWinStreak 
                + "\n" + "longest streak: " + longestWinStreak 
                + "\n" + "guess distribution:\n";
        int i = 1;
        for(String g : guessDistribution){
            s += i + "|" + g + "\n";
            i++;
        }

        return s;
    }

}
