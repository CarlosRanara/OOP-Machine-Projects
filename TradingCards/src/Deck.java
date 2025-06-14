import java.util.*;

/**
 * Represents a deck that can hold up to 10 unique cards.
 */
public class Deck {
    private String name;
    private ArrayList<Card> cards;
    private static final int MAX_CAPACITY = 10;

    /**
     * Constructs a new deck with specified name.
     * @param name the name of the deck
     */
    public Deck(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the deck if it doesn't already exist.
     * @param card the card to add
     * @return true if successfully added, false if duplicate or full
     */
    public boolean addCard(Card card) {
        if (cards.size() >= MAX_CAPACITY) {
            return false;
        }

        if (containsCard(card.getName())) {
            return false;
        }

        cards.add(card);
        return true;
    }

    /**
     * Removes a card from the deck by name.
     * @param cardName the name of the card to remove
     * @return the removed card, null if not found
     */
    public Card removeCard(String cardName) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getName().equalsIgnoreCase(cardName)) {
                return cards.remove(i);
            }
        }
        return null;
    }

    /**
     * Removes a card from the deck by index.
     * @param index the index of the card to remove (1-based)
     * @return the removed card, null if invalid index
     */
    public Card removeCard(int index) {
        if (index >= 1 && index <= cards.size()) {
            return cards.remove(index - 1);
        }
        return null;
    }

    /**
     * Checks if deck contains a card with given name.
     * @param cardName the name to check
     * @return true if card exists in deck
     */
    public boolean containsCard(String cardName) {
        for (Card card : cards) {
            if (card.getName().equalsIgnoreCase(cardName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays all cards in the deck with selection options.
     */
    public void viewDeck() {
        if (cards.isEmpty()) {
            System.out.println("Deck '" + name + "' is empty.");
            return;
        }

        System.out.println("\n=== DECK: " + name + " ===");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i).getName());
        }
    }

    /**
     * Gets a card by index for detailed viewing.
     * @param index the index of the card (1-based)
     * @return the card at index, null if invalid
     */
    public Card getCard(int index) {
        if (index >= 1 && index <= cards.size()) {
            return cards.get(index - 1);
        }
        return null;
    }

    /**
     * Returns all cards in deck to collection.
     * @param collection the collection to return cards to
     */
    public void returnAllCards(Collection collection) {
        for (Card card : cards) {
            collection.returnCardToCollection(card);
        }
        cards.clear();
    }

    public String getName() { return name; }
    public int getSize() { return cards.size(); }
    public boolean isFull() { return cards.size() >= MAX_CAPACITY; }
    public boolean isEmpty() { return cards.isEmpty(); }
}