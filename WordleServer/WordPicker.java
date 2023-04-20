import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordPicker {
    
    ArrayList<String> wordlist = new ArrayList<String>();
    
    String secretWord;

    public WordPicker(String pathWords){
        takeFrom(pathWords);
    }

    private void takeFrom(String pathWords){
        File file = new File(pathWords);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                wordlist.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public synchronized void changeWord(){
        Random rand = new Random();
        secretWord = wordlist.get(rand.nextInt(wordlist.size()));
    }

    public synchronized String getSecretWord(){
        return secretWord;
    }

    public boolean inDb(String word){
        return wordlist.contains(word);
    }

}
