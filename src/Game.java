package src;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Constants {
    private int letters_score;
    private int numbers_score;
    private int total_games_played;
    private int num_letters_games_played;
    private int num_numbers_games_played;
    static Random random = new Random();
    
    public Game() {
        letters_score = 0;
        numbers_score = 0;
        total_games_played = 0;
        num_letters_games_played = 0;
        num_numbers_games_played = 0;
    }

    public void incrementNumGamesPlayed() { total_games_played++; }
    public void incrementNumLettersGamesPlayed() { num_letters_games_played++; }
    public void incrementNumNumbersGamesPlayed() { num_numbers_games_played++; }

    public int getLettersScore() { return letters_score; }
    public int getNumbersScore() { return numbers_score; }
    public int getTotalScore() { return letters_score + numbers_score; }
    public int getNumGamesPlayed() { return total_games_played; }
    public int getNumLettersGamesPlayed() { return num_letters_games_played; }
    public int getNumNumbersGamesPlayed() { return num_numbers_games_played; }

    public void increaseLettersScore(int pts) { letters_score += pts; }
    public void increaseNumbersScore(int pts) { numbers_score += pts; }

    //////////////////////////////////////// GAME TIME ////////////////////////////////////////////

    public void nextRound(String game_type) {
        DIFFICULTY difficulty = getDifficulty();
        int num_points = 0, num_items = 0;
        List<Character> char_list = null;
        List<Integer> numbers_list = null;

        switch (difficulty) {
            case EASY:
                num_items = EASY_NUM_ITEMS;
                num_points = EASY_REWARD_PTS;
                break;
            case MEDIUM:
                num_items = MEDIUM_NUM_ITEMS;
                num_points = MEDIUM_REWARD_PTS;
                break;
            case HARD:
                num_items = HARD_NUM_ITEMS;
                num_points = HARD_REWARD_PTS;
                break;
            default:
                System.out.println("Error: Unable to determine game difficulty. Difficulty is set to MEDIUM");
                num_items = MEDIUM_NUM_ITEMS;
        }

        // Randomly Determine Swapped Items
        int random_index1;
        int random_index2;

        do {
            random_index1 = random.nextInt(num_items);
            random_index2 = random.nextInt(num_items);
        } while (random_index1 == random_index2);

        // Keep Track of Correct Answer
        char correct_letter = ' ';
        int correct_number = 0;

        // Generate List / Randomly Swap 2 Items
        if("LETTERS".equals(game_type)) {
            char_list = new ArrayList<>();
            char_list = generateRandomList(num_items, game_type);
            Collections.swap(char_list, random_index1, random_index2);
            correct_letter = char_list.get(Math.min(random_index1, random_index2));
        }
        else if ("NUMBERS".equals(game_type)) {
            numbers_list = new ArrayList<>();
            numbers_list = generateRandomList(num_items, Constants.MAX_NUM);
            Collections.swap(numbers_list, random_index1, random_index2);
            correct_number = numbers_list.get(Math.min(random_index1, random_index2));
        }

        // Display List
        Main.printCenteredTitle("Round " + (getNumGamesPlayed() + 1), TOTAL_WIDTH, PADDING_CHAR_MENU);
        Main.printCenteredTitle(game_type + " (" + difficulty + ")", TOTAL_WIDTH, ' ');

        // Play Round
        boolean playerWins = false;

        if("LETTERS".equals(game_type)) {
            String instructions = "\nInstructions: Type in the first Letter in the list that's out of order\n";
            display(char_list, instructions);

            System.out.print(">> ");
            char user_input = System.console().readLine().charAt(0);
            playerWins = checkAnswer(user_input, correct_letter);
        }
        else if ("NUMBERS".equals(game_type)) {
            String instructions = "\nInstructions: Type in the first Number in the list that's out of order\n";
            display(numbers_list, instructions);

            System.out.print(">> ");
            String tmp = System.console().readLine();
            int user_input = Integer.parseInt(tmp);

            playerWins = checkAnswer(user_input, correct_number);
        }

        // Check for all game modes (for future additions)
        if(playerWins) {
            if(game_type == "LETTERS") {
                increaseLettersScore(num_points);
            }
            else if (game_type == "NUMBERS") {
                increaseNumbersScore(num_points);
            }
        }

        incrementNumGamesPlayed();
    }

    public DIFFICULTY getDifficulty() {
        Main.printCenteredTitle("DIFFICULTY SELECTION", TOTAL_WIDTH, PADDING_CHAR_SELECTION);
        String menu_options = "\nDifficulty Menu\n1. Easy\n2. Medium\n3. Hard";
        DIFFICULTY mode;

        do {
            mode = difficultySelection(menu_options);
            switch(mode) {
                case EASY:
                case MEDIUM:
                case HARD:
                    break;
                default:
                    System.out.println("\nError: Invalid Difficulty Selection");
            }
        } while (mode == DIFFICULTY.ERROR);
        return mode;
    }

    static DIFFICULTY difficultySelection(String menu_options) {
        int input;
        DIFFICULTY difficulty = DIFFICULTY.ERROR;

        try {
            System.out.println(menu_options);
            input = Integer.parseInt(System.console().readLine("Selection: "));

            if (input > 0 && input < MENU.values().length) {
                difficulty = DIFFICULTY.values()[input];
            }
        }
        catch (NumberFormatException ex) {
            input = MENU.values().length;
        }

        return difficulty;
    }

    // Overloaded method for letters game
    public static List<Character> generateRandomList(int size, String gameType) {
        List<Character> list = new ArrayList<>();
        if ("LETTERS".equals(gameType)) {
            while (list.size() < size) {
                char randomChar = (char) ('A' + random.nextInt(26));
                if (!list.contains(randomChar)) {
                    list.add(randomChar);
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    // Overloaded method for numbers game
    public static List<Integer> generateRandomList(int size, int maxNum) {
        List<Integer> list = new ArrayList<>();
        while (list.size() < size) {
            int randomNum = random.nextInt(maxNum);
            if (!list.contains(randomNum)) {
                list.add(randomNum);
            }
        }
        Collections.sort(list);
        return list;
    }

    public static <T> boolean checkAnswer(T user_input, T answer) {
        System.out.println("\nYour Answer:     " + user_input);
        System.out.println("Correct Answer:  " + answer);

        if (user_input == answer) {
            System.out.println("\nYOUR ANSWER IS CORRECT!");
        }
        else {
            System.out.println("\nSO CLOSE... BETTER LUCK NEXT TIME");
        }

        return user_input.equals(answer);
    }

    public static <T> void display(List<T> list, String message) {
        //Main.printCenteredTitle(message, Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_MENU);
        System.out.println(message);

        for (T element : list) {
            System.out.print(element + " ");
        }

        System.out.println('\n');
    }

    public void printGameScoreAsTable() {
        System.out.println("\nScores:");
        System.out.println("+-----------------+---------------+");
        System.out.println("| Letter Game     | " + String.format("%10s", getLettersScore()) + " PTS|");
        System.out.println("| Number Game     | " + String.format("%10s", getNumbersScore()) + " PTS|");
        System.out.println("+-----------------+---------------+");
        System.out.println("| Total Score     | " + String.format("%10s", getTotalScore()) + " PTS|");
        System.out.println("| Games Played    | " + String.format("%10s", getNumGamesPlayed()) + "    |");
        System.out.println("+-----------------+---------------+");
    }

}
