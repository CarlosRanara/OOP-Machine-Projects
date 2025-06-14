
/**
 * Ability class represents a character ability with name, description, and effects
 */
public class Ability {
    private String name;
    private String description;
    private int epCost;
    private AbilityType type;
    private int value; // damage, heal amount, EP restore, etc.

    /**
     * Constructor for abilities without value
     * @param name ability name
     * @param description ability description
     * @param epCost EP cost to use
     * @param type ability type
     */
    public Ability(String name, String description, int epCost, AbilityType type) {
        this(name, description, epCost, type, 0);
    }

    /**
     * Constructor for abilities with value
     * @param name ability name
     * @param description ability description
     * @param epCost EP cost to use
     * @param type ability type
     * @param value numerical value for the ability effect
     */
    public Ability(String name, String description, int epCost, AbilityType type, int value) {
        this.name = name;
        this.description = description;
        this.epCost = epCost;
        this.type = type;
        this.value = value;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEpCost() { return epCost; }
    public AbilityType getType() { return type; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return name + " (" + epCost + " EP) - " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ability ability = (Ability) obj;
        return name.equals(ability.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}