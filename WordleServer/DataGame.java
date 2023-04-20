import java.util.*;

public class DataGame {
    
    private String username;
    private List<Partita> partite = new ArrayList<Partita>();

    public DataGame(String username) {
        this.username = username;
    }

    public void addPartita(Partita partita) {
        partite.add(partita);
    }

    public String getUsername() {
        return username;
    }

    public int partiteGiocate(){
        return partite.size();
    }

    public int getPercentualeWin(){
        int vinte = 0;
        int giocate = partiteGiocate();
        for(Partita partita : partite){
            if(partita.getEsito()){
                vinte++;
            }
        }
        return (vinte * 100) / giocate;
    }

    public int lastWinStreak(){
        /* Get elements of this.partite starting from last one with a for loop */
        int lastWinStreak = 0;
        for(int i = partite.size() - 1; i >= 0; i--){
            if(!partite.get(i).getEsito()){
                break;
            }
            lastWinStreak++;
        }
        return lastWinStreak;
    }

    public int longestWinStreak(){
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
        return Collections.max(streakList);
    }

    public ArrayList<Integer> getGuessDistribution(){
        ArrayList<Integer> guessDistribution = new ArrayList<Integer>(12);
        for(Partita partita : partite){
            guessDistribution.set(partita.getTentativo(), guessDistribution.get(partita.getTentativo()) + 1);
        }
        return guessDistribution;
    }

}
