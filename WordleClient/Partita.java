import java.util.*;

public class Partita {
    
    private List<String> gameMatrix;
    private Boolean esito;
    private int tentativo = -1;
    private String secretWord;

    public Partita(List<String> matrix, String secretWord) {
        this.secretWord = secretWord;
        this.gameMatrix = matrix;
        this.esito = calcEsito(this.gameMatrix);
        if(this.esito){
            this.tentativo = calcTentativo(this.gameMatrix);
        }
    }

    /**
     * Determines if the given matrix contains a row with all "+" characters meaning it's the last one thus all letters guessed.
     * @param matrix A list of strings representing the matrix.
     * @return True if the matrix contains a row with all "+" characters, false otherwise.
     */
    private Boolean calcEsito(List<String> matrix) {
        for (String row : matrix) {
            if (row.equals("++++++++++")) {
                return true;
            }
        }
        return false;
    }


    /**
     * Calculates the number of attempts made in a game of Connect Four based on the current state of the game board.
     * 
     * @param matrix The game board represented as a list of strings, where each string is a row of the board.
     * @return The number of attempts made in the game so far.
     */
    private int calcTentativo(List<String> matrix){
        if(matrix.size() < 12){
            return matrix.size() - 1;           // Last row is the completed one
        }
        return 12;
    }


    /**
     * Returns the value of the esito field.
     * @return the value of the esito field.
     */
    public Boolean getEsito() {
        return this.esito;
    }


    /**
     * Returns the value of the 'tentativo' instance variable.
     *
     * @return the value of the 'tentativo' instance variable
     */
    public int getTentativo() {
        return this.tentativo;
    }


    /**
     * Returns the secret word.
     * 
     * @return the secret word as a String
     */
    public String getSecretWord() {
        // Return the value of the secretWord instance variable
        return this.secretWord;
    }


}

