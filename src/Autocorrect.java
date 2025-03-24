import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
        // Create an ArrayList to store words within threshold edit distance
        ArrayList <String> viableWords = new ArrayList<>();

        // Iterate through dictionary to find words within threshold distance
        for (int i = 0; i < dictionary.length; i++){
            int distance  = levenshteinDistance(typed, dictionary[i]);
            // If we find an exact match, return only that word immediately
            if (distance == 0) {
                return new String[]{dictionary[i]};
            }
            // Calculate edit distance and add word if within threshold
            else if (distance <= threshold){
                viableWords.add(dictionary[i]);
            }
        }

        // Sort the viable words using insertion sort
        for (int i = 1; i < viableWords.size(); i++) {
            String key = viableWords.get(i);
            int keyDist = levenshteinDistance(typed, key);
            int j = i - 1;

            // Move elements that have greater distance or same distance but alphabetically later
            while (j >= 0) {
                String current = viableWords.get(j);
                int currentDist = levenshteinDistance(typed, current);

                if (currentDist > keyDist || (currentDist == keyDist && current.compareTo(key) > 0)) {
                    // Shift the current word forward
                    viableWords.set(j + 1, current);
                    j--;
                }
                else {
                    break;
                }
            }
            // Place the key word in its sorted position
            viableWords.set(j + 1, key);
        }
        return viableWords.toArray(new String[0]);

    }


    private static int levenshteinDistance(String one, String two){
        one = one.toLowerCase();
        two = two.toLowerCase();

        int[][] dynamic = new int[one.length() + 1][two.length() + 1];

        // Initialize first row and column with incremental values
        for (int i = 0; i <= one.length(); i++){
            dynamic[i][0] = i;
        }
        for (int j = 0; j <= two.length(); j++){
            dynamic[0][j] = j;
        }
        // Fill the dynamic programming table
        for (int i = 1; i <= one.length(); i++){
            for (int j = 1; j <= two.length(); j++){
                // If characters match, no additional cost
                if (one.charAt(i - 1) == two.charAt(j - 1)){
                    dynamic[i][j] = dynamic[i - 1][j - 1];
                }
                else{
                    // Take minimum of deletion, insertion, or substitution costs
                    dynamic[i][j] = Math.min(Math.min(dynamic[i -1][j] + 1, dynamic[i][j - 1] + 1), dynamic[i - 1][j - 1] + 1);
                }

            }
        }

        // Return the Levenshtein distance
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

    public static void main(String[] args){
        // Load dictionary and determine threshold size
        String[] dictionary = loadDictionary("large");
        int threshold = 2;

        // Create Autocorrect instance
        Autocorrect autocorrect = new Autocorrect(dictionary, threshold);

        // Create Scanner for user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a word:");

        String input = scanner.nextLine().trim();

        while(!input.equals("")){

            String[] suggestions = autocorrect.runTest(input);

            if (suggestions.length == 0) {
                System.out.println("No matches found.");
            }
            else if (suggestions.length == 1 && levenshteinDistance(input, suggestions[0]) == 0){
                System.out.println("Already a valid word");
            }
            else {
                System.out.println("Suggestions:");
                for (String suggestion: suggestions){
                    System.out.println(suggestion);
                }

            }
            System.out.println("Enter a word:");
            input = scanner.nextLine().trim();

        }
        scanner.close();

    }


}