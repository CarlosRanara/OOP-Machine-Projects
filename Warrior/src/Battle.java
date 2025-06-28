import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Manages the turn-based battle system with comprehensive combat mechanics.
 *
 * The Battle class orchestrates combat between the player and opponent,
 * handling turn order, action execution, environment effects, and battle resolution.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class Battle {
    private Warrior player;
    private Opponent opponent;
    private Environment environment;
    private Scanner scanner;
    private int turnCount;
    private BattleLogger logger;

    /**
     * Constructor - initializes battle with all participants and environment.
     * @param player Player warrior
     * @param opponent Enemy opponent
     * @param environment Battle environment
     */
    public Battle(Warrior player, Opponent opponent, Environment environment) {
        this.player = player;
        this.opponent = opponent;
        this.environment = environment;
        this.scanner = new Scanner(System.in);
        this.turnCount = 0;
        this.logger = new BattleLogger();
    }

    /**
     * Starts and manages the complete battle sequence.
     * @return Battle result containing outcome and statistics
     */
    public BattleResult startBattle() {
        System.out.println("========================================================================");
        System.out.println("                          BATTLE BEGINS                                ");
        System.out.println("========================================================================");
        System.out.printf("Location: %s%n", environment.getName());
        System.out.printf("%s vs %s%n", "Warrior", opponent.getName());
        System.out.println("Fight until one falls! Victory awaits the worthy!");
        System.out.println();

        boolean playerDefending = false;
        long battleStartTime = System.currentTimeMillis();

        // Main battle loop
        while (player.isAlive() && opponent.isAlive()) {
            turnCount++;
            System.out.println("==================================================");
            System.out.printf("                    TURN %d                     %n", turnCount);
            System.out.println("==================================================");

            // Display current battle status
            displayBattleStatus();

            // Get player action
            int playerAction = getPlayerAction();
            playerDefending = (playerAction == 2);

            // Determine turn order (defend always goes first, otherwise speed determines)
            boolean playerFirst = playerDefending || player.getTotalSpeed() >= opponent.getSpeed();

            System.out.println("\nTURN RESOLUTION:");
            System.out.println("------------------------------");

            // Execute actions based on speed/priority
            if (playerFirst) {
                executePlayerAction(playerAction);
                if (opponent.isAlive()) {
                    executeOpponentAction(playerDefending ? 0.5 : 1.0);
                }
            } else {
                executeOpponentAction(1.0);
                if (player.isAlive() && playerAction != 2) { // Don't execute if player was defending
                    executePlayerAction(playerAction);
                }
            }

            // Apply environment effects at end of turn
            System.out.println("\nENVIRONMENT EFFECTS:");
            if (player.isAlive()) {
                player.applyEnvironmentEffect(environment);
            }
            if (opponent.isAlive()) {
                opponent.applyEnvironmentEffect(environment);
            }

            // Update player charge availability
            player.updateChargeAvailability();

            // Log turn data
            logger.logTurn(turnCount, player.getCurrentHp(), opponent.getCurrentHp());

            // Check for battle end
            if (!player.isAlive() || !opponent.isAlive()) {
                break;
            }

            System.out.println("\nEnd of turn " + turnCount);
            pauseForEffect(1000);
        }

        // Determine and display battle outcome
        long battleEndTime = System.currentTimeMillis();
        long battleDuration = battleEndTime - battleStartTime;

        BattleResult result = determineBattleOutcome(battleDuration);
        displayBattleOutcome(result);

        return result;
    }

    /**
     * Displays current status of both combatants.
     */
    private void displayBattleStatus() {
        System.out.println("BATTLE STATUS:");
        System.out.print("Your Status: ");
        player.displayCompactStats();
        System.out.print("Enemy Status: ");
        opponent.displayCompactStats();

        // Display HP bars
        displayHealthBar("You", player.getCurrentHp(), player.getMaxHp());
        displayHealthBar(opponent.getName(), opponent.getCurrentHp(), opponent.getMaxHp());
        System.out.println();
    }

    /**
     * Displays a visual health bar.
     * @param name Character name
     * @param currentHp Current HP
     * @param maxHp Maximum HP
     */
    private void displayHealthBar(String name, int currentHp, int maxHp) {
        int barLength = 20;
        int filledLength = (int)((double)currentHp / maxHp * barLength);

        StringBuilder bar = new StringBuilder();
        bar.append("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                bar.append("#");
            } else {
                bar.append("-");
            }
        }
        bar.append("]");

        System.out.printf("%-10s %s %d/%d HP%n", name + ":", bar.toString(), currentHp, maxHp);
    }

    /**
     * Gets the player's action choice with comprehensive input validation.
     * @return Player action (1=Attack, 2=Defend, 3=Charge)
     */
    private int getPlayerAction() {
        System.out.println("CHOOSE YOUR ACTION:");
        System.out.println("1. Attack  - Strike your enemy");
        System.out.println("2. Defend  - Reduce incoming damage by half");
        System.out.println("3. Charge  - Skip turn to triple next attack");

        if (player.isCharging()) {
            System.out.println("   (CHARGED! Next attack will be devastating!)");
        } else if (!player.canCharge()) {
            System.out.println("   (Charge on cooldown)");
        }

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("\nEnter your choice (1-3): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice >= 1 && choice <= 3) {
                    // Special validation for charge
                    if (choice == 3 && !player.canCharge() && !player.isCharging()) {
                        System.out.println("You cannot charge yet! Choose a different action.");
                        continue;
                    }
                    validInput = true;
                } else {
                    System.out.println("Invalid choice! Please select 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        return choice;
    }

    /**
     * Executes the player's chosen action.
     * @param action Action to execute (1=Attack, 2=Defend, 3=Charge)
     */
    private void executePlayerAction(int action) {
        switch (action) {
            case 1: // Attack
                System.out.println("You launch an attack!");
                int damage = player.performAttack(opponent);
                logger.logPlayerAttack(damage);
                break;
            case 2: // Defend
                double defenseMultiplier = player.performDefend();
                logger.logPlayerDefend();
                break;
            case 3: // Charge
                boolean chargeSuccess = player.performCharge();
                if (chargeSuccess) {
                    logger.logPlayerCharge();
                }
                break;
        }
    }

    /**
     * Executes the opponent's action (simple AI - always attacks).
     * @param playerDefenseMultiplier Damage multiplier if player is defending
     */
    private void executeOpponentAction(double playerDefenseMultiplier) {
        int damage = opponent.performAttack(player, playerDefenseMultiplier);
        logger.logOpponentAttack(damage);
    }

    /**
     * Determines the battle outcome and creates result object.
     * @param battleDuration Duration of battle in milliseconds
     * @return Battle result with outcome and statistics
     */
    private BattleResult determineBattleOutcome(long battleDuration) {
        boolean playerWon = player.isAlive();
        String outcome = playerWon ? "VICTORY" : "DEFEAT";

        return new BattleResult(
                playerWon,
                outcome,
                turnCount,
                battleDuration,
                player.getCurrentHp(),
                opponent.getCurrentHp(),
                logger.getTotalDamageDealt(),
                logger.getTotalDamageTaken()
        );
    }

    /**
     * Displays the battle outcome with detailed statistics.
     * @param result Battle result to display
     */
    private void displayBattleOutcome(BattleResult result) {
        System.out.println("\n" + "=".repeat(60));
        if (result.isPlayerVictory()) {
            System.out.println("                     GLORIOUS VICTORY!                     ");
            System.out.printf("You have triumphed over the %s!%n", opponent.getName());
            System.out.printf("Remaining HP: %d/%d%n", player.getCurrentHp(), player.getMaxHp());
        } else {
            System.out.println("                    HONORABLE DEFEAT                      ");
            System.out.printf("The %s has bested you in combat.%n", opponent.getName());
            System.out.println("Train harder and return stronger!");
        }

        System.out.println("=".repeat(60));

        // Display detailed battle statistics
        System.out.println("\nBATTLE STATISTICS:");
        System.out.println("----------------------------------------");
        System.out.printf("Battle Duration: %.1f seconds%n", result.getBattleDuration() / 1000.0);
        System.out.printf("Total Turns: %d%n", result.getTurnsElapsed());
        System.out.printf("Damage Dealt: %d%n", result.getTotalDamageDealt());
        System.out.printf("Damage Taken: %d%n", result.getTotalDamageTaken());
        System.out.printf("Damage Ratio: %.2f%n",
                result.getTotalDamageTaken() > 0 ?
                        (double)result.getTotalDamageDealt() / result.getTotalDamageTaken() :
                        result.getTotalDamageDealt());
        System.out.printf("Battlefield: %s%n", environment.getName());

        System.out.println();
    }

    /**
     * Pauses execution for dramatic effect.
     * @param milliseconds Time to pause in milliseconds
     */
    private void pauseForEffect(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}