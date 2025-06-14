import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * GameManager class handles the main game flow and menu system
 * Manages the overall game state and user interactions
 */
public class GameManager {
    private PlayerManager playerManager;
    private Scanner scanner;

    /**
     * Constructor for GameManager
     * Initializes the player manager and scanner
     */
    public GameManager() {
        this.playerManager = new PlayerManager();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main game loop
     * Displays the main menu and handles user choices
     */
    public void startGame() {
        System.out.println("=================================");
        System.out.println("   FATAL FANTASY: TACTICS");
        System.out.println("=================================");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getValidIntInput(1, 3);

            switch (choice) {
                case 1:
                    handleCharacterManagement();
                    break;
                case 2:
                    handleBattle();
                    break;
                case 3:
                    System.out.println("Thanks for playing!");
                    running = false;
                    break;
            }
        }
        scanner.close();
    }

    /**
     * Displays the main menu options
     */
    private void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Character Management");
        System.out.println("2. Start Battle");
        System.out.println("3. Exit Game");
        System.out.print("Choose an option: ");
    }

    /**
     * Handles character management menu and operations
     */
    private void handleCharacterManagement() {
        boolean inCharacterMenu = true;
        while (inCharacterMenu) {
            displayCharacterMenu();
            int choice = getValidIntInput(1, 6);

            switch (choice) {
                case 1:
                    playerManager.viewAllCharacters();
                    break;
                case 2:
                    playerManager.viewCharacterDetails();
                    break;
                case 3:
                    playerManager.createCharacter(scanner);
                    break;
                case 4:
                    playerManager.editCharacter(scanner);
                    break;
                case 5:
                    playerManager.deleteCharacter(scanner);
                    break;
                case 6:
                    inCharacterMenu = false;
                    break;
            }
        }
    }

    /**
     * Displays character management menu options
     */
    private void displayCharacterMenu() {
        System.out.println("\n--- CHARACTER MANAGEMENT ---");
        System.out.println("1. View All Characters");
        System.out.println("2. View Character Details");
        System.out.println("3. Create Character");
        System.out.println("4. Edit Character");
        System.out.println("5. Delete Character");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    /**
     * Handles battle setup and execution
     */
    private void handleBattle() {
        if (playerManager.getCharacterCount() < 2) {
            System.out.println("You need at least 2 characters to start a battle!");
            return;
        }

        BattleManager battleManager = new BattleManager(playerManager, scanner);
        battleManager.startBattle();
    }

    /**
     * Gets valid integer input within specified range
     * @param min minimum valid value
     * @param max maximum valid value
     * @return valid integer input
     */
    private int getValidIntInput(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); // consume invalid input
            }
        }
    }
}