import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
public class Main {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static void main(String[] args) throws IOException, InterruptedException {
        Word word = new Word();
        ArrayList<String> previousAnswers = nullifyArrayList();
        GameBoard gameBoard = new GameBoard(previousAnswers);
        playRound(word, gameBoard);
    }
    public static ArrayList nullifyArrayList() {
        ArrayList<String> tmp = new ArrayList<>();
        int turns = 7;
        for (int i = 0; i < turns; i++) {
            tmp.add("");
        }
        return tmp;
    }
    public static ArrayList nullifyLetterArrayList() {
        ArrayList<String> tmp = new ArrayList<>();
        int turns = 100;
        for (int i = 0; i < turns; i++) {
            tmp.add(" ");
        }
        return tmp;
    }
    private static void playRound(Word word, GameBoard gameBoard) {

        ArrayList<String> green = nullifyLetterArrayList();
        ArrayList<String> yellow = nullifyLetterArrayList();
        int turn = 0;
        Gameplay gameplay = new Gameplay();

        while (turn < 6) {
            String guess = gameplay.getUserGuess(turn);

            if (guess.equalsIgnoreCase("devmode")) {

                System.out.println(word.word);

            } else if (guess.length() != 5) {

                System.out.println("Error: Word must contain 6 letters.");

            } else if (guess.equalsIgnoreCase(word.word)) {

                System.out.println(ANSI_BLACK + ANSI_GREEN_BACKGROUND + "You guessed correct!" + ANSI_RESET);
                System.out.printf(ANSI_BLACK + ANSI_GREEN_BACKGROUND + "the word was %s", word.word + ANSI_RESET);
                break;

            } else {
                turn++;
                gameBoard.previousAnswers.set(turn, guess);

            for (int i = 0; i < guess.length(); i++) {
                if (guess.charAt(i) == word.word.charAt(i)) {
                    green.set(i, ANSI_BLACK + ANSI_GREEN_BACKGROUND + guess.charAt(i) + ANSI_RESET);

                } else if (!word.word.contains(String.valueOf(guess.charAt(i)))) {
                } else yellow.set(i, ANSI_YELLOW_BACKGROUND + ANSI_BLACK + (guess.charAt(i)) + ANSI_RESET);

            }
            Set<String> y = new LinkedHashSet<>(yellow);
            Set<String> g = new LinkedHashSet<>(green);
            y.remove(g);
            System.out.println("\n");
            gameBoard.showBoard(green, y, gameBoard.previousAnswers);
            }
        }
        System.out.printf("\nThe word was %s", word.word);
    }
    static class GameBoard {

        ArrayList<String> previousAnswers;

        public GameBoard(ArrayList<String> previousAnswers) {
            this.previousAnswers = previousAnswers;
        }

        public void showBoard(ArrayList<String> greenLetters, Set<String> yellowLetters, ArrayList<String> previousAnswers) {
            System.out.println("⎯⎯⎯⎯⎯⎯");
            for (String p : yellowLetters) {
                System.out.print(p + " ");
            }
            System.out.println("\n_ _ _ _ _");
            for (String l : greenLetters) {
                System.out.print(l + " ");
            }

            for (String answer : previousAnswers) {
                System.out.print("│ " + answer+ "\n⎯⎯⎯⎯⎯⎯\n");
            }
        }
    }
    static class Word {
        String word;

        public Word() throws IOException, InterruptedException {
            this.word = genWord2();
        }

//OLD WORD GEN API
//        public static String genWord() throws IOException {
//            String inline = "";
//            String word = "";
//
//
//            URL url = new URL("https://random-words5.p.rapidapi.com/getMultipleRandom?count=5&wordLength=5&minLength=5&maxLength=5");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.connect();
//
//            int responsecode = conn.getResponseCode();
//
//
//            if (responsecode != 200) {
//                throw new RuntimeException("HttpResponseCode: " + responsecode);
//            } else {
//
//                Scanner scanner = new Scanner(url.openStream());
//
//                //Write all the JSON data into a string using a scanner
//                while (scanner.hasNext()) {
//                    inline += scanner.nextLine();
//                }
//                word = inline.substring(2, inline.length() - 2);
//
//                //Close the scanner
//                scanner.close();
//            }
//            return word;
//        }

        public static String genWord2() throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://random-words5.p.rapidapi.com/getMultipleRandom?count=1&wordLength=5"))
                    .header("X-RapidAPI-Key", "838baab396mshb1cd6e3e525fd30p13665djsna6c1a9cb9b95")
                    .header("X-RapidAPI-Host", "random-words5.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String tmp = response.body();
            String word = tmp.substring(2,tmp.length()-2);
            return word;
        }


    }
    public static class Gameplay {
        Scanner s = new Scanner(System.in);
        String getUserGuess(int turns) {
            System.out.println("Guess a 5 letter word:");
            System.out.printf("Turn:%s of 6\n",turns);
            String userGuess = s.nextLine();
            return userGuess;
        }
    }
}