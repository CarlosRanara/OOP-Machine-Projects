import java.util.Arrays;
import java.util.List;

/**
 * Enum representing different character classes and their abilities
 */
public enum CharacterClass {
    MAGE("Mage", Arrays.asList(
            new Ability("Arcane Bolt", "Launch a basic magical projectile that deals 20 arcane damage", 5, AbilityType.DAMAGE, 20),
            new Ability("Arcane Blast", "Unleash a burst of fiery energy, dealing 65 arcane damage", 30, AbilityType.DAMAGE, 65),
            new Ability("Mana Channel", "Draw upon ambient magical energy to restore 15 EP", 0, AbilityType.EP_RESTORE, 15),
            new Ability("Lesser Heal", "Weave a minor healing spell to restore 40 HP", 15, AbilityType.HEAL, 40),
            new Ability("Arcane Shield", "Conjure protective barrier, take no damage this round", 12, AbilityType.SHIELD)
    )),

    ROGUE("Rogue", Arrays.asList(
            new Ability("Shiv", "A quick, precise stab that deals 20 physical damage", 5, AbilityType.DAMAGE, 20),
            new Ability("Backstab", "Strike a vital point and deal 35 points of physical damage", 15, AbilityType.DAMAGE, 35),
            new Ability("Focus", "Take a moment to concentrate, restoring 10 EP", 0, AbilityType.EP_RESTORE, 10),
            new Ability("Smoke Bomb", "50% chance of evading any incoming attacks this round", 15, AbilityType.EVASION),
            new Ability("Sneak Attack", "Evade opponent's attacks and deal 45 physical damage", 25, AbilityType.SNEAK_ATTACK, 45)
    )),

    WARRIOR("Warrior", Arrays.asList(
            new Ability("Cleave", "A sweeping strike that deals 20 physical damage", 5, AbilityType.DAMAGE, 20),
            new Ability("Shield Bash", "Slam shield into opponent, dealing 35 physical damage", 15, AbilityType.DAMAGE, 35),
            new Ability("Ironclad Defense", "Brace yourself, taking no damage for the current round", 15, AbilityType.SHIELD),
            new Ability("Bloodlust", "Tap into inner fury, restoring 30 HP", 12, AbilityType.HEAL, 30),
            new Ability("Rallying Cry", "Let out a powerful shout, recovering 12 EP", 0, AbilityType.EP_RESTORE, 12)
    ));

    private String displayName;
    private List<Ability> abilities;

    /**
     * Constructor for CharacterClass enum
     * @param displayName the display name of the class
     * @param abilities list of class abilities
     */
    CharacterClass(String displayName, List<Ability> abilities) {
        this.displayName = displayName;
        this.abilities = abilities;
    }

    public String getDisplayName() { return displayName; }
    public List<Ability> getAbilities() { return abilities; }
}