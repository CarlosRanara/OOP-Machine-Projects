import java.util.*;

/**
 * Manages the collector's card collection with unlimited capacity.
 */
public class Collection {
    private ArrayList<Card> cards;

    /**
     * Constructs a new empty collection.
     */
    public Collection() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the collection or increases count if card already exists.
     * @param newCard the card to add
     * @return true if new card added, false if count increased
     */
    public boolean addCard(Card newCard) {
        for (Card card : cards) {
            if (card.getName().equals(newCard.getName())) {
                card.increaseCount(1);
                return false;
            }
        }
        cards.add(newCard);
        return true;
    }

    /**
     * Finds a card by name.
     * @param name the name of the card to find
     * @return the Card object if found, null otherwise
     */
    public Card findCard(String name) {
        for (Card card : cards) {
            if (card.getName().equalsIgnoreCase(name)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Displays the entire collection sorted alphabetically by name.
     */
    public void displayCollection() {
        if (cards.isEmpty()) {
            System.out.println("Collection is empty.");
            return;
        }

        // Sort cards alphabetically by name
        ArrayList<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        System.out.println("\n=== YOUR COLLECTION ===");
        for (Card card : sortedCards) {
            System.out.println(card.getName() + " x" + card.getCount());
        }
    }

    /**
     * Displays detailed information about a specific card.
     * @param name the name of the card to display
     */
    public void displayCard(String name) {
        Card card = findCard(name);
        if (card != null) {
            System.out.println("\n=== CARD DETAILS ===");
            System.out.println("Name: " + card.getName());
            System.out.println("Rarity: " + card.getRarity());
            System.out.println("Variant: " + card.getVariant());
            System.out.println("Base Value: $" + String.format("%.2f", card.getBaseValue()));
            System.out.println("Total Value: $" + String.format("%.2f", card.calculateTotalValue()));
            System.out.println("Count: " + card.getCount());
        } else {
            System.out.println("Card not found in collection.");
        }
    }

    /**
     * Removes one copy of a card from collection (for adding to binder/deck).
     * @param name the name of the card to remove
     * @return the Card object if successfully removed, null otherwise
     */
    public Card removeCardFromCollection(String name) {
        Card card = findCard(name);
        if (card != null && card.isAvailable()) {
            card.decreaseCount(1);
            return card.createCopy();
        }
        return null;
    }

    /**
     * Returns a card to the collection (from binder/deck).
     * @param returnedCard the card to return
     */
    public void returnCardToCollection(Card returnedCard) {
        Card existing = findCard(returnedCard.getName());
        if (existing != null) {
            existing.increaseCount(1);
        } else {
            cards.add(returnedCard);
        }
    }

    /**
     * Gets all available cards for selection.
     * @return list of cards with count > 0
     */
    public ArrayList<Card> getAvailableCards() {
        ArrayList<Card> available = new ArrayList<>();
        for (Card card : cards) {
            if (card.isAvailable()) {
                available.add(card);
            }
        }
        return available;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean hasAvailableCards() {
        return !getAvailableCards().isEmpty();
    }
}