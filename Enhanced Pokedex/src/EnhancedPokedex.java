//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.*;

/**
 * Enhanced Pokédex Application
 * A comprehensive system for managing Pokémon, moves, items, and trainers
 *
 * @author CCPROG3 Project Team
 * @version 1.0
 */

// ============================================================================
// POKEMON CLASS
// ============================================================================

/**
 * Represents a Pokémon with all its attributes and behaviors
 */
class Pokemon {
    private int pokedexNumber;
    private String name;
    private String type1;
    private String type2; // Optional
    private int baseLevel;
    private int evolvesFrom; // Pokédex number
    private int evolvesTo;   // Pokédex number
    private int evolutionLevel;

    // Base stats
    private int hp;
    private int attack;
    private int defense;
    private int speed;

    private ArrayList<Move> moveSet;
    private Item heldItem;
    private int currentLevel;

    /**
     * Constructor for Pokemon
     */
    public Pokemon(int pokedexNumber, String name, String type1, String type2,
                   int baseLevel, int evolvesFrom, int evolvesTo, int evolutionLevel,
                   int hp, int attack, int defense, int speed) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.baseLevel = baseLevel;
        this.evolvesFrom = evolvesFrom;
        this.evolvesTo = evolvesTo;
        this.evolutionLevel = evolutionLevel;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.currentLevel = baseLevel;
        this.moveSet = new ArrayList<>();
        this.heldItem = null;

        // Add default moves
        moveSet.add(new Move("Tackle", "A physical attack", "Normal", null));
        moveSet.add(new Move("Defend", "Raises defense", "Normal", null));
    }

    /**
     * Makes the Pokémon cry (play sound)
     */
    public void cry() {
        System.out.println(name + " says: " + name.toUpperCase() + "!");
    }

    /**
     * Levels up the Pokémon, increasing stats by 10%
     */
    public void levelUp() {
        currentLevel++;
        hp = (int)(hp * 1.1);
        attack = (int)(attack * 1.1);
        defense = (int)(defense * 1.1);
        speed = (int)(speed * 1.1);

        System.out.println(name + " leveled up to level " + currentLevel + "!");

        // Check for evolution
        if (evolvesTo != 0 && currentLevel >= evolutionLevel) {
            System.out.println(name + " is ready to evolve!");
        }
    }

    /**
     * Teaches a new move to the Pokémon
     */
    public boolean teachMove(Move move) {
        // Check type compatibility
        if (!isCompatibleMove(move)) {
            System.out.println(name + " cannot learn " + move.getName() + " - incompatible type!");
            return false;
        }

        if (moveSet.size() < 4) {
            moveSet.add(move);
            System.out.println(name + " learned " + move.getName() + "!");
            return true;
        } else {
            System.out.println(name + " already knows 4 moves. Replace one? (Implementation needed)");
            return false;
        }
    }

    /**
     * Checks if a move is compatible with this Pokémon's types
     */
    private boolean isCompatibleMove(Move move) {
        return move.getType1().equals(type1) ||
                (type2 != null && move.getType1().equals(type2)) ||
                (move.getType2() != null && move.getType2().equals(type1)) ||
                (move.getType2() != null && type2 != null && move.getType2().equals(type2));
    }

    /**
     * Sets the held item (discards current item if any)
     */
    public void setHeldItem(Item item) {
        if (heldItem != null) {
            System.out.println(name + " discarded " + heldItem.getName());
        }
        this.heldItem = item;
        if (item != null) {
            System.out.println(name + " is now holding " + item.getName());
        }
    }

    // Getters
    public int getPokedexNumber() { return pokedexNumber; }
    public String getName() { return name; }
    public String getType1() { return type1; }
    public String getType2() { return type2; }
    public int getCurrentLevel() { return currentLevel; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public ArrayList<Move> getMoveSet() { return moveSet; }
    public Item getHeldItem() { return heldItem; }
    public int getEvolvesTo() { return evolvesTo; }
    public int getEvolutionLevel() { return evolutionLevel; }

    @Override
    public String toString() {
        return String.format("#%03d %s (Lv.%d) - %s%s",
                pokedexNumber, name, currentLevel, type1,
                type2 != null ? "/" + type2 : "");
    }
}

// ============================================================================
// MOVE CLASS
// ============================================================================

/**
 * Represents a Pokémon move
 */
class Move {
    private String name;
    private String description;
    private String classification; // HM or TM
    private String type1;
    private String type2; // Optional

    public Move(String name, String description, String type1, String type2) {
        this.name = name;
        this.description = description;
        this.type1 = type1;
        this.type2 = type2;
        this.classification = "Normal"; // Default
    }

    public Move(String name, String description, String classification, String type1, String type2) {
        this.name = name;
        this.description = description;
        this.classification = classification;
        this.type1 = type1;
        this.type2 = type2;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getClassification() { return classification; }
    public String getType1() { return type1; }
    public String getType2() { return type2; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, type1, description);
    }
}

// ============================================================================
// ITEM CLASS
// ============================================================================

/**
 * Represents an item in the Pokémon world
 */
class Item {
    private String name;
    private String category;
    private String description;
    private String effects;
    private int buyingPrice;
    private int sellingPrice;

    public Item(String name, String category, String description, String effects,
                int buyingPrice, int sellingPrice) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.effects = effects;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    /**
     * Applies the item's effect to a Pokémon
     */
    public void useOn(Pokemon pokemon) {
        switch (name) {
            case "Rare Candy":
                pokemon.levelUp();
                break;
            case "HP Up":
                // Increase HP EVs (simplified implementation)
                System.out.println(pokemon.getName() + "'s HP increased!");
                break;
            case "Protein":
                System.out.println(pokemon.getName() + "'s Attack increased!");
                break;
            case "Iron":
                System.out.println(pokemon.getName() + "'s Defense increased!");
                break;
            case "Carbos":
                System.out.println(pokemon.getName() + "'s Speed increased!");
                break;
            default:
                System.out.println("Used " + name + " on " + pokemon.getName());
        }
    }

    // Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getEffects() { return effects; }
    public int getBuyingPrice() { return buyingPrice; }
    public int getSellingPrice() { return sellingPrice; }

    @Override
    public String toString() {
        return String.format("%s (%s) - ₽%d", name, category, buyingPrice);
    }
}

// ============================================================================
// TRAINER CLASS
// ============================================================================

/**
 * Represents a Pokémon trainer
 */
class Trainer {
    private int trainerId;
    private String name;
    private String birthdate;
    private String sex;
    private String hometown;
    private String description;
    private double money;

    private ArrayList<Pokemon> activeLineup;
    private ArrayList<Pokemon> storage;
    private HashMap<String, Integer> inventory; // Item name -> quantity

    public Trainer(int trainerId, String name, String birthdate, String sex,
                   String hometown, String description) {
        this.trainerId = trainerId;
        this.name = name;
        this.birthdate = birthdate;
        this.sex = sex;
        this.hometown = hometown;
        this.description = description;
        this.money = 1000000.0; // Starting funds

        this.activeLineup = new ArrayList<>();
        this.storage = new ArrayList<>();
        this.inventory = new HashMap<>();
    }

    /**
     * Buys an item from the shop
     */
    public boolean buyItem(Item item) {
        if (money >= item.getBuyingPrice()) {
            // Check inventory limits
            int totalItems = inventory.values().stream().mapToInt(Integer::intValue).sum();
            int uniqueItems = inventory.size();

            if (totalItems >= 50) {
                System.out.println("Inventory full! (50 item limit)");
                return false;
            }

            if (!inventory.containsKey(item.getName()) && uniqueItems >= 10) {
                System.out.println("Too many unique items! (10 type limit)");
                return false;
            }

            money -= item.getBuyingPrice();
            inventory.put(item.getName(), inventory.getOrDefault(item.getName(), 0) + 1);
            System.out.println("Bought " + item.getName() + " for ₽" + item.getBuyingPrice());
            return true;
        } else {
            System.out.println("Not enough money!");
            return false;
        }
    }

    /**
     * Sells an item
     */
    public boolean sellItem(String itemName, Item itemReference) {
        if (inventory.containsKey(itemName) && inventory.get(itemName) > 0) {
            money += itemReference.getSellingPrice();
            inventory.put(itemName, inventory.get(itemName) - 1);
            if (inventory.get(itemName) == 0) {
                inventory.remove(itemName);
            }
            System.out.println("Sold " + itemName + " for ₽" + itemReference.getSellingPrice());
            return true;
        } else {
            System.out.println("You don't have " + itemName);
            return false;
        }
    }

    /**
     * Uses an item on a Pokémon
     */
    public boolean useItem(String itemName, Item itemReference, Pokemon target) {
        if (inventory.containsKey(itemName) && inventory.get(itemName) > 0) {
            itemReference.useOn(target);
            inventory.put(itemName, inventory.get(itemName) - 1);
            if (inventory.get(itemName) == 0) {
                inventory.remove(itemName);
            }
            return true;
        } else {
            System.out.println("You don't have " + itemName);
            return false;
        }
    }

    /**
     * Adds a Pokémon to the lineup
     */
    public boolean addPokemonToLineup(Pokemon pokemon) {
        if (activeLineup.size() < 6) {
            activeLineup.add(pokemon);
            System.out.println(pokemon.getName() + " added to lineup!");
            return true;
        } else {
            System.out.println("Lineup full! Move a Pokémon to storage first.");
            return false;
        }
    }

    /**
     * Moves a Pokémon from lineup to storage
     */
    public boolean switchPokemonToStorage(int lineupIndex) {
        if (lineupIndex >= 0 && lineupIndex < activeLineup.size()) {
            Pokemon pokemon = activeLineup.remove(lineupIndex);
            storage.add(pokemon);
            System.out.println(pokemon.getName() + " moved to storage.");
            return true;
        }
        return false;
    }

    /**
     * Moves a Pokémon from storage to lineup
     */
    public boolean switchPokemonFromStorage(int storageIndex) {
        if (storageIndex >= 0 && storageIndex < storage.size() && activeLineup.size() < 6) {
            Pokemon pokemon = storage.remove(storageIndex);
            activeLineup.add(pokemon);
            System.out.println(pokemon.getName() + " moved to lineup.");
            return true;
        }
        return false;
    }

    /**
     * Releases a Pokémon (removes from trainer)
     */
    public void releasePokemon(Pokemon pokemon) {
        activeLineup.remove(pokemon);
        storage.remove(pokemon);
        System.out.println(pokemon.getName() + " was released. Take care!");
    }

    // Getters
    public int getTrainerId() { return trainerId; }
    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public String getSex() { return sex; }
    public String getHometown() { return hometown; }
    public String getDescription() { return description; }
    public double getMoney() { return money; }
    public ArrayList<Pokemon> getActiveLineup() { return activeLineup; }
    public ArrayList<Pokemon> getStorage() { return storage; }
    public HashMap<String, Integer> getInventory() { return inventory; }

    @Override
    public String toString() {
        return String.format("Trainer %s (ID: %d) from %s - ₽%.2f",
                name, trainerId, hometown, money);
    }
}

// ============================================================================
// POKEDEX DATABASE MANAGER
// ============================================================================

/**
 * Manages all data for the Enhanced Pokédex system
 */
class PokedexManager {
    private ArrayList<Pokemon> pokemonDatabase;
    private ArrayList<Move> moveDatabase;
    private ArrayList<Item> itemDatabase;
    private ArrayList<Trainer> trainerDatabase;

    public PokedexManager() {
        pokemonDatabase = new ArrayList<>();
        moveDatabase = new ArrayList<>();
        itemDatabase = new ArrayList<>();
        trainerDatabase = new ArrayList<>();

        initializeData();
    }

    /**
     * Initializes the database with sample data
     */
    private void initializeData() {
        // Sample Pokémon
        pokemonDatabase.add(new Pokemon(1, "Bulbasaur", "Grass", "Poison", 5, 0, 2, 16, 45, 49, 49, 45));
        pokemonDatabase.add(new Pokemon(2, "Ivysaur", "Grass", "Poison", 16, 1, 3, 32, 60, 62, 63, 60));
        pokemonDatabase.add(new Pokemon(4, "Charmander", "Fire", null, 5, 0, 5, 16, 39, 52, 43, 65));
        pokemonDatabase.add(new Pokemon(7, "Squirtle", "Water", null, 5, 0, 8, 16, 44, 48, 65, 43));
        pokemonDatabase.add(new Pokemon(25, "Pikachu", "Electric", null, 5, 0, 26, 22, 35, 55, 40, 90));

        // Sample Moves
        moveDatabase.add(new Move("Vine Whip", "Strikes with vines", "Grass", null));
        moveDatabase.add(new Move("Ember", "Shoots small flames", "Fire", null));
        moveDatabase.add(new Move("Water Gun", "Sprays water", "Water", null));
        moveDatabase.add(new Move("Thunder Shock", "Electric attack", "Electric", null));
        moveDatabase.add(new Move("Surf", "Large wave attack", "HM", "Water", null));

        // Sample Items
        itemDatabase.add(new Item("HP Up", "Vitamin", "A nutritious drink for Pokémon", "+10 HP EVs", 10000, 5000));
        itemDatabase.add(new Item("Protein", "Vitamin", "A nutritious drink for Pokémon", "+10 Attack EVs", 10000, 5000));
        itemDatabase.add(new Item("Iron", "Vitamin", "A nutritious drink for Pokémon", "+10 Defense EVs", 10000, 5000));
        itemDatabase.add(new Item("Carbos", "Vitamin", "A nutritious drink for Pokémon", "+10 Speed EVs", 10000, 5000));
        itemDatabase.add(new Item("Rare Candy", "Leveling Item", "A candy packed with energy", "Increases level by 1", 0, 2400));
        itemDatabase.add(new Item("Fire Stone", "Evolution Stone", "A stone that radiates heat", "Evolves certain Pokémon", 4000, 1500));
        itemDatabase.add(new Item("Water Stone", "Evolution Stone", "A stone with watery appearance", "Evolves certain Pokémon", 4000, 1500));
        itemDatabase.add(new Item("Thunder Stone", "Evolution Stone", "A stone that sparkles with electricity", "Evolves certain Pokémon", 4000, 1500));
    }

    // Pokemon operations
    public boolean addPokemon(Pokemon pokemon) {
        // Check for unique Pokédex number
        for (Pokemon p : pokemonDatabase) {
            if (p.getPokedexNumber() == pokemon.getPokedexNumber()) {
                System.out.println("Pokédex number already exists!");
                return false;
            }
        }
        pokemonDatabase.add(pokemon);
        return true;
    }

    public void viewAllPokemon() {
        System.out.println("\n=== POKÉMON DATABASE ===");
        for (Pokemon pokemon : pokemonDatabase) {
            System.out.println(pokemon);
        }
    }

    public ArrayList<Pokemon> searchPokemon(String query) {
        ArrayList<Pokemon> results = new ArrayList<>();
        query = query.toLowerCase();

        for (Pokemon pokemon : pokemonDatabase) {
            if (pokemon.getName().toLowerCase().contains(query) ||
                    pokemon.getType1().toLowerCase().contains(query) ||
                    (pokemon.getType2() != null && pokemon.getType2().toLowerCase().contains(query))) {
                results.add(pokemon);
            }
        }
        return results;
    }

    // Move operations
    public void addMove(Move move) {
        moveDatabase.add(move);
    }

    public void viewAllMoves() {
        System.out.println("\n=== MOVE DATABASE ===");
        for (Move move : moveDatabase) {
            System.out.println(move);
        }
    }

    public ArrayList<Move> searchMoves(String query) {
        ArrayList<Move> results = new ArrayList<>();
        query = query.toLowerCase();

        for (Move move : moveDatabase) {
            if (move.getName().toLowerCase().contains(query) ||
                    move.getType1().toLowerCase().contains(query)) {
                results.add(move);
            }
        }
        return results;
    }

    // Item operations
    public void viewAllItems() {
        System.out.println("\n=== ITEM DATABASE ===");
        for (Item item : itemDatabase) {
            System.out.println(item);
        }
    }

    public ArrayList<Item> searchItems(String query) {
        ArrayList<Item> results = new ArrayList<>();
        query = query.toLowerCase();

        for (Item item : itemDatabase) {
            if (item.getName().toLowerCase().contains(query) ||
                    item.getCategory().toLowerCase().contains(query)) {
                results.add(item);
            }
        }
        return results;
    }

    public Item getItemByName(String name) {
        for (Item item : itemDatabase) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Trainer operations
    public void addTrainer(Trainer trainer) {
        trainerDatabase.add(trainer);
    }

    public void viewAllTrainers() {
        System.out.println("\n=== TRAINER DATABASE ===");
        for (Trainer trainer : trainerDatabase) {
            System.out.println(trainer);
            System.out.println("  Active Lineup: " + trainer.getActiveLineup().size() + " Pokémon");
            System.out.println("  Storage: " + trainer.getStorage().size() + " Pokémon");
            System.out.println("  Inventory: " + trainer.getInventory().size() + " item types");
        }
    }

    public ArrayList<Trainer> searchTrainers(String query) {
        ArrayList<Trainer> results = new ArrayList<>();
        query = query.toLowerCase();

        for (Trainer trainer : trainerDatabase) {
            if (trainer.getName().toLowerCase().contains(query) ||
                    trainer.getHometown().toLowerCase().contains(query)) {
                results.add(trainer);
            }
        }
        return results;
    }

    public ArrayList<Pokemon> getPokemonDatabase() { return pokemonDatabase; }
    public ArrayList<Move> getMoveDatabase() { return moveDatabase; }
    public ArrayList<Item> getItemDatabase() { return itemDatabase; }
    public ArrayList<Trainer> getTrainerDatabase() { return trainerDatabase; }
}

public class EnhancedPokedex {
    private PokedexManager manager;
    private Scanner scanner;

    public EnhancedPokedex() {
        manager = new PokedexManager();
        scanner = new Scanner(System.in);
    }

    /**
     * Main menu system
     */
    public void run() {
        System.out.println("=================================================");
        System.out.println("        ENHANCED POKÉDEX SYSTEM v1.0");
        System.out.println("=================================================");

        while (true) {
            showMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1: pokemonMenu(); break;
                case 2: movesMenu(); break;
                case 3: itemsMenu(); break;
                case 4: trainersMenu(); break;
                case 0:
                    System.out.println("Thank you for using Enhanced Pokédex!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Pokémon Management");
        System.out.println("2. Moves Management");
        System.out.println("3. Items Management");
        System.out.println("4. Trainer Management");
        System.out.println("0. Exit");
        System.out.print("Select option: ");
    }

    private void pokemonMenu() {
        while (true) {
            System.out.println("\n=== POKÉMON MANAGEMENT ===");
            System.out.println("1. Add New Pokémon");
            System.out.println("2. View All Pokémon");
            System.out.println("3. Search Pokémon");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: addNewPokemon(); break;
                case 2: manager.viewAllPokemon(); break;
                case 3: searchPokemon(); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void movesMenu() {
        while (true) {
            System.out.println("\n=== MOVES MANAGEMENT ===");
            System.out.println("1. Add New Move");
            System.out.println("2. View All Moves");
            System.out.println("3. Search Moves");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: addNewMove(); break;
                case 2: manager.viewAllMoves(); break;
                case 3: searchMoves(); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void itemsMenu() {
        while (true) {
            System.out.println("\n=== ITEMS MANAGEMENT ===");
            System.out.println("1. View All Items");
            System.out.println("2. Search Items");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: manager.viewAllItems(); break;
                case 2: searchItems(); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void trainersMenu() {
        while (true) {
            System.out.println("\n=== TRAINER MANAGEMENT ===");
            System.out.println("1. Add New Trainer");
            System.out.println("2. View All Trainers");
            System.out.println("3. Search Trainers");
            System.out.println("4. Trainer Operations");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: addNewTrainer(); break;
                case 2: manager.viewAllTrainers(); break;
                case 3: searchTrainers(); break;
                case 4: trainerOperations(); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void addNewPokemon() {
        System.out.println("\n=== ADD NEW POKÉMON ===");

        System.out.print("Pokédex Number: ");
        int pokedexNum = getIntInput();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Type 1: ");
        String type1 = scanner.nextLine();

        System.out.print("Type 2 (optional, press Enter to skip): ");
        String type2 = scanner.nextLine();
        if (type2.trim().isEmpty()) type2 = null;

        System.out.print("Base Level: ");
        int baseLevel = getIntInput();

        System.out.print("Evolves From (Pokédex #, 0 if none): ");
        int evolvesFrom = getIntInput();

        System.out.print("Evolves To (Pokédex #, 0 if none): ");
        int evolvesTo = getIntInput();

        System.out.print("Evolution Level (0 if doesn't evolve): ");
        int evolutionLevel = getIntInput();

        System.out.print("Base HP: ");
        int hp = getIntInput();

        System.out.print("Base Attack: ");
        int attack = getIntInput();

        System.out.print("Base Defense: ");
        int defense = getIntInput();

        System.out.print("Base Speed: ");
        int speed = getIntInput();

        Pokemon pokemon = new Pokemon(pokedexNum, name, type1, type2, baseLevel,
                evolvesFrom, evolvesTo, evolutionLevel,
                hp, attack, defense, speed);

        if (manager.addPokemon(pokemon)) {
            System.out.println("Pokémon added successfully!");
            pokemon.cry();
        }
    }

    private void searchPokemon() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        ArrayList<Pokemon> results = manager.searchPokemon(query);

        if (results.isEmpty()) {
            System.out.println("No Pokémon found matching: " + query);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (Pokemon pokemon : results) {
                System.out.println(pokemon);
            }
        }
    }

    private void addNewMove() {
        System.out.println("\n=== ADD NEW MOVE ===");

        System.out.print("Move Name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Classification (Normal/HM/TM): ");
        String classification = scanner.nextLine();

        System.out.print("Type 1: ");
        String type1 = scanner.nextLine();

        System.out.print("Type 2 (optional, press Enter to skip): ");
        String type2 = scanner.nextLine();
        if (type2.trim().isEmpty()) type2 = null;

        Move move = new Move(name, description, classification, type1, type2);
        manager.addMove(move);
        System.out.println("Move added successfully!");
    }

    private void searchMoves() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        ArrayList<Move> results = manager.searchMoves(query);

        if (results.isEmpty()) {
            System.out.println("No moves found matching: " + query);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (Move move : results) {
                System.out.println(move);
            }
        }
    }

    private void searchItems() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        ArrayList<Item> results = manager.searchItems(query);

        if (results.isEmpty()) {
            System.out.println("No items found matching: " + query);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (Item item : results) {
                System.out.println(item);
            }
        }
    }

    private void addNewTrainer() {
        System.out.println("\n=== ADD NEW TRAINER ===");

        System.out.print("Trainer ID: ");
        int trainerId = getIntInput();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Birthdate (YYYY-MM-DD): ");
        String birthdate = scanner.nextLine();

        System.out.print("Sex (M/F): ");
        String sex = scanner.nextLine();

        System.out.print("Hometown: ");
        String hometown = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        Trainer trainer = new Trainer(trainerId, name, birthdate, sex, hometown, description);
        manager.addTrainer(trainer);
        System.out.println("Trainer added successfully!");
        System.out.println("Starting funds: ₽" + trainer.getMoney());
    }

    private void searchTrainers() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        ArrayList<Trainer> results = manager.searchTrainers(query);

        if (results.isEmpty()) {
            System.out.println("No trainers found matching: " + query);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (Trainer trainer : results) {
                System.out.println(trainer);
            }
        }
    }

    private void trainerOperations() {
        if (manager.getTrainerDatabase().isEmpty()) {
            System.out.println("No trainers available. Add a trainer first.");
            return;
        }

        System.out.println("\n=== SELECT TRAINER ===");
        for (int i = 0; i < manager.getTrainerDatabase().size(); i++) {
            System.out.println((i + 1) + ". " + manager.getTrainerDatabase().get(i).getName());
        }

        System.out.print("Select trainer (number): ");
        int trainerIndex = getIntInput() - 1;

        if (trainerIndex < 0 || trainerIndex >= manager.getTrainerDatabase().size()) {
            System.out.println("Invalid trainer selection.");
            return;
        }

        Trainer selectedTrainer = manager.getTrainerDatabase().get(trainerIndex);
        trainerOperationsMenu(selectedTrainer);
    }

    private void trainerOperationsMenu(Trainer trainer) {
        while (true) {
            System.out.println("\n=== TRAINER OPERATIONS: " + trainer.getName() + " ===");
            System.out.println("Money: ₽" + String.format("%.2f", trainer.getMoney()));
            System.out.println("Active Lineup: " + trainer.getActiveLineup().size() + "/6");
            System.out.println("Storage: " + trainer.getStorage().size() + " Pokémon");
            System.out.println("Inventory: " + trainer.getInventory().size() + "/10 item types");

            System.out.println("\n1. Buy Item");
            System.out.println("2. Sell Item");
            System.out.println("3. Use Item");
            System.out.println("4. Add Pokémon to Lineup");
            System.out.println("5. Manage Pokémon");
            System.out.println("6. View Lineup");
            System.out.println("7. View Storage");
            System.out.println("8. View Inventory");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: buyItemForTrainer(trainer); break;
                case 2: sellItemForTrainer(trainer); break;
                case 3: useItemForTrainer(trainer); break;
                case 4: addPokemonToTrainer(trainer); break;
                case 5: managePokemonForTrainer(trainer); break;
                case 6: viewTrainerLineup(trainer); break;
                case 7: viewTrainerStorage(trainer); break;
                case 8: viewTrainerInventory(trainer); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void buyItemForTrainer(Trainer trainer) {
        System.out.println("\n=== ITEM SHOP ===");
        ArrayList<Item> items = manager.getItemDatabase();

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getBuyingPrice() > 0) { // Only show buyable items
                System.out.println((i + 1) + ". " + item);
            }
        }

        System.out.print("Select item to buy (number, 0 to cancel): ");
        int itemIndex = getIntInput() - 1;

        if (itemIndex >= 0 && itemIndex < items.size()) {
            Item selectedItem = items.get(itemIndex);
            if (selectedItem.getBuyingPrice() > 0) {
                trainer.buyItem(selectedItem);
            } else {
                System.out.println("This item is not for sale.");
            }
        }
    }

    private void sellItemForTrainer(Trainer trainer) {
        if (trainer.getInventory().isEmpty()) {
            System.out.println("No items to sell.");
            return;
        }

        System.out.println("\n=== SELL ITEMS ===");
        ArrayList<String> itemNames = new ArrayList<>(trainer.getInventory().keySet());

        for (int i = 0; i < itemNames.size(); i++) {
            String itemName = itemNames.get(i);
            int quantity = trainer.getInventory().get(itemName);
            Item itemRef = manager.getItemByName(itemName);
            System.out.println((i + 1) + ". " + itemName + " x" + quantity +
                    " (Sell for ₽" + (itemRef != null ? itemRef.getSellingPrice() : "N/A") + ")");
        }

        System.out.print("Select item to sell (number, 0 to cancel): ");
        int itemIndex = getIntInput() - 1;

        if (itemIndex >= 0 && itemIndex < itemNames.size()) {
            String selectedItemName = itemNames.get(itemIndex);
            Item itemRef = manager.getItemByName(selectedItemName);
            if (itemRef != null) {
                trainer.sellItem(selectedItemName, itemRef);
            }
        }
    }

    private void useItemForTrainer(Trainer trainer) {
        if (trainer.getInventory().isEmpty()) {
            System.out.println("No items to use.");
            return;
        }

        if (trainer.getActiveLineup().isEmpty()) {
            System.out.println("No Pokémon in lineup to use items on.");
            return;
        }

        System.out.println("\n=== SELECT ITEM ===");
        ArrayList<String> itemNames = new ArrayList<>(trainer.getInventory().keySet());

        for (int i = 0; i < itemNames.size(); i++) {
            String itemName = itemNames.get(i);
            int quantity = trainer.getInventory().get(itemName);
            System.out.println((i + 1) + ". " + itemName + " x" + quantity);
        }

        System.out.print("Select item to use (number, 0 to cancel): ");
        int itemIndex = getIntInput() - 1;

        if (itemIndex >= 0 && itemIndex < itemNames.size()) {
            String selectedItemName = itemNames.get(itemIndex);
            Item itemRef = manager.getItemByName(selectedItemName);

            if (itemRef != null) {
                System.out.println("\n=== SELECT POKÉMON ===");
                for (int i = 0; i < trainer.getActiveLineup().size(); i++) {
                    System.out.println((i + 1) + ". " + trainer.getActiveLineup().get(i));
                }

                System.out.print("Select Pokémon (number, 0 to cancel): ");
                int pokemonIndex = getIntInput() - 1;

                if (pokemonIndex >= 0 && pokemonIndex < trainer.getActiveLineup().size()) {
                    Pokemon selectedPokemon = trainer.getActiveLineup().get(pokemonIndex);
                    trainer.useItem(selectedItemName, itemRef, selectedPokemon);
                }
            }
        }
    }

    private void addPokemonToTrainer(Trainer trainer) {
        if (manager.getPokemonDatabase().isEmpty()) {
            System.out.println("No Pokémon available in database.");
            return;
        }

        System.out.println("\n=== ADD POKÉMON TO LINEUP ===");
        System.out.println("Available Pokémon:");

        for (int i = 0; i < manager.getPokemonDatabase().size(); i++) {
            System.out.println((i + 1) + ". " + manager.getPokemonDatabase().get(i));
        }

        System.out.print("Select Pokémon to add (number, 0 to cancel): ");
        int pokemonIndex = getIntInput() - 1;

        if (pokemonIndex >= 0 && pokemonIndex < manager.getPokemonDatabase().size()) {
            Pokemon original = manager.getPokemonDatabase().get(pokemonIndex);
            Pokemon trainerPokemon = new Pokemon(
                    original.getPokedexNumber(),
                    original.getName(),
                    original.getType1(),
                    original.getType2(),
                    original.getCurrentLevel(),
                    0, // evolvesFrom
                    original.getEvolvesTo(),
                    original.getEvolutionLevel(),
                    original.getHp(),
                    original.getAttack(),
                    original.getDefense(),
                    original.getSpeed()
            );

            trainer.addPokemonToLineup(trainerPokemon);
        }
    }

    private void managePokemonForTrainer(Trainer trainer) {
        while (true) {
            System.out.println("\n=== POKÉMON MANAGEMENT ===");
            System.out.println("1. Switch Pokémon to Storage");
            System.out.println("2. Switch Pokémon from Storage");
            System.out.println("3. Release Pokémon");
            System.out.println("4. Teach Move");
            System.out.println("5. Make Pokémon Cry");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: switchToStorage(trainer); break;
                case 2: switchFromStorage(trainer); break;
                case 3: releasePokemon(trainer); break;
                case 4: teachMove(trainer); break;
                case 5: makePokemonCry(trainer); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void switchToStorage(Trainer trainer) {
        if (trainer.getActiveLineup().isEmpty()) {
            System.out.println("No Pokémon in lineup.");
            return;
        }

        System.out.println("\n=== MOVE TO STORAGE ===");
        for (int i = 0; i < trainer.getActiveLineup().size(); i++) {
            System.out.println((i + 1) + ". " + trainer.getActiveLineup().get(i));
        }

        System.out.print("Select Pokémon to move to storage (number, 0 to cancel): ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < trainer.getActiveLineup().size()) {
            trainer.switchPokemonToStorage(index);
        }
    }

    private void switchFromStorage(Trainer trainer) {
        if (trainer.getStorage().isEmpty()) {
            System.out.println("No Pokémon in storage.");
            return;
        }

        if (trainer.getActiveLineup().size() >= 6) {
            System.out.println("Lineup is full. Move a Pokémon to storage first.");
            return;
        }

        System.out.println("\n=== MOVE FROM STORAGE ===");
        for (int i = 0; i < trainer.getStorage().size(); i++) {
            System.out.println((i + 1) + ". " + trainer.getStorage().get(i));
        }

        System.out.print("Select Pokémon to move to lineup (number, 0 to cancel): ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < trainer.getStorage().size()) {
            trainer.switchPokemonFromStorage(index);
        }
    }

    private void releasePokemon(Trainer trainer) {
        ArrayList<Pokemon> allPokemon = new ArrayList<>();
        allPokemon.addAll(trainer.getActiveLineup());
        allPokemon.addAll(trainer.getStorage());

        if (allPokemon.isEmpty()) {
            System.out.println("No Pokémon to release.");
            return;
        }

        System.out.println("\n=== RELEASE POKÉMON ===");
        for (int i = 0; i < allPokemon.size(); i++) {
            Pokemon pokemon = allPokemon.get(i);
            String location = trainer.getActiveLineup().contains(pokemon) ? "(Lineup)" : "(Storage)";
            System.out.println((i + 1) + ". " + pokemon + " " + location);
        }

        System.out.print("Select Pokémon to release (number, 0 to cancel): ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < allPokemon.size()) {
            Pokemon selectedPokemon = allPokemon.get(index);
            System.out.print("Are you sure you want to release " + selectedPokemon.getName() + "? (y/n): ");
            String confirm = scanner.nextLine();

            if (confirm.toLowerCase().startsWith("y")) {
                trainer.releasePokemon(selectedPokemon);
            } else {
                System.out.println("Release cancelled.");
            }
        }
    }

    private void teachMove(Trainer trainer) {
        if (trainer.getActiveLineup().isEmpty()) {
            System.out.println("No Pokémon in lineup to teach moves to.");
            return;
        }

        // Select Pokémon
        System.out.println("\n=== SELECT POKÉMON ===");
        for (int i = 0; i < trainer.getActiveLineup().size(); i++) {
            System.out.println((i + 1) + ". " + trainer.getActiveLineup().get(i));
        }

        System.out.print("Select Pokémon (number, 0 to cancel): ");
        int pokemonIndex = getIntInput() - 1;

        if (pokemonIndex >= 0 && pokemonIndex < trainer.getActiveLineup().size()) {
            Pokemon selectedPokemon = trainer.getActiveLineup().get(pokemonIndex);

            // Select Move
            System.out.println("\n=== SELECT MOVE ===");
            for (int i = 0; i < manager.getMoveDatabase().size(); i++) {
                System.out.println((i + 1) + ". " + manager.getMoveDatabase().get(i));
            }

            System.out.print("Select move to teach (number, 0 to cancel): ");
            int moveIndex = getIntInput() - 1;

            if (moveIndex >= 0 && moveIndex < manager.getMoveDatabase().size()) {
                Move selectedMove = manager.getMoveDatabase().get(moveIndex);
                selectedPokemon.teachMove(selectedMove);
            }
        }
    }

    private void makePokemonCry(Trainer trainer) {
        ArrayList<Pokemon> allPokemon = new ArrayList<>();
        allPokemon.addAll(trainer.getActiveLineup());
        allPokemon.addAll(trainer.getStorage());

        if (allPokemon.isEmpty()) {
            System.out.println("No Pokémon available.");
            return;
        }

        System.out.println("\n=== SELECT POKÉMON ===");
        for (int i = 0; i < allPokemon.size(); i++) {
            Pokemon pokemon = allPokemon.get(i);
            String location = trainer.getActiveLineup().contains(pokemon) ? "(Lineup)" : "(Storage)";
            System.out.println((i + 1) + ". " + pokemon + " " + location);
        }

        System.out.print("Select Pokémon to hear cry (number, 0 to cancel): ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < allPokemon.size()) {
            allPokemon.get(index).cry();
        }
    }

    private void viewTrainerLineup(Trainer trainer) {
        System.out.println("\n=== " + trainer.getName().toUpperCase() + "'S LINEUP ===");
        if (trainer.getActiveLineup().isEmpty()) {
            System.out.println("No Pokémon in lineup.");
        } else {
            for (int i = 0; i < trainer.getActiveLineup().size(); i++) {
                Pokemon pokemon = trainer.getActiveLineup().get(i);
                System.out.println((i + 1) + ". " + pokemon);
                System.out.println("    HP: " + pokemon.getHp() + " | ATK: " + pokemon.getAttack() +
                        " | DEF: " + pokemon.getDefense() + " | SPD: " + pokemon.getSpeed());
                if (pokemon.getHeldItem() != null) {
                    System.out.println("    Holding: " + pokemon.getHeldItem().getName());
                }
                System.out.println("    Moves: " + pokemon.getMoveSet().size() + "/4");
                for (Move move : pokemon.getMoveSet()) {
                    System.out.println("      - " + move.getName() + " (" + move.getType1() + ")");
                }
            }
        }
    }

    private void viewTrainerStorage(Trainer trainer) {
        System.out.println("\n=== " + trainer.getName().toUpperCase() + "'S STORAGE ===");
        if (trainer.getStorage().isEmpty()) {
            System.out.println("No Pokémon in storage.");
        } else {
            for (int i = 0; i < trainer.getStorage().size(); i++) {
                Pokemon pokemon = trainer.getStorage().get(i);
                System.out.println((i + 1) + ". " + pokemon);
            }
        }
    }

    private void viewTrainerInventory(Trainer trainer) {
        System.out.println("\n=== " + trainer.getName().toUpperCase() + "'S INVENTORY ===");
        if (trainer.getInventory().isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            int totalItems = 0;
            for (Map.Entry<String, Integer> entry : trainer.getInventory().entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                totalItems += quantity;

                Item itemRef = manager.getItemByName(itemName);
                String category = itemRef != null ? itemRef.getCategory() : "Unknown";

                System.out.println(itemName + " x" + quantity + " (" + category + ")");
            }
            System.out.println("\nTotal items: " + totalItems + "/50");
            System.out.println("Unique items: " + trainer.getInventory().size() + "/10");
        }
    }

   private int getIntInput() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

   public static void main(String[] args) {
        EnhancedPokedex app = new EnhancedPokedex();
        app.run();
    }
}