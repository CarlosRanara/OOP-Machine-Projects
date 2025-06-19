/**
 * Enum defining weapon types with their properties and descriptions.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
enum WeaponType {
    DAGGER("Dagger", 20, 0, "Quick and precise strikes"),
    SWORD("Sword", 30, -10, "Balanced offensive weapon"),
    BATTLE_AXE("Battle Axe", 40, -20, "Devastating but slow attacks");

    private final String name;
    private final int attackBonus;
    private final int speedPenalty;
    private final String description;

    /**
     * Constructor for WeaponType enum.
     * @param name Display name of the weapon type
     * @param attackBonus Attack points added by this weapon
     * @param speedPenalty Speed points reduced by this weapon (negative value)
     * @param description Flavor text describing the weapon
     */
    WeaponType(String name, int attackBonus, int speedPenalty, String description) {
        this.name = name;
        this.attackBonus = attackBonus;
        this.speedPenalty = speedPenalty;
        this.description = description;
    }

    /**
     * Displays all weapon types with their statistics.
     */
    public static void displayAllTypes() {
        for (int i = 0; i < values().length; i++) {
            WeaponType type = values()[i];
            System.out.printf("%d. %-11s | +%d Atk, %d Spd | %s%n",
                    i + 1, type.name, type.attackBonus, type.speedPenalty, type.description);
        }
        System.out.println();
    }

    // Getters
    public String getName() { return name; }
    public int getAttackBonus() { return attackBonus; }
    public int getSpeedPenalty() { return speedPenalty; }
    public String getDescription() { return description; }
}

/**
 * Represents weapon equipment with offensive properties.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class Weapon {
    private WeaponType type;

    /**
     * Constructor - creates weapon of specified type.
     * @param type Weapon type from WeaponType enum
     */
    public Weapon(WeaponType type) {
        this.type = type;
    }

    /**
     * Gets formatted description of weapon effects.
     * @return Description string
     */
    public String getDescription() {
        String speedText = type.getSpeedPenalty() == 0 ? "No speed penalty" :
                type.getSpeedPenalty() + " Speed";
        return String.format("+%d Attack, %s", type.getAttackBonus(), speedText);
    }

    // Getters
    public String getName() { return type.getName(); }
    public int getAttackBonus() { return type.getAttackBonus(); }
    public int getSpeedPenalty() { return type.getSpeedPenalty(); }
    public WeaponType getType() { return type; }
}