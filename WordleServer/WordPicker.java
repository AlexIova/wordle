import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordPicker {
    
    ArrayList<String> wordlist = new ArrayList<String>();
    
    String secretWord;

    public WordPicker(String pathWords){
        takeFrom(pathWords);
    }

    /**
     * Reads words from a file and adds them to a list in memory.
     *
     * @param pathWords the path to the file containing the words.
     */
    private void takeFrom(String pathWords){
        File file = new File(pathWords);
        try (Scanner scanner = new Scanner(file)) {
            // Read each line from the file.
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                wordlist.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Changes the secret word to a random word from the wordlist.
     */
    public void changeWord() {
        // Use synchronization to ensure thread safety
        synchronized(this) {
            Random rand = new Random();
            secretWord = wordlist.get(rand.nextInt(wordlist.size()));
        }
    }

    /**
     * Returns the secret word, synchronized to prevent race conditions.
     * 
     * @return the secret word
     */
    public String getSecretWord() {
        // Use synchronization to ensure thread safety
        synchronized (this) {
            return secretWord;
        }
    }


    /**
     * Checks if a given word is present in the wordlist.
     * 
     * @param word the word to search for
     * @return true if the word is in the wordlist, false otherwise
     */
    public boolean inDb(String word) {
        return wordlist.contains(word);
    }

}
