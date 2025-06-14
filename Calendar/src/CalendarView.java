// File: CalendarView.java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the user interface and display logic for the calendar application.
 * Provides text-based interface for user interaction.
 */
class CalendarView {
    private CalendarSystem system;
    private Scanner scanner;
    private int currentYear;
    private int currentMonth;

    /**
     * Initializes the calendar view with the system.
     * @param system the calendar system to interact with
     */
    public CalendarView(CalendarSystem system) {
        this.system = system;
        this.scanner = new Scanner(System.in);

        // Set current date
        java.time.YearMonth now = DateUtil.getCurrentYearMonth();
        this.currentYear = now.getYear();
        this.currentMonth = now.getMonthValue();
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        System.out.println("=== Welcome to Digital Calendar Application ===");

        while (true) {
            if (system.getCurrentUser() == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    /**
     * Displays the login/register menu.
     */
    private void showLoginMenu() {
        System.out.println("\n--- Login Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleCreateAccount();
                break;
            case 3:
                System.out.println("Thank you for using Digital Calendar!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /**
     * Handles user login process.
     */
    private void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (system.login(username, password)) {
            System.out.println("Login successful! Welcome, " + username + "!");
        } else {
            System.out.println("Invalid credentials or account is inactive.");
        }
    }

    /**
     * Handles account creation process.
     */
    private void handleCreateAccount() {
        System.out.print("Choose username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        System.out.print("Choose password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        if (system.createAccount(username, password)) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Username already exists. Please choose a different username.");
        }
    }

    /**
     * Displays the main application menu for logged-in users.
     */
    private void showMainMenu() {
        displayCurrentMonth();

        System.out.println("\n--- Main Menu (User: " + system.getCurrentUser().getUsername() + ") ---");
        System.out.println("1. Navigate Month");
        System.out.println("2. View Date Details");
        System.out.println("3. Add Calendar Entry");
        System.out.println("4. Edit Calendar Entry");
        System.out.println("5. Delete Calendar Entry");
        System.out.println("6. Manage Calendars");
        System.out.println("7. Account Options");
        System.out.println("8. Logout");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                handleNavigateMonth();
                break;
            case 2:
                handleViewDateDetails();
                break;
            case 3:
                handleAddEntry();
                break;
            case 4:
                handleEditEntry();
                break;
            case 5:
                handleDeleteEntry();
                break;
            case 6:
                handleManageCalendars();
                break;
            case 7:
                handleAccountOptions();
                break;
            case 8:
                system.logout();
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /**
     * Displays the current month in calendar format.
     */
    private void displayCurrentMonth() {
        System.out.println("\n" + "=".repeat(50));
        System.out.printf("           %s %d\n",
                getMonthName(currentMonth), currentYear);
        System.out.println("=".repeat(50));

        // Display calendar headers
        System.out.println("Mon Tue Wed Thu Fri Sat Sun");

        int firstDay = DateUtil.getFirstDayOfWeek(currentYear, currentMonth);
        int daysInMonth = DateUtil.getDaysInMonth(currentYear, currentMonth);

        // Print leading spaces for first week
        for (int i = 1; i < firstDay; i++) {
            System.out.print("    ");
        }

        // Print days with entry indicators
        for (int day = 1; day <= daysInMonth; day++) {
            String dateStr = DateUtil.formatDate(currentYear, currentMonth, day);
            List<CalendarEntry> dayEntries = getAllEntriesForDate(dateStr);

            if (dayEntries.isEmpty()) {
                System.out.printf("%2d  ", day);
            } else {
                System.out.printf("%2d* ", day);  // * indicates entries exist
            }

            // New line after Sunday
            if ((day + firstDay - 2) % 7 == 6) {
                System.out.println();
            }
        }

        if ((daysInMonth + firstDay - 2) % 7 != 6) {
            System.out.println();
        }

        System.out.println("* indicates days with calendar entries");

        // Display current calendars
        System.out.println("\nYour Calendars:");
        for (Calendar cal : system.getCurrentUser().getCalendars()) {
            String visibility = cal.isPublic() ? "Public" : "Private";
            String owner = cal.getOwner().equals(system.getCurrentUser()) ? "Owned" : "Shared";
            System.out.printf("- %s (%s, %s)\n", cal.getName(), visibility, owner);
        }
    }

    /**
     * Gets all calendar entries for a specific date across all user's calendars.
     * @param date the date to search for
     * @return sorted list of entries for that date
     */
    private List<CalendarEntry> getAllEntriesForDate(String date) {
        List<CalendarEntry> allEntries = new ArrayList<>();

        for (Calendar calendar : system.getCurrentUser().getCalendars()) {
            allEntries.addAll(calendar.getEntriesForDate(date));
        }

        // Sort by start time
        Collections.sort(allEntries, CalendarEntry::compareStartTime);

        return allEntries;
    }

    /**
     * Handles month navigation.
     */
    private void handleNavigateMonth() {
        System.out.println("\n--- Navigate Month ---");
        System.out.println("1. Previous Month");
        System.out.println("2. Next Month");
        System.out.println("3. Jump to Specific Month/Year");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                if (currentMonth == 1) {
                    currentMonth = 12;
                    currentYear--;
                } else {
                    currentMonth--;
                }
                break;
            case 2:
                if (currentMonth == 12) {
                    currentMonth = 1;
                    currentYear++;
                } else {
                    currentMonth++;
                }
                break;
            case 3:
                System.out.print("Enter year: ");
                int year = getIntInput();
                System.out.print("Enter month (1-12): ");
                int month = getIntInput();

                if (year > 0 && month >= 1 && month <= 12) {
                    currentYear = year;
                    currentMonth = month;
                } else {
                    System.out.println("Invalid year or month.");
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Handles viewing details for a specific date.
     */
    private void handleViewDateDetails() {
        System.out.print("Enter date (yyyy-mm-dd): ");
        String date = scanner.nextLine().trim();

        if (!DateUtil.isValidDate(date)) {
            System.out.println("Invalid date format. Use yyyy-mm-dd.");
            return;
        }

        List<CalendarEntry> entries = getAllEntriesForDate(date);

        System.out.println("\n--- Entries for " + date + " ---");
        if (entries.isEmpty()) {
            System.out.println("No entries found for this date.");
        } else {
            for (int i = 0; i < entries.size(); i++) {
                CalendarEntry entry = entries.get(i);
                System.out.printf("%d. [%s] %s (%s - %s)\n",
                        i + 1,
                        entry.getParentCalendar().getName(),
                        entry.getTitle(),
                        entry.getStartTime(),
                        entry.getEndTime());
                if (entry.getDetails() != null && !entry.getDetails().trim().isEmpty()) {
                    System.out.println("   Details: " + entry.getDetails());
                }
            }
        }
    }

    /**
     * Handles adding a new calendar entry.
     */
    private void handleAddEntry() {
        List<Calendar> userCalendars = system.getCurrentUser().getCalendars();

        if (userCalendars.isEmpty()) {
            System.out.println("No calendars available. Please create a calendar first.");
            return;
        }

        // Select calendar
        System.out.println("\n--- Add New Entry ---");
        System.out.println("Select calendar:");
        for (int i = 0; i < userCalendars.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, userCalendars.get(i).getName());
        }
        System.out.print("Choose calendar: ");

        int calIndex = getIntInput() - 1;
        if (calIndex < 0 || calIndex >= userCalendars.size()) {
            System.out.println("Invalid calendar selection.");
            return;
        }

        Calendar selectedCalendar = userCalendars.get(calIndex);

        // Get entry details
        System.out.print("Enter date (yyyy-mm-dd): ");
        String date = scanner.nextLine().trim();
        if (!DateUtil.isValidDate(date)) {
            System.out.println("Invalid date format.");
            return;
        }

        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }

        System.out.print("Enter start time (HH:mm): ");
        String startTime = scanner.nextLine().trim();
        if (!DateUtil.isValidTime(startTime)) {
            System.out.println("Invalid time format. Use HH:mm.");
            return;
        }

        System.out.print("Enter end time (HH:mm): ");
        String endTime = scanner.nextLine().trim();
        if (!DateUtil.isValidTime(endTime)) {
            System.out.println("Invalid time format. Use HH:mm.");
            return;
        }

        System.out.print("Enter details (optional): ");
        String details = scanner.nextLine().trim();

        CalendarEntry newEntry = new CalendarEntry(date, title, startTime, endTime, details, selectedCalendar);
        selectedCalendar.addEntry(newEntry);

        System.out.println("Entry added successfully!");
    }

    /**
     * Handles editing an existing calendar entry.
     */
    private void handleEditEntry() {
        System.out.print("Enter date to edit entries (yyyy-mm-dd): ");
        String date = scanner.nextLine().trim();

        if (!DateUtil.isValidDate(date)) {
            System.out.println("Invalid date format.");
            return;
        }

        List<CalendarEntry> entries = getAllEntriesForDate(date);

        if (entries.isEmpty()) {
            System.out.println("No entries found for this date.");
            return;
        }

        System.out.println("\n--- Edit Entry ---");
        for (int i = 0; i < entries.size(); i++) {
            CalendarEntry entry = entries.get(i);
            System.out.printf("%d. [%s] %s (%s - %s)\n",
                    i + 1,
                    entry.getParentCalendar().getName(),
                    entry.getTitle(),
                    entry.getStartTime(),
                    entry.getEndTime());
        }

        System.out.print("Select entry to edit: ");
        int entryIndex = getIntInput() - 1;

        if (entryIndex < 0 || entryIndex >= entries.size()) {
            System.out.println("Invalid entry selection.");
            return;
        }

        CalendarEntry entry = entries.get(entryIndex);

        System.out.println("\nEditing: " + entry.getTitle());
        System.out.println("Press Enter to keep current value");

        System.out.print("New title [" + entry.getTitle() + "]: ");
        String newTitle = scanner.nextLine().trim();
        if (!newTitle.isEmpty()) {
            entry.setTitle(newTitle);
        }

        System.out.print("New start time [" + entry.getStartTime() + "]: ");
        String newStartTime = scanner.nextLine().trim();
        if (!newStartTime.isEmpty()) {
            if (DateUtil.isValidTime(newStartTime)) {
                entry.setStartTime(newStartTime);
            } else {
                System.out.println("Invalid time format, keeping original.");
            }
        }

        System.out.print("New end time [" + entry.getEndTime() + "]: ");
        String newEndTime = scanner.nextLine().trim();
        if (!newEndTime.isEmpty()) {
            if (DateUtil.isValidTime(newEndTime)) {
                entry.setEndTime(newEndTime);
            } else {
                System.out.println("Invalid time format, keeping original.");
            }
        }

        System.out.print("New details [" + (entry.getDetails() != null ? entry.getDetails() : "None") + "]: ");
        String newDetails = scanner.nextLine().trim();
        if (!newDetails.isEmpty()) {
            entry.setDetails(newDetails);
        }

        System.out.println("Entry updated successfully!");
    }

    /**
     * Handles deleting a calendar entry.
     */
    private void handleDeleteEntry() {
        System.out.print("Enter date to delete entries (yyyy-mm-dd): ");
        String date = scanner.nextLine().trim();

        if (!DateUtil.isValidDate(date)) {
            System.out.println("Invalid date format.");
            return;
        }

        List<CalendarEntry> entries = getAllEntriesForDate(date);

        if (entries.isEmpty()) {
            System.out.println("No entries found for this date.");
            return;
        }

        System.out.println("\n--- Delete Entry ---");
        for (int i = 0; i < entries.size(); i++) {
            CalendarEntry entry = entries.get(i);
            System.out.printf("%d. [%s] %s (%s - %s)\n",
                    i + 1,
                    entry.getParentCalendar().getName(),
                    entry.getTitle(),
                    entry.getStartTime(),
                    entry.getEndTime());
        }

        System.out.print("Select entry to delete: ");
        int entryIndex = getIntInput() - 1;

        if (entryIndex < 0 || entryIndex >= entries.size()) {
            System.out.println("Invalid entry selection.");
            return;
        }

        CalendarEntry entry = entries.get(entryIndex);
        System.out.print("Are you sure you want to delete '" + entry.getTitle() + "'? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            entry.getParentCalendar().removeEntry(entry);
            System.out.println("Entry deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Handles calendar management operations.
     */
    private void handleManageCalendars() {
        System.out.println("\n--- Manage Calendars ---");
        System.out.println("1. Create New Calendar");
        System.out.println("2. Add Public Calendar");
        System.out.println("3. Delete Calendar");
        System.out.println("4. View All Calendars");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                handleCreateCalendar();
                break;
            case 2:
                handleAddPublicCalendar();
                break;
            case 3:
                handleDeleteCalendar();
                break;
            case 4:
                handleViewAllCalendars();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Handles creating a new calendar.
     */
    private void handleCreateCalendar() {
        System.out.print("Enter calendar name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Calendar name cannot be empty.");
            return;
        }

        System.out.print("Make calendar public? (y/n): ");
        String publicChoice = scanner.nextLine().trim().toLowerCase();
        boolean isPublic = publicChoice.equals("y") || publicChoice.equals("yes");

        Calendar newCalendar = system.createCalendar(name, isPublic);
        if (newCalendar != null) {
            String visibility = isPublic ? "public" : "private";
            System.out.println("Calendar '" + name + "' created successfully as " + visibility + "!");
        } else {
            System.out.println("Failed to create calendar.");
        }
    }

    /**
     * Handles adding a public calendar to user's list.
     */
    private void handleAddPublicCalendar() {
        List<Calendar> publicCalendars = system.getPublicCalendars();
        List<Calendar> userCalendars = system.getCurrentUser().getCalendars();

        // Filter out calendars user already has
        List<Calendar> availableCalendars = new ArrayList<>();
        for (Calendar pubCal : publicCalendars) {
            if (!userCalendars.contains(pubCal)) {
                availableCalendars.add(pubCal);
            }
        }

        if (availableCalendars.isEmpty()) {
            System.out.println("No public calendars available to add.");
            return;
        }

        System.out.println("\n--- Available Public Calendars ---");
        for (int i = 0; i < availableCalendars.size(); i++) {
            Calendar cal = availableCalendars.get(i);
            System.out.printf("%d. %s (by %s)\n",
                    i + 1,
                    cal.getName(),
                    cal.getOwner().getUsername());
        }

        System.out.print("Select calendar to add: ");
        int calIndex = getIntInput() - 1;

        if (calIndex < 0 || calIndex >= availableCalendars.size()) {
            System.out.println("Invalid calendar selection.");
            return;
        }

        Calendar selectedCalendar = availableCalendars.get(calIndex);
        if (system.addPublicCalendar(selectedCalendar)) {
            System.out.println("Calendar '" + selectedCalendar.getName() + "' added successfully!");
        } else {
            System.out.println("Failed to add calendar.");
        }
    }

    /**
     * Handles deleting a calendar.
     */
    private void handleDeleteCalendar() {
        List<Calendar> userCalendars = system.getCurrentUser().getCalendars();
        List<Calendar> ownedCalendars = new ArrayList<>();

        // Filter to only calendars owned by current user
        for (Calendar cal : userCalendars) {
            if (cal.getOwner().equals(system.getCurrentUser())) {
                ownedCalendars.add(cal);
            }
        }

        if (ownedCalendars.isEmpty()) {
            System.out.println("You don't own any calendars that can be deleted.");
            return;
        }

        System.out.println("\n--- Delete Calendar ---");
        System.out.println("You can only delete calendars you own:");

        for (int i = 0; i < ownedCalendars.size(); i++) {
            Calendar cal = ownedCalendars.get(i);
            String deletable = cal.getName().equals(system.getCurrentUser().getUsername()) ?
                    " (Cannot delete - default calendar)" : "";
            System.out.printf("%d. %s%s\n", i + 1, cal.getName(), deletable);
        }

        System.out.print("Select calendar to delete: ");
        int calIndex = getIntInput() - 1;

        if (calIndex < 0 || calIndex >= ownedCalendars.size()) {
            System.out.println("Invalid calendar selection.");
            return;
        }

        Calendar selectedCalendar = ownedCalendars.get(calIndex);

        if (selectedCalendar.getName().equals(system.getCurrentUser().getUsername())) {
            System.out.println("Cannot delete your default calendar.");
            return;
        }

        System.out.print("Are you sure you want to delete '" + selectedCalendar.getName() +
                "'? This will delete all entries. (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            if (system.deleteCalendar(selectedCalendar)) {
                System.out.println("Calendar deleted successfully!");
            } else {
                System.out.println("Failed to delete calendar.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Handles viewing all calendars with details.
     */
    private void handleViewAllCalendars() {
        List<Calendar> userCalendars = system.getCurrentUser().getCalendars();

        System.out.println("\n--- Your Calendars ---");
        if (userCalendars.isEmpty()) {
            System.out.println("No calendars found.");
            return;
        }

        for (Calendar cal : userCalendars) {
            String visibility = cal.isPublic() ? "Public" : "Private";
            String ownership = cal.getOwner().equals(system.getCurrentUser()) ? "Owned by you" :
                    "Owned by " + cal.getOwner().getUsername();
            int entryCount = cal.getEntries().size();

            System.out.printf("- %s (%s, %s, %d entries)\n",
                    cal.getName(), visibility, ownership, entryCount);
        }
    }

    /**
     * Handles account-related operations.
     */
    private void handleAccountOptions() {
        System.out.println("\n--- Account Options ---");
        System.out.println("1. Delete Account");
        System.out.println("2. View Account Info");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                handleDeleteAccount();
                break;
            case 2:
                handleViewAccountInfo();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Handles account deletion.
     */
    private void handleDeleteAccount() {
        System.out.println("\n--- Delete Account ---");
        System.out.println("WARNING: This will permanently delete your account and all private calendars!");
        System.out.println("Public calendars will remain available to other users.");
        System.out.print("Are you sure you want to delete your account? (y/n): ");

        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            System.out.print("Enter your password to confirm: ");
            String password = scanner.nextLine().trim();

            if (system.getCurrentUser().getPassword().equals(password)) {
                system.deleteCurrentAccount();
                System.out.println("Account deleted successfully. You have been logged out.");
            } else {
                System.out.println("Incorrect password. Account deletion cancelled.");
            }
        } else {
            System.out.println("Account deletion cancelled.");
        }
    }

    /**
     * Handles viewing account information.
     */
    private void handleViewAccountInfo() {
        Account currentUser = system.getCurrentUser();

        System.out.println("\n--- Account Information ---");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Status: " + (currentUser.isActive() ? "Active" : "Inactive"));
        System.out.println("Total Calendars: " + currentUser.getCalendars().size());

        // Count owned vs shared calendars
        int ownedCount = 0;
        int sharedCount = 0;
        int totalEntries = 0;

        for (Calendar cal : currentUser.getCalendars()) {
            if (cal.getOwner().equals(currentUser)) {
                ownedCount++;
            } else {
                sharedCount++;
            }
            totalEntries += cal.getEntries().size();
        }

        System.out.println("Owned Calendars: " + ownedCount);
        System.out.println("Shared Calendars: " + sharedCount);
        System.out.println("Total Entries: " + totalEntries);
    }

    /**
     * Gets the name of a month from its number.
     * @param month the month number (1-12)
     * @return the month name
     */
    private String getMonthName(int month) {
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    /**
     * Safely gets integer input from user.
     * @return the integer input, or -1 if invalid
     */
    private int getIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}