/**
 * Enum defining opponent types with their properties and descriptions.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
enum OpponentType {
    THIEF("Thief", 150, 20, 20, 40, "Swift and cunning, strikes from shadows"),
    VIKING("Viking", 250, 30, 30, 30, "Fierce warrior with balanced combat skills"),
    MINOTAUR("Minotaur", 350, 40, 40, 20, "Massive beast with devastating power");

    private final String name;
    private final int hp;
    private final int attack;
    private final int defense;
    private final int speed;
    private final String description;

    /**
     * Constructor for OpponentType enum.
     * @param name Display name of the opponent
     * @param hp Hit points of the opponent
     * @param attack Attack value of the opponent
     * @param defense Defense value of the opponent
     * @param speed Speed value of the opponent
     * @param description Flavor text describing the opponent
     */
    OpponentType(String name, int hp, int attack, int defense, int speed, String description) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.description = description;
    }

    /**
     * Displays all opponent types with their statistics.
     */
    public static void displayAllTypes() {
        for (int i = 0; i < values().length; i++) {
            OpponentType type = values()[i];
            System.out.printf("%d. %-8s | %d HP, %d Atk, %d Def, %d Spd%n",
                    i + 1, type.name, type.hp, type.attack, type.defense, type.speed);
            System.out.printf("   %s%n%n", type.description);
        }
    }

    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public String getDescription() { return description; }
}

/**
 * Represents enemy opponents with AI behavior.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class Opponent {
    private OpponentType type;
    private int currentHp;
    private int maxHp;
    private int currentAttack;
    private int baseDefense;
    private int currentDefense;
    private int speed;

    /**
     * Constructor - creates opponent of specified type.
     * @param type Opponent type from OpponentType enum
     */
    public Opponent(OpponentType type) {
        this.type = type;
        this.currentHp = type.getHp();
        this.maxHp = type.getHp();
        this.currentAttack = type.getAttack();
        this.baseDefense = type.getDefense();
        this.currentDefense = type.getDefense();
        this.speed = type.getSpeed();
    }

    /**
     * Opponent performs an attack action.
     * @param target Target to attack
     * @param targetDefenseMultiplier Defense multiplier if target is defending
     * @return Damage dealt
     */
    public int performAttack(Warrior target, double targetDefenseMultiplier) {
        System.out.printf("%s attacks with %d power!%n", type.getName(), currentAttack);
        return target.takeDamage(currentAttack, targetDefenseMultiplier);
    }

    /**
     * Opponent takes damage.
     * @param incomingDamage Raw damage before defense calculation
     * @return Actual damage taken
     */
    public int takeDamage(int incomingDamage) {
        int actualDamage = Math.max(0, incomingDamage - currentDefense);
        this.currentHp = Math.max(0, this.currentHp - actualDamage);

        System.out.printf("%s takes %d damage! HP: %d/%d%n",
                type.getName(), actualDamage, currentHp, maxHp);

        return actualDamage;
    }

    /**
     * Applies environment effects to the opponent.
     * @param environment Current battle environment
     */
    public void applyEnvironmentEffect(Environment environment) {
        environment.applyOpponentEffect(this);
    }

    /**
     * Increases attack from environment effects.
     * @param amount Amount to increase attack
     */
    public void gainAttack(int amount) {
        this.currentAttack += amount;
    }

    /**
     * Decreases defense from environment effects.
     * @param amount Amount to decrease defense
     */
    public void loseDefense(int amount) {
        this.currentDefense = Math.max(0, this.currentDefense - amount);
    }

    /**
     * Displays detailed opponent statistics.
     */
    public void displayDetailedStats() {
        System.out.printf("%s - HP: %d/%d | Attack: %d | Defense: %d | Speed: %d%n",
                type.getName(), currentHp, maxHp, currentAttack, currentDefense, speed);
    }

    /**
     * Displays compact opponent statistics.
     */
    public void displayCompactStats() {
        System.out.printf("%s: %d HP | %d Atk | %d Def | %d Spd%n",
                type.getName(), currentHp, currentAttack, currentDefense, speed);
    }

    // Getters
    public String getName() { return type.getName(); }
    public String getDescription() { return type.getDescription(); }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentAttack() { return currentAttack; }
    public int getCurrentDefense() { return currentDefense; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return currentHp > 0; }
    public OpponentType getType() { return type; }
}