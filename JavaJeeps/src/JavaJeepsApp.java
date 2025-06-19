import java.util.*;

/**
 * JavaJeeps Coffee Truck Business Simulation
 * Main application class that handles the user interface and menu system
 *
 * This class implements the main menu system and coordinates all truck operations
 * including creation, simulation, and dashboard features.
 *
 * @author Student Name
 * @version 1.0
 * @since 2024
 */
public class JavaJeepsApp {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    private static Scanner scanner = new Scanner(System.in);
    private static List<CoffeeTruck> trucks = new ArrayList<>();

    // ========================================
    // MAIN METHOD
    // ========================================

    /**
     * Main method to start the JavaJeeps application
     * Displays welcome message and runs the main menu loop
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        displayWelcomeMessage();
        runMainMenuLoop();
        cleanup();
    }

    // ========================================
    // MAIN MENU SYSTEM
    // ========================================

    /**
     * Displays the welcome message for the application
     */
    private static void displayWelcomeMessage() {
        System.out.println("=".repeat(60));
        System.out.println("        JAVAJEEPS COFFEE TRUCK BUSINESS SIMULATION");
        System.out.println("=".repeat(60));
        System.out.println("Welcome to JavaJeeps - Mobile Coffee on Wheels!");
        System.out.println("Inspired by the iconic Filipino Jolli-jeeps");
        System.out.println();
    }

    /**
     * Runs the main menu loop until user chooses to exit
     */
    private static void runMainMenuLoop() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleCreateCoffeeTruck();
                    break;
                case 2:
                    handleSimulateCoffeeTruckFeatures();
                    break;
                case 3:
                    handleDisplayDashboard();
                    break;
                case 4:
                    running = handleExit();
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-4.");
            }

            if (running) {
                pauseForUser();
            }
        }
    }

    /**
     * Displays the main menu options
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("              MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. Create a Coffee Truck");
        System.out.println("2. Simulate Coffee Truck features");
        System.out.println("3. Dashboard Feature");
        System.out.println("4. Exit");
        System.out.println("=".repeat(40));
    }

    // ========================================
    // MENU HANDLERS
    // ========================================

    /**
     * Handles the coffee truck creation process
     */
    private static void handleCreateCoffeeTruck() {
        System.out.println("\nCREATE COFFEE TRUCK");
        System.out.println("-".repeat(40));
        createCoffeeTruck();
    }

    /**
     * Handles the truck simulation features
     */
    private static void handleSimulateCoffeeTruckFeatures() {
        System.out.println("\nSIMULATE COFFEE TRUCK FEATURES");
        System.out.println("-".repeat(40));
        simulateCoffeeTruckFeatures();
    }

    /**
     * Handles the dashboard display
     */
    private static void handleDisplayDashboard() {
        System.out.println("\nDASHBOARD");
        System.out.println("-".repeat(40));
        displayDashboard();
    }

    /**
     * Handles application exit
     * @return false to stop the main loop
     */
    private static boolean handleExit() {
        System.out.println("\nEXITING APPLICATION");
        System.out.println("-".repeat(40));
        System.out.println("Thank you for using JavaJeeps!");
        System.out.println("Your mobile coffee adventure ends here.");
        return false;
    }

    // ========================================
    // COFFEE TRUCK CREATION
    // ========================================

    /**
     * Creates a new coffee truck (Regular or Special)
     */
    private static void createCoffeeTruck() {
        // Display truck type options
        System.out.println("Choose your JavaJeep type:");
        System.out.println("1. Regular Coffee Truck (JavaJeep)");
        System.out.println("2. Special Coffee Truck (JavaJeep+)");

        int truckType = getIntInput("Select truck type (1-2): ");

        if (truckType != 1 && truckType != 2) {
            System.out.println("Invalid truck type! Please select 1 or 2.");
            return;
        }

        // Get truck location
        String location = getTruckLocation();
        if (location == null) return; // Location already taken

        // Create appropriate truck type
        CoffeeTruck newTruck = createTruckInstance(truckType, location);

        // Perform initial loadout and setup
        performInitialLoadout(newTruck);

        // Add truck to fleet
        trucks.add(newTruck);

        System.out.println("Coffee truck created successfully!");
        System.out.println("Location: " + location);
        System.out.println("Type: " + newTruck.getTruckType());
    }

    /**
     * Gets and validates truck location
     * @return Location string, or null if location is taken
     */
    private static String getTruckLocation() {
        System.out.print("Enter truck location: ");
        String location = scanner.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("Location cannot be empty!");
            return null;
        }

        // Check if location is already taken
        for (CoffeeTruck truck : trucks) {
            if (truck.getLocation().equalsIgnoreCase(location)) {
                System.out.println("Location already occupied by another truck!");
                System.out.println("Existing truck: " + truck.getTruckType());
                return null;
            }
        }

        return location;
    }

    /**
     * Creates truck instance based on type
     * @param truckType 1 for Regular, 2 for Special
     * @param location Truck location
     * @return New CoffeeTruck instance
     */
    private static CoffeeTruck createTruckInstance(int truckType, String location) {
        if (truckType == 1) {
            return new RegularCoffeeTruck(location);
        } else {
            return new SpecialCoffeeTruck(location);
        }
    }

    // ========================================
    // INITIAL LOADOUT PROCESS
    // ========================================

    /**
     * Performs initial loadout of the truck with storage bins and pricing
     * @param truck The truck to configure
     */
    private static void performInitialLoadout(CoffeeTruck truck) {
        System.out.println("\nINITIAL LOADOUT PROCESS");
        System.out.println("-".repeat(50));
        System.out.println("Configure your truck's storage bins and pricing...");

        configureStorageBins(truck);
        configurePricing(truck);

        System.out.println("Initial loadout completed!");
    }

    /**
     * Configures storage bins for the truck
     * @param truck The truck to configure
     */
    private static void configureStorageBins(CoffeeTruck truck) {
        StorageBin[] bins = truck.getStorageBins();

        for (int i = 0; i < bins.length; i++) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("STORAGE BIN " + (i + 1));
            System.out.println("-".repeat(30));

            displayAvailableItems(truck, i);

            int itemChoice = getIntInput("Select item for bin " + (i + 1) + " (0 to skip): ");

            if (itemChoice == 0) {
                bins[i] = new StorageBin("Empty", 0, 0);
                System.out.println("Bin " + (i + 1) + " left empty");
                continue;
            }

            ItemConfig config = getItemConfig(itemChoice, truck, i);
            if (config == null) {
                i--; // Retry this bin
                continue;
            }

            int quantity = getQuantityForItem(config);
            bins[i] = new StorageBin(config.itemType, quantity, config.maxCapacity);

            System.out.println("Bin " + (i + 1) + " configured: " +
                    config.itemType + " (" + quantity + "/" + config.maxCapacity + ")");
        }
    }

    /**
     * Displays available items for storage bin configuration
     * @param truck The truck being configured
     * @param binIndex Current bin index
     */
    private static void displayAvailableItems(CoffeeTruck truck, int binIndex) {
        System.out.println("Available items:");
        System.out.println("1. Small Cup (capacity: 80)");
        System.out.println("2. Medium Cup (capacity: 64)");
        System.out.println("3. Large Cup (capacity: 40)");
        System.out.println("4. Coffee Beans (capacity: 1008g)");
        System.out.println("5. Milk (capacity: 640 fl.oz.)");
        System.out.println("6. Water (capacity: 640 fl.oz.)");

        if (truck instanceof SpecialCoffeeTruck && binIndex >= 8) {
            System.out.println("7. Syrup Add-on (capacity: 640 fl.oz.)");
        }

        System.out.println("0. Leave empty");
    }

    /**
     * Helper class to hold item configuration
     */
    private static class ItemConfig {
        String itemType;
        int maxCapacity;

        ItemConfig(String itemType, int maxCapacity) {
            this.itemType = itemType;
            this.maxCapacity = maxCapacity;
        }
    }

    /**
     * Gets item configuration based on user choice
     * @param choice User's item choice
     * @param truck The truck being configured
     * @param binIndex Current bin index
     * @return ItemConfig or null if invalid
     */
    private static ItemConfig getItemConfig(int choice, CoffeeTruck truck, int binIndex) {
        switch (choice) {
            case 1: return new ItemConfig("Small Cup", 80);
            case 2: return new ItemConfig("Medium Cup", 64);
            case 3: return new ItemConfig("Large Cup", 40);
            case 4: return new ItemConfig("Coffee Beans", 1008);
            case 5: return new ItemConfig("Milk", 640);
            case 6: return new ItemConfig("Water", 640);
            case 7:
                if (truck instanceof SpecialCoffeeTruck && binIndex >= 8) {
                    return getSyrupConfig();
                } else {
                    System.out.println("Syrup add-ons only available in bins 9-10 for Special trucks!");
                    return null;
                }
            default:
                System.out.println("Invalid choice! Please select a valid option.");
                return null;
        }
    }

    /**
     * Gets syrup configuration from user
     * @return ItemConfig for syrup
     */
    private static ItemConfig getSyrupConfig() {
        System.out.print("Enter syrup type (hazelnut, chocolate, vanilla, etc.): ");
        String syrupType = scanner.nextLine().trim();

        if (syrupType.isEmpty()) {
            System.out.println("Syrup type cannot be empty!");
            return null;
        }

        return new ItemConfig(syrupType + " Syrup", 640);
    }

    /**
     * Gets quantity for an item with validation
     * @param config Item configuration
     * @return Validated quantity
     */
    private static int getQuantityForItem(ItemConfig config) {
        int quantity = getIntInput("Enter quantity (max " + config.maxCapacity + "): ");

        if (quantity > config.maxCapacity) {
            quantity = config.maxCapacity;
            System.out.println("Quantity adjusted to maximum capacity: " + config.maxCapacity);
        } else if (quantity < 0) {
            quantity = 0;
            System.out.println("Quantity cannot be negative. Set to 0.");
        }

        return quantity;
    }

    // ========================================
    // PRICING CONFIGURATION
    // ========================================

    /**
     * Configures pricing for all coffee drinks and add-ons
     * @param truck The truck to configure pricing for
     */
    private static void configurePricing(CoffeeTruck truck) {
        System.out.println("\nPRICING SETUP");
        System.out.println("-".repeat(40));
        System.out.println("Set prices for your coffee menu...");

        configureDrinkPricing(truck);

        if (truck instanceof SpecialCoffeeTruck) {
            configureAddonPricing((SpecialCoffeeTruck) truck);
        }
    }

    /**
     * Configures pricing for coffee drinks
     * @param truck The truck to configure
     */
    private static void configureDrinkPricing(CoffeeTruck truck) {
        String[] drinks = {"Americano", "Latte", "Cappuccino"};
        String[] sizes = {"Small", "Medium", "Large"};

        for (String drink : drinks) {
            System.out.println("\n" + drink.toUpperCase() + " PRICING");
            System.out.println("-".repeat(25));

            for (String size : sizes) {
                double price = getDoubleInput(size + " " + drink + " price: $");
                truck.setPricing(drink, size, price);
            }
        }
    }

    /**
     * Configures add-on pricing for special trucks
     * @param truck The special truck to configure
     */
    private static void configureAddonPricing(SpecialCoffeeTruck truck) {
        System.out.println("\nADD-ON PRICING");
        System.out.println("-".repeat(25));

        double syrupPrice = getDoubleInput("Syrup add-on price: $");
        double extraShotPrice = getDoubleInput("Extra shot price: $");

        truck.setAddonPricing(syrupPrice, extraShotPrice);

        System.out.println("Add-on pricing configured!");
    }

    // ========================================
    // TRUCK SIMULATION FEATURES
    // ========================================

    /**
     * Simulates coffee truck features for a selected truck
     */
    private static void simulateCoffeeTruckFeatures() {
        if (trucks.isEmpty()) {
            System.out.println("No trucks available. Please create a truck first.");
            return;
        }

        CoffeeTruck selectedTruck = selectTruck();
        if (selectedTruck == null) return;

        runTruckSimulation(selectedTruck);
    }

    /**
     * Runs the simulation menu for a specific truck
     * @param truck The truck to simulate
     */
    private static void runTruckSimulation(CoffeeTruck truck) {
        boolean simulating = true;

        while (simulating) {
            displayTruckSimulationMenu(truck);
            int choice = getIntInput("Choose feature (1-4): ");

            switch (choice) {
                case 1:
                    processCoffeeSale(truck);
                    break;
                case 2:
                    viewTruckInformation(truck);
                    break;
                case 3:
                    handleRestockingAndMaintenance(truck);
                    break;
                case 4:
                    simulating = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1-4.");
            }

            if (simulating) {
                pauseForUser();
            }
        }
    }

    /**
     * Displays the truck simulation menu
     * @param truck The truck being simulated
     */
    private static void displayTruckSimulationMenu(CoffeeTruck truck) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TRUCK SIMULATION - " + truck.getTruckType());
        System.out.println("Location: " + truck.getLocation());
        System.out.println("=".repeat(50));
        System.out.println("1. Sale and preparation of coffee drink");
        System.out.println("2. View truck information");
        System.out.println("3. Restocking and Maintenance");
        System.out.println("4. Back to main menu");
        System.out.println("=".repeat(50));
    }

    // ========================================
    // COFFEE SALES PROCESSING
    // ========================================

    /**
     * Processes a coffee sale order
     * @param truck The truck processing the order
     */
    private static void processCoffeeSale(CoffeeTruck truck) {
        System.out.println("\nCOFFEE SALE PROCESS");
        System.out.println("-".repeat(40));

        // Get drink selection
        String drinkType = selectDrinkType();
        if (drinkType == null) return;

        // Get size selection
        String size = selectDrinkSize();
        if (size == null) return;

        // Get customizations for special trucks
        OrderCustomization customization = getOrderCustomization(truck);
        if (customization == null) return;

        // Create and process order
        CoffeeOrder order = new CoffeeOrder(
                drinkType, size, customization.brewType,
                customization.addons, customization.extraShot
        );

        boolean success = truck.processOrder(order);

        displayOrderResult(success);
    }

    /**
     * Lets user select drink type
     * @return Selected drink type or null if invalid
     */
    private static String selectDrinkType() {
        System.out.println("Available drinks:");
        System.out.println("1. Americano");
        System.out.println("2. Latte");
        System.out.println("3. Cappuccino");

        int choice = getIntInput("Select drink (1-3): ");

        switch (choice) {
            case 1: return "Americano";
            case 2: return "Latte";
            case 3: return "Cappuccino";
            default:
                System.out.println("Invalid drink choice!");
                return null;
        }
    }

    /**
     * Lets user select drink size
     * @return Selected size or null if invalid
     */
    private static String selectDrinkSize() {
        System.out.println("\nAvailable sizes:");
        System.out.println("1. Small (8 fl. oz.)");
        System.out.println("2. Medium (12 fl. oz.)");
        System.out.println("3. Large (16 fl. oz.)");

        int choice = getIntInput("Select size (1-3): ");

        switch (choice) {
            case 1: return "Small";
            case 2: return "Medium";
            case 3: return "Large";
            default:
                System.out.println("Invalid size choice!");
                return null;
        }
    }

    /**
     * Helper class for order customization
     */
    private static class OrderCustomization {
        String brewType;
        List<String> addons;
        boolean extraShot;

        OrderCustomization(String brewType, List<String> addons, boolean extraShot) {
            this.brewType = brewType;
            this.addons = addons;
            this.extraShot = extraShot;
        }
    }

    /**
     * Gets order customization for special trucks
     * @param truck The truck processing the order
     * @return OrderCustomization or null if cancelled
     */
    private static OrderCustomization getOrderCustomization(CoffeeTruck truck) {
        String brewType = "Standard";
        List<String> addons = new ArrayList<>();
        boolean extraShot = false;

        if (truck instanceof SpecialCoffeeTruck) {
            System.out.println("\nCUSTOMIZATION OPTIONS");
            System.out.println("-".repeat(35));

            brewType = selectBrewType();
            if (brewType == null) return null;

            addons = selectSyrupAddons((SpecialCoffeeTruck) truck);
            extraShot = getYesNoInput("Add extra shot? (y/n): ");
        }

        return new OrderCustomization(brewType, addons, extraShot);
    }

    /**
     * Lets user select brew type for special trucks
     * @return Selected brew type or null if invalid
     */
    private static String selectBrewType() {
        System.out.println("Brew types:");
        System.out.println("1. Standard (1:18)");
        System.out.println("2. Strong (1:15)");
        System.out.println("3. Light (1:20)");
        System.out.println("4. Custom ratio");

        int choice = getIntInput("Select brew type (1-4): ");

        switch (choice) {
            case 1: return "Standard";
            case 2: return "Strong";
            case 3: return "Light";
            case 4:
                int ratio = getIntInput("Enter water ratio (1:?): ");
                if (ratio <= 0) {
                    System.out.println("Invalid ratio!");
                    return null;
                }
                return "Custom 1:" + ratio;
            default:
                System.out.println("Invalid choice! Using Standard brew.");
                return "Standard";
        }
    }

    /**
     * Lets user select syrup add-ons
     * @param truck The special truck
     * @return List of selected syrups
     */
    private static List<String> selectSyrupAddons(SpecialCoffeeTruck truck) {
        List<String> addons = new ArrayList<>();
        List<String> availableSyrups = truck.getAvailableSyrups();

        if (availableSyrups.isEmpty()) {
            System.out.println("No syrups available in storage.");
            return addons;
        }

        System.out.println("\nAvailable syrups:");
        for (int i = 0; i < availableSyrups.size(); i++) {
            System.out.println((i + 1) + ". " + availableSyrups.get(i));
        }
        System.out.println("0. No syrup");

        int choice = getIntInput("Select syrup (0-" + availableSyrups.size() + "): ");

        if (choice > 0 && choice <= availableSyrups.size()) {
            addons.add(availableSyrups.get(choice - 1));
            System.out.println("Added: " + availableSyrups.get(choice - 1));
        }

        return addons;
    }

    /**
     * Displays the result of order processing
     * @param success Whether the order was successful
     */
    private static void displayOrderResult(boolean success) {
        System.out.println("\n" + "=".repeat(40));
        if (success) {
            System.out.println("ORDER COMPLETED SUCCESSFULLY!");
            System.out.println("Enjoy your JavaJeeps coffee!");
        } else {
            System.out.println("ORDER FAILED");
            System.out.println("Insufficient ingredients or supplies!");
            System.out.println("Try restocking your truck or choose a different drink.");
        }
        System.out.println("=".repeat(40));
    }

    // ========================================
    // TRUCK INFORMATION DISPLAY
    // ========================================

    /**
     * Displays comprehensive truck information
     * @param truck The truck to display information for
     */
    private static void viewTruckInformation(CoffeeTruck truck) {
        System.out.println("\nTRUCK INFORMATION");
        System.out.println("=".repeat(60));
        truck.displayTruckInfo();
        System.out.println("=".repeat(60));
    }

    // ========================================
    // RESTOCKING AND MAINTENANCE
    // ========================================

    /**
     * Handles restocking and maintenance operations
     * @param truck The truck to maintain
     */
    private static void handleRestockingAndMaintenance(CoffeeTruck truck) {
        boolean maintaining = true;

        while (maintaining) {
            displayMaintenanceMenu();
            int choice = getIntInput("Choose action (1-6): ");

            switch (choice) {
                case 1:
                    restockStorageBins(truck);
                    break;
                case 2:
                    replaceBinContents(truck);
                    break;
                case 3:
                    emptyStorageBin(truck);
                    break;
                case 4:
                    updateTruckLocation(truck);
                    break;
                case 5:
                    configurePricing(truck);
                    break;
                case 6:
                    maintaining = false;
                    System.out.println("Returning to truck simulation...");
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1-6.");
            }

            if (maintaining) {
                pauseForUser();
            }
        }
    }

    /**
     * Displays the maintenance menu
     */
    private static void displayMaintenanceMenu() {
        System.out.println("\nRESTOCKING & MAINTENANCE");
        System.out.println("-".repeat(40));
        System.out.println("1. Restock storage bins");
        System.out.println("2. Replace bin contents");
        System.out.println("3. Empty storage bin");
        System.out.println("4. Update truck location");
        System.out.println("5. Update pricing");
        System.out.println("6. Back to simulation");
        System.out.println("-".repeat(40));
    }

    /**
     * Handles restocking of storage bins
     * @param truck The truck to restock
     */
    private static void restockStorageBins(CoffeeTruck truck) {
        System.out.println("\nRESTOCK STORAGE BINS");
        System.out.println("-".repeat(35));

        truck.displayStorageBins();

        int binNumber = getIntInput("\nEnter bin number to restock (1-" +
                truck.getStorageBins().length + "): ");

        if (!isValidBinNumber(binNumber, truck.getStorageBins().length)) {
            System.out.println("Invalid bin number!");
            return;
        }

        StorageBin bin = truck.getStorageBins()[binNumber - 1];
        if (bin == null || bin.getItemType().equals("Empty")) {
            System.out.println("Bin is empty. Use 'Replace bin contents' instead.");
            return;
        }

        int addQuantity = getIntInput("Enter quantity to add: ");
        if (addQuantity <= 0) {
            System.out.println("Quantity must be positive!");
            return;
        }

        bin.addItems(addQuantity);
        System.out.println("Bin " + binNumber + " restocked successfully!");
        System.out.println("Current: " + bin.getCurrentQuantity() + "/" + bin.getMaxCapacity());
    }

    /**
     * Handles replacing bin contents
     * @param truck The truck to modify
     */
    private static void replaceBinContents(CoffeeTruck truck) {
        System.out.println("\nREPLACE BIN CONTENTS");
        System.out.println("-".repeat(35));

        truck.displayStorageBins();

        int binNumber = getIntInput("\nEnter bin number to replace (1-" +
                truck.getStorageBins().length + "): ");

        if (!isValidBinNumber(binNumber, truck.getStorageBins().length)) {
            System.out.println("Invalid bin number!");
            return;
        }

        System.out.println("\nReplacing contents of Bin " + binNumber);
        displayAvailableItems(truck, binNumber - 1);

        int itemChoice = getIntInput("Select new item: ");
        ItemConfig config = getItemConfig(itemChoice, truck, binNumber - 1);

        if (config == null) return;

        int quantity = getQuantityForItem(config);
        truck.getStorageBins()[binNumber - 1] = new StorageBin(config.itemType, quantity, config.maxCapacity);

        System.out.println("Bin " + binNumber + " contents replaced successfully!");
        System.out.println("New contents: " + config.itemType + " (" + quantity + "/" + config.maxCapacity + ")");
    }

    /**
     * Handles emptying a storage bin
     * @param truck The truck to modify
     */
    private static void emptyStorageBin(CoffeeTruck truck) {
        System.out.println("\nEMPTY STORAGE BIN");
        System.out.println("-".repeat(30));

        truck.displayStorageBins();

        int binNumber = getIntInput("\nEnter bin number to empty (1-" +
                truck.getStorageBins().length + "): ");

        if (!isValidBinNumber(binNumber, truck.getStorageBins().length)) {
            System.out.println("Invalid bin number!");
            return;
        }

        StorageBin currentBin = truck.getStorageBins()[binNumber - 1];
        if (currentBin != null && !currentBin.getItemType().equals("Empty")) {
            System.out.println("Current contents: " + currentBin.getItemType() +
                    " (" + currentBin.getCurrentQuantity() + " units)");

            if (getYesNoInput("Are you sure you want to empty this bin? (y/n): ")) {
                truck.getStorageBins()[binNumber - 1] = new StorageBin("Empty", 0, 0);
                System.out.println("Bin " + binNumber + " emptied successfully!");
            } else {
                System.out.println("Operation cancelled.");
            }
        } else {
            System.out.println("Bin " + binNumber + " is already empty!");
        }
    }

    /**
     * Handles updating truck location
     * @param truck The truck to relocate
     */
    private static void updateTruckLocation(CoffeeTruck truck) {
        System.out.println("\nUPDATE TRUCK LOCATION");
        System.out.println("-".repeat(35));
        System.out.println("Current location: " + truck.getLocation());

        System.out.print("Enter new location: ");
        String newLocation = scanner.nextLine().trim();

        if (newLocation.isEmpty()) {
            System.out.println("Location cannot be empty!");
            return;
        }

        // Check if new location is already taken
        for (CoffeeTruck t : trucks) {
            if (t != truck && t.getLocation().equalsIgnoreCase(newLocation)) {
                System.out.println("Location already occupied by another truck!");
                System.out.println("Existing truck: " + t.getTruckType());
                return;
            }
        }

        String oldLocation = truck.getLocation();
        truck.setLocation(newLocation);

        System.out.println("Location updated successfully!");
        System.out.println("Old location: " + oldLocation);
        System.out.println("New location: " + newLocation);
    }

    /**
     * Validates bin number
     * @param binNumber The bin number to validate
     * @param maxBins Maximum number of bins
     * @return true if valid, false otherwise
     */
    private static boolean isValidBinNumber(int binNumber, int maxBins) {
        return binNumber >= 1 && binNumber <= maxBins;
    }

    // ========================================
    // DASHBOARD FUNCTIONALITY
    // ========================================

    /**
     * Displays the business dashboard with comprehensive statistics
     */
    private static void displayDashboard() {
        if (trucks.isEmpty()) {
            System.out.println("No trucks deployed yet.");
            System.out.println("Create some trucks first to see dashboard data!");
            return;
        }

        System.out.println("BUSINESS OVERVIEW");
        System.out.println("=".repeat(60));

        displayTruckDeploymentStats();
        displayAggregateInventory();
        displayTransactionSummary();

        System.out.println("=".repeat(60));
    }

    /**
     * Displays truck deployment statistics
     */
    private static void displayTruckDeploymentStats() {
        System.out.println("TRUCK DEPLOYMENT SUMMARY");
        System.out.println("-".repeat(40));

        int regularTrucks = 0;
        int specialTrucks = 0;

        for (CoffeeTruck truck : trucks) {
            if (truck instanceof RegularCoffeeTruck) {
                regularTrucks++;
            } else if (truck instanceof SpecialCoffeeTruck) {
                specialTrucks++;
            }
        }

        System.out.println("Regular Trucks (JavaJeep): " + regularTrucks);
        System.out.println("Special Trucks (JavaJeep+): " + specialTrucks);
        System.out.println("Total Trucks Deployed: " + trucks.size());

        // List all truck locations
        System.out.println("\nTRUCK LOCATIONS:");
        for (int i = 0; i < trucks.size(); i++) {
            CoffeeTruck truck = trucks.get(i);
            String truckType = truck instanceof SpecialCoffeeTruck ? "Special" : "Regular";
            System.out.println("  " + (i + 1) + ". " + truckType + " - " + truck.getLocation());
        }
        System.out.println();
    }

    /**
     * Displays aggregate inventory across all trucks
     */
    private static void displayAggregateInventory() {
        System.out.println("AGGREGATE INVENTORY");
        System.out.println("-".repeat(40));

        Map<String, Integer> totalInventory = calculateTotalInventory();

        if (totalInventory.isEmpty()) {
            System.out.println("No inventory items found across all trucks.");
        } else {
            // Group items by category for better display
            displayInventoryByCategory(totalInventory);
        }
        System.out.println();
    }

    /**
     * Calculates total inventory across all trucks
     * @return Map of item types to total quantities
     */
    private static Map<String, Integer> calculateTotalInventory() {
        Map<String, Integer> totalInventory = new HashMap<>();

        for (CoffeeTruck truck : trucks) {
            for (StorageBin bin : truck.getStorageBins()) {
                if (bin != null && !bin.getItemType().equals("Empty") && bin.getCurrentQuantity() > 0) {
                    totalInventory.merge(bin.getItemType(), bin.getCurrentQuantity(), Integer::sum);
                }
            }
        }

        return totalInventory;
    }

    /**
     * Displays inventory grouped by category
     * @param inventory The inventory map
     */
    private static void displayInventoryByCategory(Map<String, Integer> inventory) {
        // Cups category
        System.out.println("CUPS:");
        displayCategoryItems(inventory, new String[]{"Small Cup", "Medium Cup", "Large Cup"}, "pieces");

        // Ingredients category
        System.out.println("\nINGREDIENTS:");
        System.out.printf("  Coffee Beans: %d grams%n", inventory.getOrDefault("Coffee Beans", 0));
        System.out.printf("  Milk: %d fl.oz.%n", inventory.getOrDefault("Milk", 0));
        System.out.printf("  Water: %d fl.oz.%n", inventory.getOrDefault("Water", 0));

        // Syrups category (for special trucks)
        System.out.println("\nSYRUPS:");
        boolean hasSyrups = false;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getKey().contains("Syrup")) {
                System.out.printf("  %s: %d fl.oz.%n", entry.getKey(), entry.getValue());
                hasSyrups = true;
            }
        }
        if (!hasSyrups) {
            System.out.println("  No syrups in inventory");
        }
    }

    /**
     * Displays items for a specific category
     * @param inventory The inventory map
     * @param items Array of item names to display
     * @param unit Unit of measurement
     */
    private static void displayCategoryItems(Map<String, Integer> inventory, String[] items, String unit) {
        for (String item : items) {
            int quantity = inventory.getOrDefault(item, 0);
            System.out.printf("  %s: %d %s%n", item, quantity, unit);
        }
    }

    /**
     * Displays transaction summary and sales statistics
     */
    private static void displayTransactionSummary() {
        System.out.println("TRANSACTION SUMMARY");
        System.out.println("-".repeat(40));

        Map<String, Integer> drinkCounts = new HashMap<>();
        Map<String, Double> drinkRevenue = new HashMap<>();
        double totalRevenue = 0.0;
        int totalTransactions = 0;

        // Collect transaction data
        for (CoffeeTruck truck : trucks) {
            for (Transaction transaction : truck.getTransactions()) {
                String drinkKey = transaction.getDrinkType() + " (" + transaction.getSize() + ")";

                drinkCounts.merge(drinkKey, 1, Integer::sum);
                drinkRevenue.merge(drinkKey, transaction.getTotalPrice(), Double::sum);
                totalRevenue += transaction.getTotalPrice();
                totalTransactions++;
            }
        }

        if (totalTransactions == 0) {
            System.out.println("No transactions recorded yet.");
            System.out.println("Start selling some coffee to see sales data!");
            return;
        }

        // Display sales by drink type
        System.out.println("SALES BY DRINK TYPE:");
        for (Map.Entry<String, Integer> entry : drinkCounts.entrySet()) {
            String drink = entry.getKey();
            int count = entry.getValue();
            double revenue = drinkRevenue.get(drink);
            System.out.printf("  %s: %d sales - $%.2f%n", drink, count, revenue);
        }

        // Display totals
        System.out.println("\nBUSINESS TOTALS:");
        System.out.printf("  Total Transactions: %d%n", totalTransactions);
        System.out.printf("  Total Revenue: $%.2f%n", totalRevenue);

        if (totalTransactions > 0) {
            double avgTransaction = totalRevenue / totalTransactions;
            System.out.printf("  Average Transaction: $%.2f%n", avgTransaction);
        }
    }

    // ========================================
    // TRUCK SELECTION UTILITY
    // ========================================

    /**
     * Allows user to select a truck from available trucks
     * @return Selected CoffeeTruck or null if cancelled/invalid
     */
    private static CoffeeTruck selectTruck() {
        System.out.println("SELECT TRUCK");
        System.out.println("-".repeat(30));

        for (int i = 0; i < trucks.size(); i++) {
            CoffeeTruck truck = trucks.get(i);
            String truckType = truck instanceof SpecialCoffeeTruck ? "Special" : "Regular";
            System.out.printf("%d. %s - %s at %s%n",
                    i + 1, truckType, truck.getTruckType(), truck.getLocation());
        }

        int choice = getIntInput("\nSelect truck (1-" + trucks.size() + "): ");

        if (choice < 1 || choice > trucks.size()) {
            System.out.println("Invalid truck selection!");
            return null;
        }

        CoffeeTruck selectedTruck = trucks.get(choice - 1);
        System.out.println("Selected: " + selectedTruck.getTruckType() + " at " + selectedTruck.getLocation());

        return selectedTruck;
    }

    // ========================================
    // INPUT UTILITY METHODS
    // ========================================

    /**
     * Gets integer input from user with validation and error handling
     * @param prompt The prompt to display to user
     * @return Valid integer input
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    /**
     * Gets double input from user with validation and error handling
     * @param prompt The prompt to display to user
     * @return Valid double input
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }

                double value = Double.parseDouble(input);

                if (value < 0) {
                    System.out.println("Price cannot be negative. Please enter a positive number.");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number (e.g., 4.50).");
            }
        }
    }

    /**
     * Gets yes/no input from user
     * @param prompt The prompt to display
     * @return true for yes, false for no
     */
    private static boolean getYesNoInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Pauses execution and waits for user to press Enter
     */
    private static void pauseForUser() {
        System.out.println("\n" + "-".repeat(50));
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Cleanup method called before application exit
     */
    private static void cleanup() {
        scanner.close();
        System.out.println("Thanks for choosing JavaJeeps!");
        System.out.println("Your mobile coffee adventure ends here.");
        System.out.println("Come back anytime for more coffee truck simulation!");
    }
}