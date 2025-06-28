/**
 * Logs battle events and calculates statistics.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class BattleLogger {
    private int totalDamageDealt;
    private int totalDamageTaken;
    private int playerAttacks;
    private int playerDefends;
    private int playerCharges;
    private int opponentAttacks;

    /**
     * Constructor - initializes empty log.
     */
    public BattleLogger() {
        this.totalDamageDealt = 0;
        this.totalDamageTaken = 0;
        this.playerAttacks = 0;
        this.playerDefends = 0;
        this.playerCharges = 0;
        this.opponentAttacks = 0;
    }

    /**
     * Logs a player attack action.
     * @param damage Damage dealt
     */
    public void logPlayerAttack(int damage) {
        this.totalDamageDealt += damage;
        this.playerAttacks++;
    }

    /**
     * Logs a player defend action.
     */
    public void logPlayerDefend() {
        this.playerDefends++;
    }

    /**
     * Logs a player charge action.
     */
    public void logPlayerCharge() {
        this.playerCharges++;
    }

    /**
     * Logs an opponent attack action.
     * @param damage Damage taken by player
     */
    public void logOpponentAttack(int damage) {
        this.totalDamageTaken += damage;
        this.opponentAttacks++;
    }

    /**
     * Logs turn completion.
     * @param turnNumber Current turn number
     * @param playerHp Player's current HP
     * @param opponentHp Opponent's current HP
     */
    public void logTurn(int turnNumber, int playerHp, int opponentHp) {
        // Could expand this for detailed turn-by-turn logging
    }

    // Getters
    public int getTotalDamageDealt() { return totalDamageDealt; }
    public int getTotalDamageTaken() { return totalDamageTaken; }
    public int getPlayerAttacks() { return playerAttacks; }
    public int getPlayerDefends() { return playerDefends; }
    public int getPlayerCharges() { return playerCharges; }
    public int getOpponentAttacks() { return opponentAttacks; }
}

/**
 * Represents the result of a completed battle with comprehensive statistics.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class BattleResult {
    private boolean playerVictory;
    private String outcome;
    private int turnsElapsed;
    private long battleDuration;
    private int finalPlayerHp;
    private int finalOpponentHp;
    private int totalDamageDealt;
    private int totalDamageTaken;

    /**
     * Constructor - creates battle result with all statistics.
     * @param playerVictory Whether player won
     * @param outcome Outcome description
     * @param turnsElapsed Number of turns taken
     * @param battleDuration Battle duration in milliseconds
     * @param finalPlayerHp Player's final HP
     * @param finalOpponentHp Opponent's final HP
     * @param totalDamageDealt Total damage dealt by player
     * @param totalDamageTaken Total damage taken by player
     */
    public BattleResult(boolean playerVictory, String outcome, int turnsElapsed,
                        long battleDuration, int finalPlayerHp, int finalOpponentHp,
                        int totalDamageDealt, int totalDamageTaken) {
        this.playerVictory = playerVictory;
        this.outcome = outcome;
        this.turnsElapsed = turnsElapsed;
        this.battleDuration = battleDuration;
        this.finalPlayerHp = finalPlayerHp;
        this.finalOpponentHp = finalOpponentHp;
        this.totalDamageDealt = totalDamageDealt;
        this.totalDamageTaken = totalDamageTaken;
    }

    // Getters
    public boolean isPlayerVictory() { return playerVictory; }
    public String getOutcome() { return outcome; }
    public int getTurnsElapsed() { return turnsElapsed; }
    public long getBattleDuration() { return battleDuration; }
    public int getFinalPlayerHp() { return finalPlayerHp; }
    public int getFinalOpponentHp() { return finalOpponentHp; }
    public int getTotalDamageDealt() { return totalDamageDealt; }
    public int getTotalDamageTaken() { return totalDamageTaken; }
}

/**
 * Tracks and displays game session statistics.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class GameStats {
    private int totalGames;
    private int victories;
    private int defeats;
    private int totalTurns;
    private long totalPlayTime;
    private int totalDamageDealt;
    private int totalDamageTaken;

    /**
     * Constructor - initializes empty statistics.
     */
    public GameStats() {
        this.totalGames = 0;
        this.victories = 0;
        this.defeats = 0;
        this.totalTurns = 0;
        this.totalPlayTime = 0;
        this.totalDamageDealt = 0;
        this.totalDamageTaken = 0;
    }

    /**
     * Records a completed battle and updates statistics.
     * @param result Battle result to record
     */
    public void recordBattle(BattleResult result) {
        this.totalGames++;
        this.totalTurns += result.getTurnsElapsed();
        this.totalPlayTime += result.getBattleDuration();
        this.totalDamageDealt += result.getTotalDamageDealt();
        this.totalDamageTaken += result.getTotalDamageTaken();

        if (result.isPlayerVictory()) {
            this.victories++;
        } else {
            this.defeats++;
        }
    }

    /**
     * Displays current session statistics.
     */
    public void displayStats() {
        if (totalGames == 0) {
            System.out.println("No battles fought yet!");
            return;
        }

        double winRate = (double)victories / totalGames * 100;
        double avgTurns = (double)totalTurns / totalGames;
        double avgPlayTime = (double)totalPlayTime / totalGames / 1000.0;

        System.out.printf("Games Played: %d%n", totalGames);
        System.out.printf("Victories: %d%n", victories);
        System.out.printf("Defeats: %d%n", defeats);
        System.out.printf("Win Rate: %.1f%%%n", winRate);
        System.out.printf("Average Battle Time: %.1f seconds%n", avgPlayTime);
        System.out.printf("Average Turns per Battle: %.1f%n", avgTurns);
        System.out.printf("Total Damage Dealt: %d%n", totalDamageDealt);
        System.out.printf("Total Damage Taken: %d%n", totalDamageTaken);

        if (totalDamageTaken > 0) {
            double overallRatio = (double)totalDamageDealt / totalDamageTaken;
            System.out.printf("Overall Damage Ratio: %.2f%n", overallRatio);
        }
    }

    // Getters
    public int getTotalGames() { return totalGames; }
    public int getVictories() { return victories; }
    public int getDefeats() { return defeats; }
    public double getWinRate() { return totalGames > 0 ? (double)victories / totalGames * 100 : 0; }
}