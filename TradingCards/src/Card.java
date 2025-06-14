/**
 * Represents a trading card with its properties and operations.
 * Each card has a unique name that serves as its identifier.
 */
public class Card {
    private String name;
    private String rarity;
    private String variant;
    private double baseValue;
    private int count;

    /**
     * Constructs a new Card with specified properties.
     * @param name the unique name of the card
     * @param rarity the rarity level (common, uncommon, rare, legendary)
     * @param variant the variant type (normal, extended-art, full-art, alt-art)
     * @param baseValue the base dollar value before variant multiplier
     */
    public Card(String name, String rarity, String variant, double baseValue) {
        this.name = name;
        this.rarity = rarity;
        this.variant = variant;
        this.baseValue = baseValue;
        this.count = 1;
    }

    /**
     * Calculates the total value of the card including variant multiplier.
     * @return the calculated value with variant bonus applied
     */
    public double calculateTotalValue() {
        double multiplier = 1.0;
        switch (variant.toLowerCase()) {
            case "extended-art":
                multiplier = 1.5;
                break;
            case "full-art":
                multiplier = 2.0;
                break;
            case "alt-art":
                multiplier = 3.0;
                break;
            default:
                multiplier = 1.0;
                break;
        }
        return baseValue * multiplier;
    }

    /**
     * Increases the count of this card.
     * @param amount the amount to increase
     */
    public void increaseCount(int amount) {
        this.count += amount;
    }

    /**
     * Decreases the count of this card.
     * @param amount the amount to decrease
     */
    public void decreaseCount(int amount) {
        this.count = Math.max(0, this.count - amount);
    }

    /**
     * Checks if the card is available (count > 0).
     * @return true if count > 0, false otherwise
     */
    public boolean isAvailable() {
        return count > 0;
    }

    /**
     * Creates a copy of this card for use in binders/decks.
     * @return a new Card instance with the same properties but count 1
     */
    public Card createCopy() {
        return new Card(this.name, this.rarity, this.variant, this.baseValue);
    }

    // Getters
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public String getVariant() { return variant; }
    public double getBaseValue() { return baseValue; }
    public int getCount() { return count; }

    // Setters
    public void setCount(int count) { this.count = Math.max(0, count); }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - $%.2f x%d",
                name, rarity, variant, calculateTotalValue(), count);
    }
}