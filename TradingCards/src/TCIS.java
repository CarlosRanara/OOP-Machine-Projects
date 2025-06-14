import java.util.*;

/**
 * Main Trading Card Inventory System class that manages the entire system.
 * Provides command-line interface for all operations.
 */
public class TCIS {
    private Collection collection;
    private ArrayList<Binder> binders;
    private ArrayList<Deck> decks;
    private Scanner scanner;

    /**
     * Constructs a new TCIS instance.
     */
    public TCIS() {
        this.collection = new Collection();
        this.binders = new ArrayList<>();
        this.decks = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Main method to start the TCIS application.
     */
    public static void main(String[] args) {
        TCIS tcis = new TCIS();
        tcis.run();
    }

    /**
     * Main application loop.
     */
    public void run() {
        System.out.println("Welcome to Trading Card Inventory System!");

        while (true) {
            displayMainMenu();
            int choice = getValidChoice();

            if (choice == -1) {
                break;
            }

            processMainMenuChoice(choice);
        }

        System.out.println("Thank you for using TCIS!");
        scanner.close();
    }

    /**
     * Displays the main menu based on current system state.
     */
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Add a Card");

        if (!collection.isEmpty()) {
            System.out.println("2. Increase/Decrease Card Count");
            System.out.println("3. Display a Card/Display Collection");
        }

        if (binders.isEmpty()) {
            System.out.println("4. Create a new Binder");
        } else {
            System.out.println("4. Manage Binders");
        }

        if (decks.isEmpty()) {
            System.out.println("5. Create a new Deck");
        } else {
            System.out.println("5. Manage Decks");
        }

        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Processes the main menu choice.
     * @param choice the user's menu choice
     */
    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addCard();
                break;
            case 2:
                if (!collection.isEmpty()) {
                    manageCardCount();
                } else {
                    System.out.println("Invalid choice.");
                }
                break;
            case 3:
                if (!collection.isEmpty()) {
                    displayMenu();
                } else {
                    System.out.println("Invalid choice.");
                }
                break;
            case 4:
                if (binders.isEmpty()) {
                    createBinder();
                } else {
                    manageBinders();
                }
                break;
            case 5:
                if (decks.isEmpty()) {
                    createDeck();
                } else {
                    manageDecks();
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Handles adding a new card to the collection.
     */
    private void addCard() {
        System.out.println("\n=== ADD CARD ===");

        System.out.print("Enter card name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Card name cannot be empty.");
            return;
        }

        // Check if card already exists
        Card existingCard = collection.findCard(name);
        if (existingCard != null) {
            System.out.println("Card '" + name + "' already exists in collection.");
            System.out.print("Would you like to increase the count instead? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                existingCard.increaseCount(1);
                System.out.println("Card count increased. New count: " + existingCard.getCount());
            }
            return;
        }

        String rarity = getRarityInput();
        if (rarity == null) return;

        String variant = getVariantInput(rarity);
        if (variant == null) return;

        double baseValue = getValueInput();
        if (baseValue < 0) return;

        Card newCard = new Card(name, rarity, variant, baseValue);
        collection.addCard(newCard);

        System.out.println("Card added successfully!");
        System.out.println("Total value: $" + String.format("%.2f", newCard.calculateTotalValue()));
    }

    /**
     * Gets valid rarity input from user.
     * @return valid rarity string or null if cancelled
     */
    private String getRarityInput() {
        while (true) {
            System.out.print("Enter rarity (common/uncommon/rare/legendary) or 'back' to return: ");
            String rarity = scanner.nextLine().trim().toLowerCase();

            if (rarity.equals("back")) {
                return null;
            }

            if (rarity.equals("common") || rarity.equals("uncommon") ||
                    rarity.equals("rare") || rarity.equals("legendary")) {
                return rarity;
            }

            System.out.println("Invalid rarity. Please enter: common, uncommon, rare, or legendary");
        }
    }

    /**
     * Gets valid variant input based on rarity.
     * @param rarity the card's rarity
     * @return valid variant string or null if cancelled
     */
    private String getVariantInput(String rarity) {
        if (rarity.equals("common") || rarity.equals("uncommon")) {
            return "normal";
        }

        while (true) {
            System.out.print("Enter variant (normal/extended-art/full-art/alt-art) or 'back' to return: ");
            String variant = scanner.nextLine().trim().toLowerCase();

            if (variant.equals("back")) {
                return null;
            }

            if (variant.equals("normal") || variant.equals("extended-art") ||
                    variant.equals("full-art") || variant.equals("alt-art")) {
                return variant;
            }

            System.out.println("Invalid variant. Please enter: normal, extended-art, full-art, or alt-art");
        }
    }

    /**
     * Gets valid value input from user.
     * @return valid value or -1 if cancelled
     */
    private double getValueInput() {
        while (true) {
            System.out.print("Enter base value (dollars) or 'back' to return: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return -1;
            }

            try {
                double value = Double.parseDouble(input);
                if (value >= 0) {
                    return value;
                } else {
                    System.out.println("Value must be non-negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid value. Please enter a number.");
            }
        }
    }

    /**
     * Handles increasing/decreasing card count.
     */
    private void manageCardCount() {
        System.out.println("\n=== MANAGE CARD COUNT ===");
        collection.displayCollection();

        System.out.print("Enter card name or 'back' to return: ");
        String name = scanner.nextLine().trim();
        if (name.equalsIgnoreCase("back")) return;

        Card card = collection.findCard(name);
        if (card == null) {
            System.out.println("Card not found.");
            return;
        }

        System.out.println("Current count: " + card.getCount());
        System.out.print("Enter new count or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int newCount = Integer.parseInt(input);
            if (newCount >= 0) {
                card.setCount(newCount);
                System.out.println("Count updated successfully.");
                if (newCount == 0) {
                    System.out.println("Note: Card with count 0 cannot be added to decks or binders.");
                }
            } else {
                System.out.println("Count must be non-negative.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid count. Please enter a number.");
        }
    }

    /**
     * Handles display menu for cards and collection.
     */
    private void displayMenu() {
        while (true) {
            System.out.println("\n=== DISPLAY MENU ===");
            System.out.println("1. Display a specific card");
            System.out.println("2. Display entire collection");
            System.out.println("3. Go back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getValidChoice();
            if (choice == 3) break;

            switch (choice) {
                case 1:
                    System.out.print("Enter card name: ");
                    String name = scanner.nextLine().trim();
                    collection.displayCard(name);
                    break;
                case 2:
                    collection.displayCollection();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Creates a new binder.
     */
    private void createBinder() {
        System.out.println("\n=== CREATE BINDER ===");
        System.out.print("Enter binder name or 'back' to return: ");
        String name = scanner.nextLine().trim();
        if (name.equalsIgnoreCase("back")) return;

        if (name.isEmpty()) {
            System.out.println("Binder name cannot be empty.");
            return;
        }

        binders.add(new Binder(name));
        System.out.println("Binder '" + name + "' created successfully!");
    }

    /**
     * Manages all binder operations.
     */
    private void manageBinders() {
        while (true) {
            System.out.println("\n=== MANAGE BINDERS ===");
            System.out.println("1. Create a new Binder");
            System.out.println("2. Delete a Binder");
            System.out.println("3. Add Card to Binder");
            System.out.println("4. Remove Card from Binder");
            System.out.println("5. Trade Card");
            System.out.println("6. View Binder");
            System.out.println("7. Go back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getValidChoice();
            if (choice == 7) break;

            switch (choice) {
                case 1:
                    createBinder();
                    break;
                case 2:
                    deleteBinder();
                    break;
                case 3:
                    addCardToBinder();
                    break;
                case 4:
                    removeCardFromBinder();
                    break;
                case 5:
                    tradeCard();
                    break;
                case 6:
                    viewBinder();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Deletes a binder and returns all cards to collection.
     */
    private void deleteBinder() {
        if (binders.isEmpty()) {
            System.out.println("No binders to delete.");
            return;
        }

        System.out.println("\n=== DELETE BINDER ===");
        displayBinders();

        System.out.print("Enter binder number to delete or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < binders.size()) {
                Binder binder = binders.get(index);
                binder.returnAllCards(collection);
                binders.remove(index);
                System.out.println("Binder deleted and all cards returned to collection.");
            } else {
                System.out.println("Invalid binder number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Adds a card from collection to a binder.
     */
    private void addCardToBinder() {
        if (binders.isEmpty()) {
            System.out.println("No binders available. Create a binder first.");
            return;
        }

        if (!collection.hasAvailableCards()) {
            System.out.println("No available cards in collection.");
            return;
        }

        System.out.println("\n=== ADD CARD TO BINDER ===");

        Binder selectedBinder = selectBinder();
        if (selectedBinder == null) return;

        if (selectedBinder.isFull()) {
            System.out.println("Binder is full (20 cards maximum).");
            return;
        }

        System.out.println("\nAvailable cards:");
        ArrayList<Card> availableCards = collection.getAvailableCards();
        for (int i = 0; i < availableCards.size(); i++) {
            Card card = availableCards.get(i);
            System.out.println((i + 1) + ". " + card.getName() + " (x" + card.getCount() + ")");
        }

        System.out.print("Enter card number or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int cardIndex = Integer.parseInt(input) - 1;
            if (cardIndex >= 0 && cardIndex < availableCards.size()) {
                Card selectedCard = availableCards.get(cardIndex);
                Card cardCopy = collection.removeCardFromCollection(selectedCard.getName());
                if (cardCopy != null && selectedBinder.addCard(cardCopy)) {
                    System.out.println("Card added to binder successfully.");
                } else {
                    collection.returnCardToCollection(cardCopy);
                    System.out.println("Failed to add card to binder.");
                }
            } else {
                System.out.println("Invalid card number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Removes a card from a binder and returns it to collection.
     */
    private void removeCardFromBinder() {
        if (binders.isEmpty()) {
            System.out.println("No binders available.");
            return;
        }

        System.out.println("\n=== REMOVE CARD FROM BINDER ===");

        Binder selectedBinder = selectBinder();
        if (selectedBinder == null) return;

        if (selectedBinder.isEmpty()) {
            System.out.println("Binder is empty.");
            return;
        }

        selectedBinder.viewBinder();

        System.out.print("Enter card number to remove or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int index = Integer.parseInt(input);
            Card removedCard = selectedBinder.removeCard(index);
            if (removedCard != null) {
                collection.returnCardToCollection(removedCard);
                System.out.println("Card removed from binder and returned to collection.");
            } else {
                System.out.println("Invalid card number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Handles card trading functionality.
     */
    private void tradeCard() {
        if (binders.isEmpty()) {
            System.out.println("No binders available.");
            return;
        }

        System.out.println("\n=== TRADE CARD ===");

        Binder selectedBinder = selectBinder();
        if (selectedBinder == null) return;

        if (selectedBinder.isEmpty()) {
            System.out.println("Binder is empty.");
            return;
        }

        selectedBinder.viewBinder();

        System.out.print("Enter card number to trade away or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int outgoingIndex = Integer.parseInt(input);
            Card outgoingCard = selectedBinder.getCard(outgoingIndex);
            if (outgoingCard == null) {
                System.out.println("Invalid card number.");
                return;
            }

            System.out.println("Trading away: " + outgoingCard.getName() +
                    " (Value: $" + String.format("%.2f", outgoingCard.calculateTotalValue()) + ")");

            // Get incoming card details
            System.out.println("\nEnter details for incoming card:");
            Card incomingCard = createNewCard();
            if (incomingCard == null) return;

            // Check value difference
            double valueDifference = Math.abs(outgoingCard.calculateTotalValue() - incomingCard.calculateTotalValue());

            if (valueDifference >= 1.0) {
                System.out.printf("Value difference: $%.2f\n", valueDifference);
                System.out.print("The value difference is $1.00 or more. Do you want to cancel the trade? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("y") || response.equals("yes")) {
                    System.out.println("Trade cancelled.");
                    return;
                }
            }

            // Execute trade
            collection.addCard(incomingCard);
            Card incomingCopy = collection.removeCardFromCollection(incomingCard.getName());
            if (incomingCopy != null) {
                selectedBinder.replaceCard(outgoingIndex, incomingCopy);
                System.out.println("Trade completed successfully!");
            } else {
                System.out.println("Trade failed.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Creates a new card from user input.
     * @return new Card object or null if cancelled
     */
    private Card createNewCard() {
        System.out.print("Enter card name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Card name cannot be empty.");
            return null;
        }

        String rarity = getRarityInput();
        if (rarity == null) return null;

        String variant = getVariantInput(rarity);
        if (variant == null) return null;

        double baseValue = getValueInput();
        if (baseValue < 0) return null;

        return new Card(name, rarity, variant, baseValue);
    }

    /**
     * Displays and allows user to select a binder.
     * @return selected Binder or null if cancelled
     */
    private Binder selectBinder() {
        displayBinders();

        System.out.print("Enter binder number or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return null;

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < binders.size()) {
                return binders.get(index);
            } else {
                System.out.println("Invalid binder number.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    /**
     * Displays all available binders.
     */
    private void displayBinders() {
        System.out.println("\nAvailable binders:");
        for (int i = 0; i < binders.size(); i++) {
            Binder binder = binders.get(i);
            System.out.println((i + 1) + ". " + binder.getName() +
                    " (" + binder.getCurrentSize() + "/20 cards)");
        }
    }

    /**
     * Views contents of a selected binder.
     */
    private void viewBinder() {
        if (binders.isEmpty()) {
            System.out.println("No binders available.");
            return;
        }

        System.out.println("\n=== VIEW BINDER ===");

        Binder selectedBinder = selectBinder();
        if (selectedBinder != null) {
            selectedBinder.viewBinder();
        }
    }

    /**
     * Creates a new deck.
     */
    private void createDeck() {
        System.out.println("\n=== CREATE DECK ===");
        System.out.print("Enter deck name or 'back' to return: ");
        String name = scanner.nextLine().trim();
        if (name.equalsIgnoreCase("back")) return;

        if (name.isEmpty()) {
            System.out.println("Deck name cannot be empty.");
            return;
        }

        decks.add(new Deck(name));
        System.out.println("Deck '" + name + "' created successfully!");
    }

    /**
     * Manages all deck operations.
     */
    private void manageDecks() {
        while (true) {
            System.out.println("\n=== MANAGE DECKS ===");
            System.out.println("1. Create a new Deck");
            System.out.println("2. Delete a Deck");
            System.out.println("3. Add Card to Deck");
            System.out.println("4. Remove Card from Deck");
            System.out.println("5. View Deck");
            System.out.println("6. Go back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getValidChoice();
            if (choice == 6) break;

            switch (choice) {
                case 1:
                    createDeck();
                    break;
                case 2:
                    deleteDeck();
                    break;
                case 3:
                    addCardToDeck();
                    break;
                case 4:
                    removeCardFromDeck();
                    break;
                case 5:
                    viewDeck();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Deletes a deck and returns all cards to collection.
     */
    private void deleteDeck() {
        if (decks.isEmpty()) {
            System.out.println("No decks to delete.");
            return;
        }

        System.out.println("\n=== DELETE DECK ===");
        displayDecks();

        System.out.print("Enter deck number to delete or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < decks.size()) {
                Deck deck = decks.get(index);
                deck.returnAllCards(collection);
                decks.remove(index);
                System.out.println("Deck deleted and all cards returned to collection.");
            } else {
                System.out.println("Invalid deck number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Adds a card from collection to a deck.
     */
    private void addCardToDeck() {
        if (decks.isEmpty()) {
            System.out.println("No decks available. Create a deck first.");
            return;
        }

        if (!collection.hasAvailableCards()) {
            System.out.println("No available cards in collection.");
            return;
        }

        System.out.println("\n=== ADD CARD TO DECK ===");

        Deck selectedDeck = selectDeck();
        if (selectedDeck == null) return;

        if (selectedDeck.isFull()) {
            System.out.println("Deck is full (10 cards maximum).");
            return;
        }

        System.out.println("\nAvailable cards:");
        ArrayList<Card> availableCards = collection.getAvailableCards();
        for (int i = 0; i < availableCards.size(); i++) {
            Card card = availableCards.get(i);
            System.out.println((i + 1) + ". " + card.getName() + " (x" + card.getCount() + ")");
        }

        System.out.print("Enter card number or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int cardIndex = Integer.parseInt(input) - 1;
            if (cardIndex >= 0 && cardIndex < availableCards.size()) {
                Card selectedCard = availableCards.get(cardIndex);

                if (selectedDeck.containsCard(selectedCard.getName())) {
                    System.out.println("A card with that name is already in the deck. Only one copy allowed per deck.");
                    return;
                }

                Card cardCopy = collection.removeCardFromCollection(selectedCard.getName());
                if (cardCopy != null && selectedDeck.addCard(cardCopy)) {
                    System.out.println("Card added to deck successfully.");
                } else {
                    collection.returnCardToCollection(cardCopy);
                    System.out.println("Failed to add card to deck.");
                }
            } else {
                System.out.println("Invalid card number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Removes a card from a deck and returns it to collection.
     */
    private void removeCardFromDeck() {
        if (decks.isEmpty()) {
            System.out.println("No decks available.");
            return;
        }

        System.out.println("\n=== REMOVE CARD FROM DECK ===");

        Deck selectedDeck = selectDeck();
        if (selectedDeck == null) return;

        if (selectedDeck.isEmpty()) {
            System.out.println("Deck is empty.");
            return;
        }

        selectedDeck.viewDeck();

        System.out.print("Enter card number to remove or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return;

        try {
            int index = Integer.parseInt(input);
            Card removedCard = selectedDeck.removeCard(index);
            if (removedCard != null) {
                collection.returnCardToCollection(removedCard);
                System.out.println("Card removed from deck and returned to collection.");
            } else {
                System.out.println("Invalid card number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Views contents of a selected deck with detailed card viewing option.
     */
    private void viewDeck() {
        if (decks.isEmpty()) {
            System.out.println("No decks available.");
            return;
        }

        System.out.println("\n=== VIEW DECK ===");

        Deck selectedDeck = selectDeck();
        if (selectedDeck == null) return;

        while (true) {
            selectedDeck.viewDeck();

            if (selectedDeck.isEmpty()) {
                break;
            }

            System.out.println("\nOptions:");
            System.out.println("1. View card details");
            System.out.println("2. Go back");
            System.out.print("Enter your choice: ");

            int choice = getValidChoice();
            if (choice == 2) break;

            if (choice == 1) {
                System.out.print("Enter card number to view details: ");
                String input = scanner.nextLine().trim();
                try {
                    int index = Integer.parseInt(input);
                    Card card = selectedDeck.getCard(index);
                    if (card != null) {
                        displayCardDetails(card);
                    } else {
                        System.out.println("Invalid card number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Displays detailed information about a card.
     * @param card the card to display
     */
    private void displayCardDetails(Card card) {
        System.out.println("\n=== CARD DETAILS ===");
        System.out.println("Name: " + card.getName());
        System.out.println("Rarity: " + card.getRarity());
        System.out.println("Variant: " + card.getVariant());
        System.out.println("Base Value: $" + String.format("%.2f", card.getBaseValue()));
        System.out.println("Total Value: $" + String.format("%.2f", card.calculateTotalValue()));
    }

    /**
     * Displays and allows user to select a deck.
     * @return selected Deck or null if cancelled
     */
    private Deck selectDeck() {
        displayDecks();

        System.out.print("Enter deck number or 'back' to return: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) return null;

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < decks.size()) {
                return decks.get(index);
            } else {
                System.out.println("Invalid deck number.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    /**
     * Displays all available decks.
     */
    private void displayDecks() {
        System.out.println("\nAvailable decks:");
        for (int i = 0; i < decks.size(); i++) {
            Deck deck = decks.get(i);
            System.out.println((i + 1) + ". " + deck.getName() +
                    " (" + deck.getSize() + "/10 cards)");
        }
    }

    /**
     * Gets a valid integer choice from user input.
     * @return valid choice or -1 if exit requested
     */
    private int getValidChoice() {
        try {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                return -1;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -999; // Invalid choice indicator
        }
    }
}