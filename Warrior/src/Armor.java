/**
 * Enum defining armor types with their properties and descriptions.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
enum ArmorType {
    LIGHT("Light Armor", 20, -5, "Swift and agile protection"),
    MEDIUM("Medium Armor", 30, -15, "Balanced defense and mobility"),
    HEAVY("Heavy Armor", 40, -25, "Maximum protection, reduced speed");

    private final String name;
    private final int defenseBonus;
    private final int speedPenalty;
    private final String description;

    /**
     * Constructor for ArmorType enum.
     * @param name Display name of the armor type
     * @param defenseBonus Defense points added by this armor
     * @param speedPenalty Speed points reduced by this armor (negative value)
     * @param description Flavor text describing the armor
     */
    ArmorType(String name, int defenseBonus, int speedPenalty, String description) {
        this.name = name;
        this.defenseBonus = defenseBonus;
        this.speedPenalty = speedPenalty;
        this.description = description;
    }

    /**
     * Displays all armor types with their statistics.
     */
    public static void displayAllTypes() {
        for (int i = 0; i < values().length; i++) {
            ArmorType type = values()[i];
            System.out.printf("%d. %-12s | +%d Def, %d Spd | %s%n",
                    i + 1, type.name, type.defenseBonus, type.speedPenalty, type.description);
        }
        System.out.println();
    }

    // Getters
    public String getName() { return name; }
    public int getDefenseBonus() { return defenseBonus; }
    public int getSpeedPenalty() { return speedPenalty; }
    public String getDescription() { return description; }
}

/**
 * Represents armor equipment with defensive properties.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class Armor {
    private ArmorType type;

    /**
     * Constructor - creates armor of specified type.
     * @param type Armor type from ArmorType enum
     */
    public Armor(ArmorType type) {
        this.type = type;
    }

    /**
     * Gets formatted description of armor effects.
     * @return Description string
     */
    public String getDescription() {
        return String.format("+%d Defense, %d Speed",
                type.getDefenseBonus(), type.getSpeedPenalty());
    }

    // Getters
    public String getName() { return type.getName(); }
    public int getDefenseBonus() { return type.getDefenseBonus(); }
    public int getSpeedPenalty() { return type.getSpeedPenalty(); }
    public ArmorType getType() { return type; }
}