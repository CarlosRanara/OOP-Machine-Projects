import java.util.*;

// ========================================
// ABSTRACT BASE CLASS: COFFEE TRUCK
// ========================================

/**
 * Abstract base class for coffee trucks
 * Contains common functionality for both Regular and Special trucks
 * Implements core business logic for coffee preparation and sales
 *
 * @author Student Name
 * @version 1.0
 * @since 2024
 */
abstract class CoffeeTruck {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    protected String location;
    protected StorageBin[] storageBins;
    protected Map<String, Map<String, Double>> pricing; // drink -> size -> price
    protected List<Transaction> transactions;

    // Constants for cup sizes (in fluid ounces)
    private static final double SMALL_CUP_SIZE = 8.0;
    private static final double MEDIUM_CUP_SIZE = 12.0;
    private static final double LARGE_CUP_SIZE = 16.0;

    // Conversion factor: 1 fl. oz. = 28.34952 grams (as per specification)
    private static final double FL_OZ_TO_GRAMS = 28.34952;

    // ========================================
    // CONSTRUCTOR
    // ========================================

    /**
     * Constructor for CoffeeTruck
     * @param location The location where the truck is deployed
     */
    public CoffeeTruck(String location) {
        this.location = location;
        this.storageBins = new StorageBin[8]; // Regular trucks have 8 bins
        this.pricing = new HashMap<>();
        this.transactions = new ArrayList<>();
        initializePricing();
    }

    /**
     * Initializes the pricing structure for all drink types
     */
    private void initializePricing() {
        pricing.put("Americano", new HashMap<>());
        pricing.put("Latte", new HashMap<>());
        pricing.put("Cappuccino", new HashMap<>());
    }

    // ========================================
    // ABSTRACT METHODS
    // ========================================

    /**
     * Abstract method to get truck type
     * @return String representing the truck type
     */
    public abstract String getTruckType();

    // ========================================
    // ORDER PROCESSING
    // ========================================

    /**
     * Processes a coffee order
     * This is the main method that handles the entire order lifecycle:
     * 1. Validates ingredient availability
     * 2. Calculates required ingredients
     * 3. Displays preparation steps
     * 4. Deducts ingredients from storage
     * 5. Records transaction
     *
     * @param order The coffee order to process
     * @return true if order was processed successfully, false otherwise
     */
    public boolean processOrder(CoffeeOrder order) {
        // Step 1: Check if all required ingredients are available
        if (!areIngredientsAvailable(order)) {
            return false;
        }

        // Step 2: Calculate exact ingredient requirements
        IngredientRequirement requirement = calculateIngredientRequirements(order);

        // Step 3: Check if required cup size is available
        if (!isCupAvailable(order.getSize())) {
            System.out.println("No " + order.getSize().toLowerCase() + " cups available!");
            return false;
        }

        // Step 4: Begin order processing
        System.out.println("\n" + "=".repeat(50));
        System.out.println(">>> Preparing " + order.getSize() + " Cup...");

        // Step 5: Deduct all ingredients from storage
        deductIngredientsFromStorage(requirement);
        deductCupFromStorage(order.getSize());

        // Step 6: Display step-by-step preparation process
        displayPreparationProcess(order, requirement);

        // Step 7: Calculate total price including add-ons
        double totalPrice = calculateTotalPrice(order);

        // Step 8: Record transaction for reporting
        Transaction transaction = new Transaction(order, requirement, totalPrice);
        transactions.add(transaction);

        // Step 9: Display completion message
        System.out.printf(">>> Total Price: $%.2f%n", totalPrice);
        System.out.println(">>> " + getCustomDrinkName(order) + " Done!");
        System.out.println("=".repeat(50));

        return true;
    }

    /**
     * Checks if all required ingredients are available for the order
     * @param order The coffee order to check
     * @return true if all ingredients are available, false otherwise
     */
    private boolean areIngredientsAvailable(CoffeeOrder order) {
        IngredientRequirement requirement = calculateIngredientRequirements(order);

        boolean coffeeAvailable = isIngredientAvailable("Coffee Beans", requirement.getCoffeeGrams());
        boolean waterAvailable = isIngredientAvailable("Water", requirement.getWaterOz());
        boolean milkAvailable = isIngredientAvailable("Milk", requirement.getMilkOz());

        if (!coffeeAvailable) {
            System.out.println("Insufficient coffee beans! Required: " +
                    String.format("%.2f", requirement.getCoffeeGrams()) + "g");
        }
        if (!waterAvailable) {
            System.out.println("Insufficient water! Required: " +
                    String.format("%.2f", requirement.getWaterOz()) + " fl.oz.");
        }
        if (!milkAvailable && requirement.getMilkOz() > 0) {
            System.out.println("Insufficient milk! Required: " +
                    String.format("%.2f", requirement.getMilkOz()) + " fl.oz.");
        }

        return coffeeAvailable && waterAvailable && milkAvailable;
    }

    // ========================================
    // INGREDIENT CALCULATIONS
    // ========================================

    /**
     * Calculates exact ingredient requirements for a coffee order
     * Uses precise ratios as specified in the requirements document
     *
     * @param order The coffee order
     * @return IngredientRequirement object with calculated amounts
     */
    private IngredientRequirement calculateIngredientRequirements(CoffeeOrder order) {
        double cupSize = getCupSizeInOz(order.getSize());
        double coffeeGrams = 0;
        double waterOz = 0;
        double milkOz = 0;

        // Calculate base drink requirements
        switch (order.getDrinkType()) {
            case "Americano":
                // Americano: 1 part espresso, 2 parts water
                double americanoEspresso = cupSize / 3.0; // 1 part out of 3 total
                waterOz = (cupSize * 2.0) / 3.0; // 2 parts out of 3 total
                coffeeGrams = calculateCoffeeForEspresso(americanoEspresso, order.getBrewType());
                break;

            case "Latte":
                // Latte: 1/5 espresso, 4/5 milk
                double latteEspresso = cupSize / 5.0;
                milkOz = (cupSize * 4.0) / 5.0;
                coffeeGrams = calculateCoffeeForEspresso(latteEspresso, order.getBrewType());
                break;

            case "Cappuccino":
                // Cappuccino: 1/3 espresso, 2/3 milk
                double cappuccinoEspresso = cupSize / 3.0;
                milkOz = (cupSize * 2.0) / 3.0;
                coffeeGrams = calculateCoffeeForEspresso(cappuccinoEspresso, order.getBrewType());
                break;
        }

        // Add extra shot if requested (Special trucks only)
        if (order.isExtraShot()) {
            double extraShotCoffee = calculateCoffeeForEspresso(1.0, order.getBrewType());
            coffeeGrams += extraShotCoffee;
            // Extra shot doesn't add water for milk-based drinks, only for preparation
        }

        return new IngredientRequirement(coffeeGrams, waterOz, milkOz);
    }

    /**
     * Calculates coffee grams needed for espresso based on brew type and ratio
     * Uses the 1:18 standard ratio with variations for different brew strengths
     *
     * @param espressoOz Amount of espresso in fluid ounces
     * @param brewType Type of brew (Standard, Strong, Light, Custom)
     * @return Coffee amount in grams
     */
    private double calculateCoffeeForEspresso(double espressoOz, String brewType) {
        // Convert espresso volume to water weight for brewing calculation
        double waterInGrams = espressoOz * FL_OZ_TO_GRAMS;

        // Calculate coffee based on brew ratio
        switch (brewType) {
            case "Strong":
                return waterInGrams / 15.0; // 1:15 ratio
            case "Light":
                return waterInGrams / 20.0; // 1:20 ratio
            default:
                if (brewType.startsWith("Custom")) {
                    // Parse custom ratio (format: "Custom 1:X")
                    String[] parts = brewType.split(":");
                    if (parts.length == 2) {
                        try {
                            int ratio = Integer.parseInt(parts[1]);
                            return waterInGrams / ratio;
                        } catch (NumberFormatException e) {
                            // Fall back to standard if parsing fails
                        }
                    }
                }
                return waterInGrams / 18.0; // 1:18 Standard ratio (default)
        }
    }

    /**
     * Gets cup size in fluid ounces based on size name
     * @param size Cup size name (Small, Medium, Large)
     * @return Cup size in fluid ounces
     */
    private double getCupSizeInOz(String size) {
        switch (size) {
            case "Small": return SMALL_CUP_SIZE;
            case "Medium": return MEDIUM_CUP_SIZE;
            case "Large": return LARGE_CUP_SIZE;
            default: return SMALL_CUP_SIZE; // Default fallback
        }
    }

    // ========================================
    // INVENTORY MANAGEMENT
    // ========================================

    /**
     * Checks if truck has enough of a specific ingredient
     * @param ingredient Name of the ingredient
     * @param amount Required amount
     * @return true if sufficient quantity available, false otherwise
     */
    private boolean isIngredientAvailable(String ingredient, double amount) {
        for (StorageBin bin : storageBins) {
            if (bin != null && bin.getItemType().equals(ingredient)) {
                return bin.getCurrentQuantity() >= Math.ceil(amount);
            }
        }
        return false;
    }

    /**
     * Checks if truck has the required cup size available
     * @param size Cup size to check
     * @return true if cup is available, false otherwise
     */
    private boolean isCupAvailable(String size) {
        String cupType = size + " Cup";
        for (StorageBin bin : storageBins) {
            if (bin != null && bin.getItemType().equals(cupType)) {
                return bin.getCurrentQuantity() >= 1;
            }
        }
        return false;
    }

    /**
     * Deducts ingredients from storage bins based on requirements
     * @param requirement The ingredient requirements to deduct
     */
    private void deductIngredientsFromStorage(IngredientRequirement requirement) {
        deductFromStorage("Coffee Beans", (int) Math.ceil(requirement.getCoffeeGrams()));
        deductFromStorage("Water", (int) Math.ceil(requirement.getWaterOz()));
        deductFromStorage("Milk", (int) Math.ceil(requirement.getMilkOz()));
    }

    /**
     * Deducts a cup from storage
     * @param size Size of cup to deduct
     */
    private void deductCupFromStorage(String size) {
        String cupType = size + " Cup";
        deductFromStorage(cupType, 1);
    }

    /**
     * Deducts specified amount from storage bin containing the item
     * @param itemType Type of item to deduct
     * @param amount Amount to deduct
     */
    private void deductFromStorage(String itemType, int amount) {
        for (StorageBin bin : storageBins) {
            if (bin != null && bin.getItemType().equals(itemType)) {
                bin.removeItems(amount);
                break;
            }
        }
    }

    // ========================================
    // PREPARATION DISPLAY
    // ========================================

    /**
     * Displays step-by-step preparation process matching specification examples
     * @param order The coffee order being prepared
     * @param requirement The calculated ingredient requirements
     */
    private void displayPreparationProcess(CoffeeOrder order, IngredientRequirement requirement) {
        String brewDescription = getBrewDescription(order.getBrewType());

        // Display brewing step with exact coffee amount
        System.out.printf(">>> Brewing %s espresso - %.2f grams of coffee...%n",
                brewDescription, requirement.getCoffeeGrams());

        // Display milk addition for milk-based drinks
        if (requirement.getMilkOz() > 0) {
            System.out.println(">>> Adding Milk...");
        }

        // Display water addition for Americano
        if (order.getDrinkType().equals("Americano") && requirement.getWaterOz() > 0) {
            System.out.println(">>> Adding Water...");
        }

        // Display add-ons for special trucks
        if (order.getAddons() != null && !order.getAddons().isEmpty()) {
            for (String addon : order.getAddons()) {
                System.out.println(">>> Adding " + addon + "...");
            }
        }

        // Display extra shot preparation
        if (order.isExtraShot()) {
            double extraShotCoffee = calculateCoffeeForEspresso(1.0, order.getBrewType());
            System.out.printf(">>> Adding an extra shot of %s brew espresso - %.2f grams of coffee.%n",
                    brewDescription, extraShotCoffee);
        }
    }

    /**
     * Gets human-readable brew description for display
     * @param brewType The brew type code
     * @return Formatted brew description
     */
    private String getBrewDescription(String brewType) {
        switch (brewType) {
            case "Strong": return "Strong";
            case "Light": return "Light";
            default:
                if (brewType.startsWith("Custom")) return "Custom";
                return "Standard";
        }
    }

    /**
     * Gets custom drink name based on order customizations
     * @param order The coffee order
     * @return Appropriate drink name
     */
    private String getCustomDrinkName(CoffeeOrder order) {
        boolean hasCustomizations = (order.getAddons() != null && !order.getAddons().isEmpty()) ||
                order.isExtraShot() ||
                !order.getBrewType().equals("Standard");

        if (hasCustomizations) {
            return "Custom " + order.getDrinkType();
        }
        return order.getDrinkType();
    }

    // ========================================
    // PRICING CALCULATIONS
    // ========================================

    /**
     * Calculates total price for an order including base price and add-ons
     * Base implementation handles standard pricing, overridden in SpecialCoffeeTruck
     * @param order The coffee order
     * @return Total price
     */
    protected double calculateTotalPrice(CoffeeOrder order) {
        Map<String, Double> drinkPricing = pricing.get(order.getDrinkType());
        if (drinkPricing == null) {
            return 0.0; // No pricing set
        }

        Double basePrice = drinkPricing.get(order.getSize());
        return basePrice != null ? basePrice : 0.0;
    }

    /**
     * Sets pricing for a specific drink type and size
     * @param drinkType Type of drink (Americano, Latte, Cappuccino)
     * @param size Size of drink (Small, Medium, Large)
     * @param price Price to set
     */
    public void setPricing(String drinkType, String size, double price) {
        Map<String, Double> drinkPricing = pricing.get(drinkType);
        if (drinkPricing != null) {
            drinkPricing.put(size, price);
        }
    }

    // ========================================
    // INFORMATION DISPLAY
    // ========================================

    /**
     * Displays comprehensive truck information including all required details
     */
    public void displayTruckInfo() {
        System.out.println("Truck Type: " + getTruckType());
        System.out.println("Location: " + location);

        System.out.println("\nSTORAGE BINS");
        System.out.println("-".repeat(40));
        displayStorageBins();

        System.out.println("\nMENU AND PRICING");
        System.out.println("-".repeat(40));
        displayMenu();

        System.out.println("\nSALES TRANSACTIONS");
        System.out.println("-".repeat(40));
        displayTransactions();
    }

    /**
     * Displays current storage bin contents with clear formatting
     */
    public void displayStorageBins() {
        for (int i = 0; i < storageBins.length; i++) {
            StorageBin bin = storageBins[i];
            if (bin != null && !bin.getItemType().equals("Empty") && bin.getCurrentQuantity() > 0) {
                System.out.printf("Bin %d: %s - %d/%d%n",
                        i + 1, bin.getItemType(),
                        bin.getCurrentQuantity(), bin.getMaxCapacity());
            } else {
                System.out.printf("Bin %d: Empty%n", i + 1);
            }
        }
    }

    /**
     * Displays menu with pricing for all drink types and sizes
     */
    public void displayMenu() {
        String[] drinks = {"Americano", "Latte", "Cappuccino"};
        String[] sizes = {"Small", "Medium", "Large"};

        for (String drink : drinks) {
            System.out.println(drink + ":");
            Map<String, Double> drinkPricing = pricing.get(drink);

            for (String size : sizes) {
                Double price = drinkPricing.get(size);
                if (price != null) {
                    System.out.printf("   %s: $%.2f%n", size, price);
                } else {
                    System.out.printf("   %s: Not priced%n", size);
                }
            }
            System.out.println();
        }
    }

    /**
     * Displays transaction history with ingredient usage details
     */
    public void displayTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            System.out.println("Start selling coffee to see transaction history!");
            return;
        }

        System.out.printf("Total Transactions: %d%n%n", transactions.size());

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            System.out.printf("Transaction %d:%n", i + 1);
            System.out.printf("   Drink: %s %s%n", transaction.getSize(), transaction.getDrinkType());
            System.out.printf("   Ingredients Used:%n");
            System.out.printf("      Coffee: %.2f grams%n", transaction.getCoffeeUsed());

            if (transaction.getWaterUsed() > 0) {
                System.out.printf("      Water: %.2f fl.oz.%n", transaction.getWaterUsed());
            }
            if (transaction.getMilkUsed() > 0) {
                System.out.printf("      Milk: %.2f fl.oz.%n", transaction.getMilkUsed());
            }

            System.out.printf("   Total Price: $%.2f%n%n", transaction.getTotalPrice());
        }
    }

    // ========================================
    // GETTERS AND SETTERS
    // ========================================

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public StorageBin[] getStorageBins() { return storageBins; }
    public List<Transaction> getTransactions() { return transactions; }
}

// ========================================
// REGULAR COFFEE TRUCK IMPLEMENTATION
// ========================================

/**
 * Regular Coffee Truck implementation (JavaJeep)
 * Standard 8-bin truck with basic coffee preparation features
 */
class RegularCoffeeTruck extends CoffeeTruck {

    /**
     * Constructor for RegularCoffeeTruck
     * @param location Truck deployment location
     */
    public RegularCoffeeTruck(String location) {
        super(location);
    }

    @Override
    public String getTruckType() {
        return "Regular Coffee Truck (JavaJeep)";
    }
}

// ========================================
// SPECIAL COFFEE TRUCK IMPLEMENTATION
// ========================================

/**
 * Special Coffee Truck implementation (JavaJeep+)
 * Enhanced 10-bin truck with customization features:
 * - Multiple brew types (Standard, Strong, Light, Custom)
 * - Syrup add-ons
 * - Extra shots
 * - Premium pricing options
 */
class SpecialCoffeeTruck extends CoffeeTruck {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    private double syrupPrice;
    private double extraShotPrice;

    // ========================================
    // CONSTRUCTOR
    // ========================================

    /**
     * Constructor for SpecialCoffeeTruck
     * @param location Truck deployment location
     */
    public SpecialCoffeeTruck(String location) {
        super(location);
        this.storageBins = new StorageBin[10]; // Special trucks have 10 bins (8 + 2 for syrups)
        this.syrupPrice = 0.0;
        this.extraShotPrice = 0.0;
    }

    @Override
    public String getTruckType() {
        return "Special Coffee Truck (JavaJeep+)";
    }

    // ========================================
    // ENHANCED PRICING
    // ========================================

    @Override
    protected double calculateTotalPrice(CoffeeOrder order) {
        double basePrice = super.calculateTotalPrice(order);

        // Add syrup pricing
        if (order.getAddons() != null && !order.getAddons().isEmpty()) {
            basePrice += order.getAddons().size() * syrupPrice;
        }

        // Add extra shot pricing
        if (order.isExtraShot()) {
            basePrice += extraShotPrice;
        }

        return basePrice;
    }

    /**
     * Sets pricing for add-on features
     * @param syrupPrice Price per syrup add-on
     * @param extraShotPrice Price per extra shot
     */
    public void setAddonPricing(double syrupPrice, double extraShotPrice) {
        this.syrupPrice = syrupPrice;
        this.extraShotPrice = extraShotPrice;
    }

    // ========================================
    // ENHANCED MENU DISPLAY
    // ========================================

    @Override
    public void displayMenu() {
        super.displayMenu();

        System.out.println("ADD-ONS:");
        System.out.printf("   Syrup: $%.2f%n", syrupPrice);
        System.out.printf("   Extra Shot: $%.2f%n%n", extraShotPrice);

        System.out.println("BREW OPTIONS:");
        System.out.println("   Standard (1:18) - Balanced flavor");
        System.out.println("   Strong (1:15) - Bold and intense");
        System.out.println("   Light (1:20) - Smooth and mild");
        System.out.println("   Custom (1:?) - Your perfect ratio");
    }

    // ========================================
    // SYRUP MANAGEMENT
    // ========================================

    /**
     * Gets list of available syrups from storage bins 9 and 10
     * @return List of available syrup names
     */
    public List<String> getAvailableSyrups() {
        List<String> availableSyrups = new ArrayList<>();

        // Check bins 9 and 10 (indices 8 and 9) for syrups
        for (int i = 8; i < storageBins.length; i++) {
            StorageBin bin = storageBins[i];
            if (bin != null &&
                    bin.getItemType().contains("Syrup") &&
                    bin.getCurrentQuantity() > 0) {
                availableSyrups.add(bin.getItemType());
            }
        }

        return availableSyrups;
    }
}

// ========================================
// STORAGE BIN CLASS
// ========================================

/**
 * Storage bin class to hold ingredients and cups
 * Manages capacity limits and inventory tracking
 */
class StorageBin {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    private String itemType;
    private int currentQuantity;
    private int maxCapacity;

    // ========================================
    // CONSTRUCTOR
    // ========================================

    /**
     * Constructor for StorageBin
     * @param itemType Type of item stored
     * @param currentQuantity Current quantity in bin
     * @param maxCapacity Maximum capacity of bin
     */
    public StorageBin(String itemType, int currentQuantity, int maxCapacity) {
        this.itemType = itemType;
        this.currentQuantity = Math.max(0, currentQuantity);
        this.maxCapacity = Math.max(0, maxCapacity);

        // Ensure current quantity doesn't exceed capacity
        if (this.currentQuantity > this.maxCapacity) {
            this.currentQuantity = this.maxCapacity;
        }
    }

    // ========================================
    // INVENTORY OPERATIONS
    // ========================================

    /**
     * Adds items to the bin, respecting capacity limits
     * @param amount Amount to add
     * @return Actual amount added
     */
    public int addItems(int amount) {
        if (amount <= 0) return 0;

        int spaceAvailable = maxCapacity - currentQuantity;
        int amountToAdd = Math.min(amount, spaceAvailable);
        currentQuantity += amountToAdd;

        return amountToAdd;
    }

    /**
     * Removes items from the bin
     * @param amount Amount to remove
     * @return Actual amount removed
     */
    public int removeItems(int amount) {
        if (amount <= 0) return 0;

        int amountToRemove = Math.min(amount, currentQuantity);
        currentQuantity -= amountToRemove;

        return amountToRemove;
    }

    /**
     * Checks if bin has sufficient quantity
     * @param requiredAmount Amount required
     * @return true if sufficient, false otherwise
     */
    public boolean hasSufficientQuantity(int requiredAmount) {
        return currentQuantity >= requiredAmount;
    }

    /**
     * Gets percentage of capacity used
     * @return Percentage (0-100)
     */
    public double getUsagePercentage() {
        if (maxCapacity == 0) return 0.0;
        return (double) currentQuantity / maxCapacity * 100.0;
    }

    // ========================================
    // GETTERS
    // ========================================

    public String getItemType() { return itemType; }
    public int getCurrentQuantity() { return currentQuantity; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getAvailableSpace() { return maxCapacity - currentQuantity; }
    public boolean isEmpty() { return currentQuantity == 0; }
    public boolean isFull() { return currentQuantity >= maxCapacity; }
}

// ========================================
// COFFEE ORDER CLASS
// ========================================

/**
 * Coffee order class to hold complete order details
 * Encapsulates all customer preferences and customizations
 */
class CoffeeOrder {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    private String drinkType;
    private String size;
    private String brewType;
    private List<String> addons;
    private boolean extraShot;

    // ========================================
    // CONSTRUCTOR
    // ========================================

    /**
     * Constructor for CoffeeOrder
     * @param drinkType Type of drink (Americano, Latte, Cappuccino)
     * @param size Size of drink (Small, Medium, Large)
     * @param brewType Brew strength (Standard, Strong, Light, Custom)
     * @param addons List of syrup add-ons
     * @param extraShot Whether to add an extra shot
     */
    public CoffeeOrder(String drinkType, String size, String brewType,
                       List<String> addons, boolean extraShot) {
        this.drinkType = drinkType;
        this.size = size;
        this.brewType = brewType;
        this.addons = addons != null ? new ArrayList<>(addons) : new ArrayList<>();
        this.extraShot = extraShot;
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Gets a formatted description of the order
     * @return Human-readable order description
     */
    public String getOrderDescription() {
        StringBuilder description = new StringBuilder();
        description.append(size).append(" ").append(drinkType);

        if (!brewType.equals("Standard")) {
            description.append(" (").append(brewType).append(" brew)");
        }

        if (!addons.isEmpty()) {
            description.append(" with ").append(String.join(", ", addons));
        }

        if (extraShot) {
            description.append(" + extra shot");
        }

        return description.toString();
    }

    /**
     * Checks if this is a customized order
     * @return true if order has customizations beyond standard
     */
    public boolean isCustomized() {
        return !brewType.equals("Standard") || !addons.isEmpty() || extraShot;
    }

    // ========================================
    // GETTERS
    // ========================================

    public String getDrinkType() { return drinkType; }
    public String getSize() { return size; }
    public String getBrewType() { return brewType; }
    public List<String> getAddons() { return new ArrayList<>(addons); }
    public boolean isExtraShot() { return extraShot; }
}

// ========================================
// INGREDIENT REQUIREMENT CLASS
// ========================================

/**
 * Ingredient requirement class for precise calculations
 * Holds exact amounts needed for coffee preparation
 */
class IngredientRequirement {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    private double coffeeGrams;
    private double waterOz;
    private double milkOz;

    // ========================================
    // CONSTRUCTOR
    // ========================================

    /**
     * Constructor for IngredientRequirement
     * @param coffeeGrams Coffee amount in grams
     * @param waterOz Water amount in fluid ounces
     * @param milkOz Milk amount in fluid ounces
     */
    public IngredientRequirement(double coffeeGrams, double waterOz, double milkOz) {
        this.coffeeGrams = Math.max(0, coffeeGrams);
        this.waterOz = Math.max(0, waterOz);
        this.milkOz = Math.max(0, milkOz);
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Gets total ingredient cost based on unit costs
     * @param coffeePerGram Cost per gram of coffee
     * @param waterPerOz Cost per fluid ounce of water
     * @param milkPerOz Cost per fluid ounce of milk
     * @return Total ingredient cost
     */
    public double calculateIngredientCost(double coffeePerGram, double waterPerOz, double milkPerOz) {
        return (coffeeGrams * coffeePerGram) +
                (waterOz * waterPerOz) +
                (milkOz * milkPerOz);
    }

    /**
     * Gets formatted string representation of requirements
     * @return Human-readable ingredient breakdown
     */
    public String getFormattedRequirements() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Coffee: %.2fg", coffeeGrams));

        if (waterOz > 0) {
            sb.append(String.format(", Water: %.2f fl.oz.", waterOz));
        }

        if (milkOz > 0) {
            sb.append(String.format(", Milk: %.2f fl.oz.", milkOz));
        }

        return sb.toString();
    }

    // ========================================
    // GETTERS
    // ========================================

    public double getCoffeeGrams() { return coffeeGrams; }
    public double getWaterOz() { return waterOz; }
    public double getMilkOz() { return milkOz; }
}

// ========================================
// TRANSACTION CLASS
// ========================================

/**
 * Transaction class to record sales and ingredient usage
 * Maintains complete history for reporting and analytics
 */
class Transaction {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    private String drinkType;
    private String size;
    private String brewType;
    private List<String> addons;
    private boolean extraShot;
    private double coffeeUsed;
    private double waterUsed;
    private double milkUsed;
    private double totalPrice;
    private long timestamp;

    // ========================================
    // CONSTRUCTOR
    // ========================================

    /**
     * Constructor for Transaction
     * @param order The coffee order
     * @param requirement The ingredient requirements
     * @param totalPrice Total price of the transaction
     */
    public Transaction(CoffeeOrder order, IngredientRequirement requirement, double totalPrice) {
        this.drinkType = order.getDrinkType();
        this.size = order.getSize();
        this.brewType = order.getBrewType();
        this.addons = new ArrayList<>(order.getAddons());
        this.extraShot = order.isExtraShot();
        this.coffeeUsed = requirement.getCoffeeGrams();
        this.waterUsed = requirement.getWaterOz();
        this.milkUsed = requirement.getMilkOz();
        this.totalPrice = totalPrice;
        this.timestamp = System.currentTimeMillis();
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Gets formatted transaction summary
     * @return Complete transaction details as string
     */
    public String getTransactionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(size).append(" ").append(drinkType);

        if (!brewType.equals("Standard")) {
            summary.append(" (").append(brewType).append(")");
        }

        if (!addons.isEmpty()) {
            summary.append(" + ").append(String.join(", ", addons));
        }

        if (extraShot) {
            summary.append(" + extra shot");
        }

        summary.append(String.format(" - $%.2f", totalPrice));

        return summary.toString();
    }

    /**
     * Calculates profit margin if cost data is available
     * @param ingredientCost Total cost of ingredients used
     * @return Profit amount
     */
    public double calculateProfit(double ingredientCost) {
        return totalPrice - ingredientCost;
    }

    // ========================================
    // GETTERS
    // ========================================

    public String getDrinkType() { return drinkType; }
    public String getSize() { return size; }
    public String getBrewType() { return brewType; }
    public List<String> getAddons() { return new ArrayList<>(addons); }
    public boolean isExtraShot() { return extraShot; }
    public double getCoffeeUsed() { return coffeeUsed; }
    public double getWaterUsed() { return waterUsed; }
    public double getMilkUsed() { return milkUsed; }
    public double getTotalPrice() { return totalPrice; }
    public long getTimestamp() { return timestamp; }
}