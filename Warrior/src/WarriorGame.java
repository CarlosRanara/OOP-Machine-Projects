/**
 * CCPROG3 Machine Project - Warrior Game
 * Phase 1 Main Application Class
 * 
 * Main class that manages the entire Warrior Game application.
 * Handles game initialization, character creation, and battle orchestration.
 * 
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */

import java.util.Scanner;
import java.util.InputMismatchException;

public class WarriorGame {
    private static Scanner scanner = new Scanner(System.in);
    private static GameStats gameStats = new GameStats();
    
    /**
     * Main entry point for the Warrior Game application.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        displayWelcome();
        
        boolean playAgain = true;
        while (playAgain) {
            try {
                playGame();
                playAgain = askPlayAgain();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Restarting game...\n");
            }
        }
        
        displayFarewell();
        scanner.close();
    }
    
    /**
     * Displays the welcome screen and game instructions.
     */
    private static void displayWelcome() {
        System.out.println("================================================================================");
        System.out.println("                          WARRIOR BATTLE GAME                                  ");
        System.out.println("                            CCPROG3 Project                                   ");
        System.out.println("================================================================================");
        System.out.println();
        System.out.println("Welcome to the arena, brave warrior!");
        System.out.println("Prepare yourself for epic battles against formidable foes.");
        System.out.println();
        System.out.println("How to play:");
        System.out.println("• Create your character and choose equipment");
        System.out.println("• Select an opponent and battlefield");
        System.out.println("• Engage in turn-based combat");
        System.out.println("• Use Attack, Defend, or Charge strategically");
        System.out.println("• Defeat your enemy to claim victory!");
        System.out.println();
        pressEnterToContinue();
    }
    
    /**
     * Main game loop that handles a single game session.
     */
    private static void playGame() {
        System.out.println("=== NEW GAME SESSION ===\n");
        
        // Character creation phase
        Warrior player = createCharacter();
        
        // Opponent selection phase
        Opponent enemy = selectOpponent();
        
        // Environment selection phase
        Environment battleground = selectEnvironment();
        
        // Pre-battle summary
        displayBattleSummary(player, enemy, battleground);
        
        // Battle phase
        Battle battle = new Battle(player, enemy, battleground);
        BattleResult result = battle.startBattle();
        
        // Post-battle statistics
        gameStats.recordBattle(result);
        displayGameStats();
    }
    
    /**
     * Creates and configures the player's warrior character.
     * @return Fully configured Warrior object
     */
    private static Warrior createCharacter() {
        System.out.println("=== CHARACTER CREATION ===");
        System.out.println("Time to forge your legend! Choose your equipment wisely.\n");
        
        Warrior warrior = new Warrior();
        
        // Display base stats
        System.out.println("Base Warrior Stats:");
        warrior.displayDetailedStats();
        System.out.println();
        
        // Armor selection with detailed information
        Armor armor = selectArmor();
        warrior.equipArmor(armor);
        
        // Weapon selection with detailed information
        Weapon weapon = selectWeapon();
        warrior.equipWeapon(weapon);
        
        // Display final character stats
        System.out.println("\n=== YOUR WARRIOR ===");
        warrior.displayDetailedStats();
        System.out.println("Equipment: " + armor.getName() + " + " + weapon.getName());
        System.out.println();
        
        return warrior;
    }
    
    /**
     * Handles armor selection with detailed descriptions.
     * @return Selected Armor object
     */
    private static Armor selectArmor() {
        System.out.println("--- ARMOR SELECTION ---");
        System.out.println("Choose your protection:");
        System.out.println();
        
        ArmorType.displayAllTypes();
        
        int choice = getValidChoice("armor", 1, 3);
        Armor selectedArmor = new Armor(ArmorType.values()[choice - 1]);
        
        System.out.println("Selected: " + selectedArmor.getName());
        System.out.println("Effect: " + selectedArmor.getDescription());
        
        return selectedArmor;
    }
    
    /**
     * Handles weapon selection with detailed descriptions.
     * @return Selected Weapon object
     */
    private static Weapon selectWeapon() {
        System.out.println("\n--- WEAPON SELECTION ---");
        System.out.println("Choose your weapon:");
        System.out.println();
        
        WeaponType.displayAllTypes();
        
        int choice = getValidChoice("weapon", 1, 3);
        Weapon selectedWeapon = new Weapon(WeaponType.values()[choice - 1]);
        
        System.out.println("Selected: " + selectedWeapon.getName());
        System.out.println("Effect: " + selectedWeapon.getDescription());
        
        return selectedWeapon;
    }
    
    /**
     * Handles opponent selection with detailed information.
     * @return Selected Opponent object
     */
    private static Opponent selectOpponent() {
        System.out.println("=== OPPONENT SELECTION ===");
        System.out.println("Choose your adversary:");
        System.out.println();
        
        OpponentType.displayAllTypes();
        
        int choice = getValidChoice("opponent", 1, 3);
        Opponent selectedOpponent = new Opponent(OpponentType.values()[choice - 1]);
        
        System.out.println("Selected: " + selectedOpponent.getName());
        System.out.println("Description: " + selectedOpponent.getDescription());
        System.out.println();
        
        return selectedOpponent;
    }
    
    /**
     * Handles environment selection with detailed effects.
     * @return Selected Environment object
     */
    private static Environment selectEnvironment() {
        System.out.println("=== ENVIRONMENT SELECTION ===");
        System.out.println("Choose your battlefield:");
        System.out.println();
        
        EnvironmentType.displayAllTypes();
        
        int choice = getValidChoice("environment", 1, 3);
        Environment selectedEnvironment = new Environment(EnvironmentType.values()[choice - 1]);
        
        System.out.println("Selected: " + selectedEnvironment.getName());
        System.out.println("Effects: " + selectedEnvironment.getDescription());
        System.out.println();
        
        return selectedEnvironment;
    }
    
    /**
     * Displays pre-battle summary information.
     */
    private static void displayBattleSummary(Warrior player, Opponent enemy, Environment environment) {
        System.out.println("=== BATTLE SUMMARY ===");
        System.out.println("Warrior vs " + enemy.getName());
        System.out.println("Location: " + environment.getName());
        System.out.println();
        
        System.out.println("Your Stats:");
        player.displayCompactStats();
        System.out.println();
        
        System.out.println("Enemy Stats:");
        enemy.displayCompactStats();
        System.out.println();
        
        pressEnterToContinue();
    }
    
    /**
     * Displays current game statistics.
     */
    private static void displayGameStats() {
        System.out.println("\n=== SESSION STATISTICS ===");
        gameStats.displayStats();
        System.out.println();
    }
    
    /**
     * Asks if the player wants to play another game.
     * @return true if player wants to play again, false otherwise
     */
    private static boolean askPlayAgain() {
        System.out.print("Would you like to play another game? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        while (!response.equals("y") && !response.equals("n") && 
               !response.equals("yes") && !response.equals("no")) {
            System.out.print("Please enter 'y' for yes or 'n' for no: ");
            response = scanner.nextLine().trim().toLowerCase();
        }
        
        return response.equals("y") || response.equals("yes");
    }
    
    /**
     * Displays farewell message.
     */
    private static void displayFarewell() {
        System.out.println("\n================================================================================");
        System.out.println("                        Thanks for playing!                                    ");
        System.out.println("                   May your victories be legendary!                           ");
        System.out.println("================================================================================");
    }
    
    /**
     * Gets a valid integer choice within specified range with robust error handling.
     * @param itemType Type of item being selected (for error messages)
     * @param min Minimum valid value
     * @param max Maximum valid value
     * @return Valid user choice
     */
    private static int getValidChoice(String itemType, int min, int max) {
        int choice = -1;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (choice >= min && choice <= max) {
                    validInput = true;
                } else {
                    System.out.println("Invalid choice! Please select a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        
        return choice;
    }
    
    /**
     * Pauses execution until user presses Enter.
     */
    private static void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }
}