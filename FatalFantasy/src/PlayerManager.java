import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * PlayerManager handles character creation, editing, and management
 */
public class PlayerManager {
    private List<Character> characters;
    private static final int MAX_CHARACTERS = 6;

    /**
     * Constructor for PlayerManager
     */
    public PlayerManager() {
        this.characters = new ArrayList<>();
    }

    /**
     * Displays all characters in list format
     */
    public void viewAllCharacters() {
        if (characters.isEmpty()) {
            System.out.println("No characters created yet.");
            return;
        }

        System.out.println("\n--- YOUR CHARACTERS ---");
        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);
            System.out.println((i + 1) + ". " + character.getName() +
                    " (" + character.getCharacterClass().getDisplayName() + ")");
        }
    }

    /**
     * Displays detailed view of a selected character
     */
    public void viewCharacterDetails() {
        if (characters.isEmpty()) {
            System.out.println("No characters created yet.");
            return;
        }

        viewAllCharacters();
        System.out.print("Select character to view details (1-" + characters.size() + "): ");
        Scanner scanner = new Scanner(System.in);
        int choice = getValidChoice(scanner, 1, characters.size());

        Character character = characters.get(choice - 1);
        displayCharacterDetails(character);
    }

    /**
     * Displays detailed information about a character
     * @param character the character to display
     */
    private void displayCharacterDetails(Character character) {
        System.out.println("\n=== CHARACTER DETAILS ===");
        System.out.println("Name: " + character.getName());
        System.out.println("Class: " + character.getCharacterClass().getDisplayName());
        System.out.println("HP: " + character.getCurrentHP() + "/" + character.getMaxHP());
        System.out.println("EP: " + character.getCurrentEP() + "/" + character.getMaxEP());
        System.out.println("\nSelected Abilities:");
        List<Ability> abilities = character.getSelectedAbilities();
        for (int i = 0; i < abilities.size(); i++) {
            System.out.println((i + 1) + ". " + abilities.get(i));
        }
        System.out.println("\nUniversal Abilities:");
        System.out.println("- Defend (5 EP): Take defensive stance, take half damage");
        System.out.println("- Recharge (0 EP): Do nothing but regain 5 EP");
    }

    /**
     * Creates a new character through the creation flow
     * @param scanner Scanner for user input
     */
    public void createCharacter(Scanner scanner) {
        if (characters.size() >= MAX_CHARACTERS) {
            System.out.println("Maximum number of characters (" + MAX_CHARACTERS + ") reached!");
            return;
        }

        System.out.println("\n--- CHARACTER CREATION ---");

        // Step 1: Class Selection
        CharacterClass selectedClass = selectClass(scanner);

        // Step 2: Ability Selection
        List<Ability> selectedAbilities = selectAbilities(scanner, selectedClass);

        // Step 3: Name Character
        String name = getUniqueCharacterName(scanner);

        // Create character
        Character newCharacter = new Character(name, selectedClass, selectedAbilities);
        characters.add(newCharacter);

        System.out.println("Character '" + name + "' created successfully!");
    }

    /**
     * Allows editing of character abilities
     * @param scanner Scanner for user input
     */
    public void editCharacter(Scanner scanner) {
        if (characters.isEmpty()) {
            System.out.println("No characters to edit.");
            return;
        }

        viewAllCharacters();
        System.out.print("Select character to edit (1-" + characters.size() + "): ");
        int choice = getValidChoice(scanner, 1, characters.size());

        Character character = characters.get(choice - 1);
        System.out.println("Editing abilities for: " + character.getName());

        List<Ability> newAbilities = selectAbilities(scanner, character.getCharacterClass());
        character.setSelectedAbilities(newAbilities);

        System.out.println("Character abilities updated successfully!");
    }

    /**
     * Deletes a character
     * @param scanner Scanner for user input
     */
    public void deleteCharacter(Scanner scanner) {
        if (characters.isEmpty()) {
            System.out.println("No characters to delete.");
            return;
        }

        viewAllCharacters();
        System.out.print("Select character to delete (1-" + characters.size() + "): ");
        int choice = getValidChoice(scanner, 1, characters.size());

        Character character = characters.get(choice - 1);
        System.out.print("Are you sure you want to delete '" + character.getName() + "'? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            characters.remove(choice - 1);
            System.out.println("Character deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Allows user to select a character class
     * @param scanner Scanner for user input
     * @return selected CharacterClass
     */
    private CharacterClass selectClass(Scanner scanner) {
        System.out.println("Select a class:");
        CharacterClass[] classes = CharacterClass.values();
        for (int i = 0; i < classes.length; i++) {
            System.out.println((i + 1) + ". " + classes[i].getDisplayName());
        }

        System.out.print("Choose class (1-" + classes.length + "): ");
        int choice = getValidChoice(scanner, 1, classes.length);
        return classes[choice - 1];
    }

    /**
     * Allows user to select 3 abilities from class abilities
     * @param scanner Scanner for user input
     * @param characterClass the character class
     * @return list of selected abilities
     */
    private List<Ability> selectAbilities(Scanner scanner, CharacterClass characterClass) {
        List<Ability> classAbilities = characterClass.getAbilities();
        List<Ability> selectedAbilities = new ArrayList<>();

        System.out.println("\nAvailable abilities for " + characterClass.getDisplayName() + ":");
        for (int i = 0; i < classAbilities.size(); i++) {
            System.out.println((i + 1) + ". " + classAbilities.get(i));
        }

        System.out.println("\nSelect 3 abilities:");
        for (int i = 0; i < 3; i++) {
            System.out.print("Choose ability " + (i + 1) + " (1-" + classAbilities.size() + "): ");
            int choice = getValidChoice(scanner, 1, classAbilities.size());
            Ability selectedAbility = classAbilities.get(choice - 1);

            if (selectedAbilities.contains(selectedAbility)) {
                System.out.println("Ability already selected! Choose a different one.");
                i--; // Retry this selection
            } else {
                selectedAbilities.add(selectedAbility);
                System.out.println("Selected: " + selectedAbility.getName());
            }
        }

        return selectedAbilities;
    }

    /**
     * Gets a unique character name
     * @param scanner Scanner for user input
     * @return unique character name
     */
    private String getUniqueCharacterName(Scanner scanner) {
        while (true) {
            System.out.print("Enter character name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
                continue;
            }

            boolean nameExists = false;
            for (Character character : characters) {
                if (character.getName().equalsIgnoreCase(name)) {
                    nameExists = true;
                    break;
                }
            }

            if (nameExists) {
                System.out.println("Name already exists! Choose a different name.");
            } else {
                return name;
            }
        }
    }

    /**
     * Gets valid choice input within range
     * @param scanner Scanner for user input
     * @param min minimum valid value
     * @param max maximum valid value
     * @return valid choice
     */
    private int getValidChoice(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Gets character for battle selection
     * @param playerName name of the player
     * @param scanner Scanner for user input
     * @return selected character for battle
     */
    public Character selectCharacterForBattle(String playerName, Scanner scanner) {
        System.out.println("\n" + playerName + ", select your character:");
        viewAllCharacters();
        System.out.print("Choose character (1-" + characters.size() + "): ");
        int choice = getValidChoice(scanner, 1, characters.size());
        return characters.get(choice - 1);
    }

    public int getCharacterCount() { return characters.size(); }
    public List<Character> getCharacters() { return new ArrayList<>(characters); }
}