
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * BattleManager handles battle flow and combat mechanics
 */
public class BattleManager {
    private PlayerManager playerManager;
    private Scanner scanner;
    private Random random;

    /**
     * Constructor for BattleManager
     * @param playerManager the player manager
     * @param scanner Scanner for user input
     */
    public BattleManager(PlayerManager playerManager, Scanner scanner) {
        this.playerManager = playerManager;
        this.scanner = scanner;
        this.random = new Random();
    }

    /**
     * Starts and manages the battle flow
     */
    public void startBattle() {
        System.out.println("\n=== BATTLE SETUP ===");

        // Character selection
        Character player1Character = playerManager.selectCharacterForBattle("Player 1", scanner);
        Character player2Character = playerManager.selectCharacterForBattle("Player 2", scanner);

        // Reset characters for battle
        player1Character.resetForBattle();
        player2Character.resetForBattle();

        System.out.println("\n=== BATTLE BEGINS ===");
        System.out.println(player1Character.getName() + " vs " + player2Character.getName());

        boolean battleEnded = false;
        int round = 1;

        while (!battleEnded) {
            System.out.println("\n--- ROUND " + round + " ---");

            // Regenerate EP
            player1Character.regenerateEP();
            player2Character.regenerateEP();

            // Display status
            displayBattleStatus(player1Character, player2Character);

            // Get moves
            Ability player1Move = selectMove(player1Character, "Player 1");
            Ability player2Move = selectMove(player2Character, "Player 2");

            // Execute moves
            executeRound(player1Character, player1Move, player2Character, player2Move);

            // Check battle end
            battleEnded = checkBattleEnd(player1Character, player2Character);
            round++;
        }

        // Ask for rematch
        askForRematch();
    }

    /**
     * Displays current battle status
     * @param char1 first character
     * @param char2 second character
     */
    private void displayBattleStatus(Character char1, Character char2) {
        System.out.println(char1.getName() + " - HP: " + char1.getCurrentHP() + "/" + char1.getMaxHP() +
                " | EP: " + char1.getCurrentEP() + "/" + char1.getMaxEP());
        System.out.println(char2.getName() + " - HP: " + char2.getCurrentHP() + "/" + char2.getMaxHP() +
                " | EP: " + char2.getCurrentEP() + "/" + char2.getMaxEP());
    }

    /**
     * Allows player to select a move
     * @param character the character selecting a move
     * @param playerName name of the player
     * @return selected ability
     */
    private Ability selectMove(Character character, String playerName) {
        System.out.println("\n" + playerName + " (" + character.getName() + "), choose your move:");
        List<Ability> availableMoves = character.getAllMoves();

        for (int i = 0; i < availableMoves.size(); i++) {
            Ability move = availableMoves.get(i);
            String status = character.getCurrentEP() >= move.getEpCost() ? "" : " [INSUFFICIENT EP]";
            System.out.println((i + 1) + ". " + move + status);
        }

        while (true) {
            System.out.print("Choose move (1-" + availableMoves.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= availableMoves.size()) {
                    Ability selectedMove = availableMoves.get(choice - 1);
                    if (character.getCurrentEP() >= selectedMove.getEpCost()) {
                        return selectedMove;
                    } else {
                        System.out.println("Insufficient EP! Choose a different move.");
                    }
                } else {
                    System.out.println("Invalid choice! Please select a valid move.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    /**
     * Executes a round of combat
     * @param char1 first character
     * @param move1 first character's move
     * @param char2 second character
     * @param move2 second character's move
     */
    private void executeRound(Character char1, Ability move1, Character char2, Ability move2) {
        System.out.println("\n--- ROUND EXECUTION ---");
        System.out.println(char1.getName() + " uses " + move1.getName());
        System.out.println(char2.getName() + " uses " + move2.getName());

        // Use abilities (consume EP)
        char1.useAbility(move1);
        char2.useAbility(move2);

        // Execute moves simultaneously
        executeMove(char1, move1, char2, move2);
        executeMove(char2, move2, char1, move1);

        System.out.println("\n--- ROUND RESULTS ---");
        displayBattleStatus(char1, char2);
    }

    /**
     * Executes a single move
     * @param attacker the attacking character
     * @param attackerMove the attacker's move
     * @param defender the defending character
     * @param defenderMove the defender's move
     */
    private void executeMove(Character attacker, Ability attackerMove, Character defender, Ability defenderMove) {
        switch (attackerMove.getType()) {
            case DAMAGE:
                if (defenderMove.getType() == AbilityType.SNEAK_ATTACK) {
                    System.out.println(defender.getName() + " evades " + attacker.getName() + "'s attack!");
                } else if (defenderMove.getType() == AbilityType.EVASION && random.nextBoolean()) {
                    System.out.println(defender.getName() + " evades " + attacker.getName() + "'s attack!");
                } else if (defenderMove.getType() == AbilityType.SHIELD) {
                    System.out.println(defender.getName() + " blocks all damage!");
                } else {
                    defender.takeDamage(attackerMove.getValue());
                    System.out.println(attacker.getName() + " deals " + attackerMove.getValue() + " damage to " + defender.getName());
                }
                break;

            case HEAL:
                attacker.heal(attackerMove.getValue());
                System.out.println(attacker.getName() + " heals for " + attackerMove.getValue() + " HP");
                break;

            case EP_RESTORE:
                attacker.restoreEP(attackerMove.getValue());
                System.out.println(attacker.getName() + " restores " + attackerMove.getValue() + " EP");
                break;

            case DEFEND:
                attacker.setDefending(true);
                System.out.println(attacker.getName() + " takes a defensive stance");
                break;

            case RECHARGE:
                attacker.restoreEP(5);
                System.out.println(attacker.getName() + " recharges and gains 5 EP");
                break;

            case SHIELD:
                System.out.println(attacker.getName() + " activates a protective barrier");
                break;

            case EVASION:
                System.out.println(attacker.getName() + " prepares to evade attacks");
                break;

            case SNEAK_ATTACK:
                defender.takeDamage(attackerMove.getValue());
                System.out.println(attacker.getName() + " performs a sneak attack for " + attackerMove.getValue() + " damage");
                break;
        }
    }

    /**
     * Checks if the battle has ended
     * @param char1 first character
     * @param char2 second character
     * @return true if battle has ended
     */
    private boolean checkBattleEnd(Character char1, Character char2) {
        if (!char1.isAlive() && !char2.isAlive()) {
            System.out.println("\n=== BATTLE RESULT ===");
            System.out.println("It's a draw! Both characters have fallen!");
            return true;
        } else if (!char1.isAlive()) {
            System.out.println("\n=== BATTLE RESULT ===");
            System.out.println(char2.getName() + " wins! " + char1.getName() + " has been defeated!");
            return true;
        } else if (!char2.isAlive()) {
            System.out.println("\n=== BATTLE RESULT ===");
            System.out.println(char1.getName() + " wins! " + char2.getName() + " has been defeated!");
            return true;
        }
        return false;
    }

    /**
     * Asks if players want a rematch
     */
    private void askForRematch() {
        System.out.print("\nWould you like to have a rematch? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("y") || response.equals("yes")) {
            startBattle();
        } else {
            System.out.println("Returning to main menu...");
        }
    }
}