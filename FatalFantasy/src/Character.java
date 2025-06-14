import java.util.ArrayList;
import java.util.List;

/**
 * Character class represents a game character with stats and abilities
 * Each character has HP, EP, a class type, and selected abilities
 */
public class Character {
    private String name;
    private CharacterClass characterClass;
    private int maxHP;
    private int currentHP;
    private int maxEP;
    private int currentEP;
    private List<Ability> selectedAbilities;
    private boolean isDefending;

    /**
     * Constructor for Character
     * @param name character name
     * @param characterClass character class type
     * @param selectedAbilities list of selected abilities
     */
    public Character(String name, CharacterClass characterClass, List<Ability> selectedAbilities) {
        this.name = name;
        this.characterClass = characterClass;
        this.maxHP = 100;
        this.currentHP = maxHP;
        this.maxEP = 50;
        this.currentEP = maxEP;
        this.selectedAbilities = new ArrayList<>(selectedAbilities);
        this.isDefending = false;
    }

    /**
     * Resets character to full HP and EP for battle
     */
    public void resetForBattle() {
        this.currentHP = maxHP;
        this.currentEP = maxEP;
        this.isDefending = false;
    }

    /**
     * Regenerates EP at the start of each round
     */
    public void regenerateEP() {
        currentEP = Math.min(maxEP, currentEP + 5);
        isDefending = false; // Reset defending status
    }

    /**
     * Uses an ability and consumes EP
     * @param ability the ability to use
     * @return true if ability was used successfully
     */
    public boolean useAbility(Ability ability) {
        if (currentEP >= ability.getEpCost()) {
            currentEP -= ability.getEpCost();
            return true;
        }
        return false;
    }

    /**
     * Takes damage, considering defense status
     * @param damage amount of damage to take
     */
    public void takeDamage(int damage) {
        int actualDamage = isDefending ? damage / 2 : damage;
        currentHP = Math.max(0, currentHP - actualDamage);
    }

    /**
     * Heals the character
     * @param amount amount to heal
     */
    public void heal(int amount) {
        currentHP = Math.min(maxHP, currentHP + amount);
    }

    /**
     * Restores EP
     * @param amount amount of EP to restore
     */
    public void restoreEP(int amount) {
        currentEP = Math.min(maxEP, currentEP + amount);
    }

    /**
     * Sets defending status
     * @param defending true if character is defending
     */
    public void setDefending(boolean defending) {
        this.isDefending = defending;
    }

    /**
     * Checks if character is alive
     * @return true if HP > 0
     */
    public boolean isAlive() {
        return currentHP > 0;
    }

    /**
     * Gets all available moves including universal abilities
     * @return list of all available abilities
     */
    public List<Ability> getAllMoves() {
        List<Ability> allMoves = new ArrayList<>();
        allMoves.addAll(selectedAbilities);
        allMoves.add(new Ability("Defend", "Take defensive stance, take half damage", 5, AbilityType.DEFEND));
        allMoves.add(new Ability("Recharge", "Do nothing but regain 5 EP", 0, AbilityType.RECHARGE));
        return allMoves;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public CharacterClass getCharacterClass() { return characterClass; }
    public int getMaxHP() { return maxHP; }
    public int getCurrentHP() { return currentHP; }
    public int getMaxEP() { return maxEP; }
    public int getCurrentEP() { return currentEP; }
    public List<Ability> getSelectedAbilities() { return new ArrayList<>(selectedAbilities); }
    public void setSelectedAbilities(List<Ability> abilities) { this.selectedAbilities = new ArrayList<>(abilities); }
    public boolean isDefending() { return isDefending; }
}