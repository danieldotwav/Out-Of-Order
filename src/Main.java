package src;
import java.util.*;
import java.io.IOException;

enum MENU { DUMMY, SHOW_SCORE, LETTERS, NUMBERS, QUIT, ERROR }
enum DIFFICULTY { DUMMY, EASY, MEDIUM, HARD, ERROR }

public class Main {
    static Random random = new Random();
    public static void main(String[] args) throws IOException {

        StringBuilder title = new StringBuilder();
        title.append("\"").append(shuffle("Out")).append(" ").append(shuffle("Of")).append(" ").append(shuffle("Order")).append("!\"");

        printCenteredTitle("Welcome to " + title, Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);
        System.out.println("\n...wait that's not right");
        
        printCenteredTitle("Welcome to \"Out Of Order!\"", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);

        String menu_options = "\nMain Menu\n1. Show Score\n2. Letters Round\n3. Numbers Round\n4. Quit";
        MENU menu;
        Game game = new Game();

        do {
            printCenteredTitle("MAIN MENU", Constants.TOTAL_WIDTH, '-');
            menu = getMenuSelection(menu_options);
            switch(menu) {
                case SHOW_SCORE:
                    printCenteredTitle("PLAYER STATS", Constants.TOTAL_WIDTH, '-');
                    game.printGameScoreAsTable();
                    break;
                case LETTERS:
                    game.nextRound("LETTERS");
                    break;
                case NUMBERS:
                    game.nextRound("NUMBERS");
                    break;
                case QUIT:
                    System.out.println("\nThank you for playing!\n");
                    break;
                default:
                    System.out.println("\nError: Invalid Menu Option");
            }
        } while (menu != MENU.QUIT);

    }

    static MENU getMenuSelection(String menu_options) {
        int input;
        MENU menu = MENU.ERROR;

        try {
            System.out.println(menu_options);
            input = Integer.parseInt(System.console().readLine("Selection: "));

            if (input > 0 && input < MENU.values().length) {
                menu = MENU.values()[input];
            }
        }
        catch (NumberFormatException ex) {
            input = MENU.values().length;
        }
        return menu;
    }

    public static String shuffle(String word) {
        char[] characters = word.toCharArray();

        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }

        return new String(characters);
    }

    public static void printCenteredTitle(String title, int total_width, char padding_char) {
        int title_length = title.length();

        if (title_length > total_width) {
            title = title.substring(0, total_width);
            title_length = title.length();
        }

        int num_padding_chars = (total_width - title_length) / Constants.TWO;
        String padding = String.valueOf(padding_char).repeat(num_padding_chars);

        boolean needs_extra_padding = (total_width % Constants.TWO != 0) && (title_length % Constants.TWO == 0);

        StringBuilder output = new StringBuilder("\n");

        output.append(padding);
        if (needs_extra_padding) {
            output.append(padding_char);
        }

        if (title_length > 0) {
            output.append(" ").append(title).append(" ");
            output.append(padding);
        }
        else {
            output.append(padding_char).append(padding_char).append(padding);
        }

        System.out.println(output.toString());
    }
}