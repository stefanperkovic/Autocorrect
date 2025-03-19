import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Stefan Perkovic
 */
public class Autocorrect {
    private String[] dictionary;
    private int threshold;
    private int nGramSize = 3;

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {
        this.dictionary = words;
        this.threshold = threshold;

    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        ArrayList <String> viableWords = new ArrayList<>();

        for (int i = 0; i < dictionary.length; i++){
            if (levenshteinDistance(typed, dictionary[i]) <= threshold){
                viableWords.add(dictionary[i]);
            }
        }


        for (int i = 1; i < viableWords.size(); i++) {
            String key = viableWords.get(i);
            int keyDist = levenshteinDistance(typed, key);
            int j = i - 1;

            while (j >= 0 && levenshteinDistance(typed, viableWords.get(j)) > keyDist) {
                viableWords.set(j + 1, viableWords.get(j));
                j--;
            }
            viableWords.set(j + 1, key);
        }
        return viableWords.toArray(new String[0]);

    }


    private int levenshteinDistance(String one, String two){
        one = one.toLowerCase();
        two = two.toLowerCase();


        int[][] dynamic = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i <= one.length(); i++){
            dynamic[i][0] = i;
        }
        for (int j = 0; j <= two.length(); j++){
            dynamic[0][j] = j;
        }
        for (int i = 1; i <= one.length(); i++){
            for (int j = 1; j <= two.length(); j++){
                if (one.charAt(i - 1) == two.charAt(j - 1)){
                    dynamic[i][j] = dynamic[i - 1][j - 1];
                }
                else{
                    dynamic[i][j] = Math.min(Math.min(dynamic[i -1][j] + 1, dynamic[i][j - 1] + 1), dynamic[i - 1][j - 1] + 1);
                }

            }
        }


        return dynamic[one.length()][two.length()];
    }

    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}