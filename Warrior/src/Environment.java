/**
 * Enum defining environment types with their effects and descriptions.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
enum EnvironmentType {
    ARENA("Arena", "No penalties for either combatant", "Neutral battleground"),
    SWAMP("Swamp", "Player: -1 HP/turn | Opponent: +1 Atk/turn", "Treacherous marshland"),
    COLOSSEUM("Colosseum", "Player: +1 Atk/turn | Opponent: -1 Def/turn", "Roaring crowd energizes you");

    private final String name;
    private final String effects;
    private final String description;

    /**
     * Constructor for EnvironmentType enum.
     * @param name Display name of the environment
     * @param effects Description of environmental effects
     * @param description Flavor text describing the environment
     */
    EnvironmentType(String name, String effects, String description) {
        this.name = name;
        this.effects = effects;
        this.description = description;
    }

    /**
     * Displays all environment types with their effects.
     */
    public static void displayAllTypes() {
        for (int i = 0; i < values().length; i++) {
            EnvironmentType type = values()[i];
            System.out.printf("%d. %-10s | %s%n", i + 1, type.name, type.description);
            System.out.printf("   Effects: %s%n%n", type.effects);
        }
    }

    // Getters
    public String getName() { return name; }
    public String getEffects() { return effects; }
    public String getDescription() { return description; }
}

/**
 * Represents battle environments with special effects.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025
 */
class Environment {
    private EnvironmentType type;

    /**
     * Constructor - creates environment of specified type.
     * @param type Environment type from EnvironmentType enum
     */
    public Environment(EnvironmentType type) {
        this.type = type;
    }

    /**
     * Applies environment effect to the player.
     * @param player Player to affect
     */
    public void applyPlayerEffect(Warrior player) {
        switch (type) {
            case SWAMP:
                player.loseHp(1);
                System.out.println("The swamp drains your life force! (-1 HP)");
                break;
            case COLOSSEUM:
                player.gainAttackBonus(1);
                System.out.println("The roaring crowd energizes you! (+1 Attack)");
                break;
            case ARENA:
                // No effect
                break;
        }
    }

    /**
     * Applies environment effect to the opponent.
     * @param opponent Opponent to affect
     */
    public void applyOpponentEffect(Opponent opponent) {
        switch (type) {
            case SWAMP:
                opponent.gainAttack(1);
                System.out.printf("The %s draws power from the swamp! (+1 Attack)%n",
                        opponent.getName());
                break;
            case COLOSSEUM:
                opponent.loseDefense(1);
                System.out.printf("The %s is intimidated by the crowd! (-1 Defense)%n",
                        opponent.getName());
                break;
            case ARENA:
                // No effect
                break;
        }
    }

    // Getters
    public String getName() { return type.getName(); }
    public String getDescription() { return type.getEffects(); }
    public EnvironmentType getType() { return type; }
}