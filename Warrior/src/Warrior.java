/**
 * Represents the player's warrior character with equipment and combat abilities.
 *
 * The Warrior class encapsulates all player character functionality including
 * base statistics, equipment management, combat actions, and stat calculations.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
public class Warrior {
    // Base stats constants
    private static final int BASE_HP = 100;
    private static final int BASE_ATTACK = 1;
    private static final int BASE_DEFENSE = 1;
    private static final int BASE_SPEED = 50;

    // Character state
    private int currentHp;
    private int maxHp;
    private int baseAttack;
    private int baseDefense;
    private int baseSpeed;

    // Equipment
    private Armor equippedArmor;
    private Weapon equippedWeapon;

    // Combat state
    private boolean isCharging;
    private boolean canCharge;
    private int environmentAttackBonus;

    /**
     * Constructor - initializes warrior with base stats.
     */
    public Warrior() {
        this.currentHp = BASE_HP;
        this.maxHp = BASE_HP;
        this.baseAttack = BASE_ATTACK;
        this.baseDefense = BASE_DEFENSE;
        this.baseSpeed = BASE_SPEED;
        this.isCharging = false;
        this.canCharge = true;
        this.environmentAttackBonus = 0;
    }

    /**
     * Equips armor and updates character stats.
     * @param armor Armor to equip
     */
    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
    }

    /**
     * Equips weapon and updates character stats.
     * @param weapon Weapon to equip
     */
    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
    }

    /**
     * Calculates total attack including equipment and environment bonuses.
     * @return Total attack value
     */
    public int getTotalAttack() {
        int total = baseAttack + environmentAttackBonus;
        if (equippedWeapon != null) {
            total += equippedWeapon.getAttackBonus();
        }
        return total;
    }

    /**
     * Calculates total defense including equipment bonuses.
     * @return Total defense value
     */
    public int getTotalDefense() {
        int total = baseDefense;
        if (equippedArmor != null) {
            total += equippedArmor.getDefenseBonus();
        }
        return total;
    }

    /**
     * Calculates total speed including equipment penalties.
     * @return Total speed value
     */
    public int getTotalSpeed() {
        int total = baseSpeed;
        if (equippedArmor != null) {
            total += equippedArmor.getSpeedPenalty();
        }
        if (equippedWeapon != null) {
            total += equippedWeapon.getSpeedPenalty();
        }
        return Math.max(1, total); // Minimum speed of 1
    }

    /**
     * Warrior performs an attack action.
     * @param target Target to attack
     * @return Damage dealt
     */
    public int performAttack(Opponent target) {
        int damage = getTotalAttack();

        if (isCharging) {
            damage *= 3;
            isCharging = false;
            canCharge = true;
            System.out.println("CHARGED ATTACK! Triple damage dealt!");
        }

        return target.takeDamage(damage);
    }

    /**
     * Warrior performs a defend action.
     * @return Defense multiplier for incoming damage
     */
    public double performDefend() {
        System.out.println("You raise your guard defensively!");
        return 0.5; // Half damage when defending
    }

    /**
     * Warrior performs a charge action.
     * @return true if charge was successful, false if not allowed
     */
    public boolean performCharge() {
        if (!canCharge) {
            System.out.println("You cannot charge yet! (Must wait one turn after charging)");
            return false;
        }

        isCharging = true;
        canCharge = false;
        System.out.println("You prepare a devastating charge attack!");
        System.out.println("(Next attack will deal triple damage!)");
        return true;
    }

    /**
     * Warrior takes damage with optional defense multiplier.
     * @param incomingDamage Raw damage before calculations
     * @param defenseMultiplier Damage reduction multiplier (1.0 = normal, 0.5 = defending)
     * @return Actual damage taken
     */
    public int takeDamage(int incomingDamage, double defenseMultiplier) {
        int modifiedDamage = (int)(incomingDamage * defenseMultiplier);
        int actualDamage = Math.max(0, modifiedDamage - getTotalDefense());
        this.currentHp = Math.max(0, this.currentHp - actualDamage);

        String defenseStatus = defenseMultiplier < 1.0 ? " (defended)" : "";
        System.out.printf("You take %d damage%s! HP: %d/%d%n",
                actualDamage, defenseStatus, currentHp, maxHp);

        return actualDamage;
    }

    /**
     * Applies environment effects to the warrior.
     * @param environment Current battle environment
     */
    public void applyEnvironmentEffect(Environment environment) {
        environment.applyPlayerEffect(this);
    }

    /**
     * Reduces HP by specified amount (for environment effects).
     * @param amount Amount to reduce HP
     */
    public void loseHp(int amount) {
        this.currentHp = Math.max(0, this.currentHp - amount);
    }

    /**
     * Increases attack bonus from environment effects.
     * @param amount Amount to increase attack
     */
    public void gainAttackBonus(int amount) {
        this.environmentAttackBonus += amount;
    }

    /**
     * Resets charge availability (called each turn).
     */
    public void updateChargeAvailability() {
        if (!isCharging && !canCharge) {
            canCharge = true;
        }
    }

    /**
     * Displays detailed character statistics.
     */
    public void displayDetailedStats() {
        System.out.printf("HP: %d/%d | Attack: %d | Defense: %d | Speed: %d%n",
                currentHp, maxHp, getTotalAttack(), getTotalDefense(), getTotalSpeed());

        if (environmentAttackBonus > 0) {
            System.out.printf("Environment Attack Bonus: +%d%n", environmentAttackBonus);
        }

        if (isCharging) {
            System.out.println("Status: CHARGING (next attack will be tripled!)");
        } else if (!canCharge) {
            System.out.println("Status: Charge cooling down");
        }
    }

    /**
     * Displays compact character statistics.
     */
    public void displayCompactStats() {
        System.out.printf("HP: %d | Atk: %d | Def: %d | Spd: %d%n",
                currentHp, getTotalAttack(), getTotalDefense(), getTotalSpeed());
    }

    // Getters
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public boolean isAlive() { return currentHp > 0; }
    public boolean isCharging() { return isCharging; }
    public boolean canCharge() { return canCharge; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
}