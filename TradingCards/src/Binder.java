import java.util.*;

/**
 * Represents a binder that can hold up to 20 cards for trading.
 */
public class Binder {
    private String name;
    private Card[] slots;
    private int currentSize;
    private static final int MAX_CAPACITY = 20;

    /**
     * Constructs a new binder with specified name.
     * @param name the name of the binder
     */
    public Binder(String name) {
        this.name = name;
        this.slots = new Card[MAX_CAPACITY];
        this.currentSize = 0;
    }

    /**
     * Adds a card to the binder.
     * @param card the card to add
     * @return true if successfully added, false if binder is full
     */
    public boolean addCard(Card card) {
        if (currentSize < MAX_CAPACITY) {
            slots[currentSize] = card;
            currentSize++;
            return true;
        }
        return false;
    }

    /**
     * Removes a card from the binder at specified index.
     * @param index the index of the card to remove (1-based)
     * @return the removed card, null if invalid index
     */
    public Card removeCard(int index) {
        if (index >= 1 && index <= currentSize) {
            Card removedCard = slots[index - 1];

            // Shift remaining cards
            for (int i = index - 1; i < currentSize - 1; i++) {
                slots[i] = slots[i + 1];
            }
            slots[currentSize - 1] = null;
            currentSize--;

            return removedCard;
        }
        return null;
    }

    /**
     * Gets a card at specified index for trading.
     * @param index the index of the card (1-based)
     * @return the card at index, null if invalid
     */
    public Card getCard(int index) {
        if (index >= 1 && index <= currentSize) {
            return slots[index - 1];
        }
        return null;
    }

    /**
     * Replaces a card at specified index (used in trading).
     * @param index the index to replace (1-based)
     * @param newCard the new card
     * @return true if successful, false if invalid index
     */
    public boolean replaceCard(int index, Card newCard) {
        if (index >= 1 && index <= currentSize) {
            slots[index - 1] = newCard;
            return true;
        }
        return false;
    }

    /**
     * Displays all cards in the binder alphabetically.
     */
    public void viewBinder() {
        if (currentSize == 0) {
            System.out.println("Binder '" + name + "' is empty.");
            return;
        }

        // Create list of cards with their indices for sorting
        ArrayList<String> cardList = new ArrayList<>();
        for (int i = 0; i < currentSize; i++) {
            cardList.add((i + 1) + ". " + slots[i].getName());
        }

        // Sort alphabetically by card name
        cardList.sort((s1, s2) -> {
            String name1 = s1.substring(s1.indexOf(". ") + 2);
            String name2 = s2.substring(s2.indexOf(". ") + 2);
            return name1.compareToIgnoreCase(name2);
        });

        System.out.println("\n=== BINDER: " + name + " ===");
        for (String cardEntry : cardList) {
            System.out.println(cardEntry);
        }
    }

    /**
     * Returns all cards in binder to collection.
     * @param collection the collection to return cards to
     */
    public void returnAllCards(Collection collection) {
        for (int i = 0; i < currentSize; i++) {
            if (slots[i] != null) {
                collection.returnCardToCollection(slots[i]);
            }
        }
        currentSize = 0;
        Arrays.fill(slots, null);
    }

    public String getName() { return name; }
    public int getCurrentSize() { return currentSize; }
    public boolean isFull() { return currentSize >= MAX_CAPACITY; }
    public boolean isEmpty() { return currentSize == 0; }
}