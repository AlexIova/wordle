import java.util.*;

public class Partita {
    
    private List<String> gameMatrix = new ArrayList<String>();
    private Boolean esito;
    private int tentativo = -1;

    public Partita(List<String> matrix){
        for(String row : matrix){
            this.gameMatrix.add(row);
        }
        this.esito = calcEsito(this.gameMatrix);
        if(this.esito){
            this.tentativo = calcTentativo(this.gameMatrix);
        }
    }

    private Boolean calcEsito(List<String> matrix){
        for(String row: matrix){
            if(row.equals("++++++++++++")){
                return true;
            }
        }
        return false;
    }

    private int calcTentativo(List<String> matrix){
        if(matrix.size() < 12){
            return matrix.size();
        }
        return 12;
    }

    public Boolean getEsito(){
        return this.esito;
    }

    public int getTentativo(){
        return this.tentativo;
    }
        
}

