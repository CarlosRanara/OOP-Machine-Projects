/**
 * Main entry point for the Digital Calendar Application.
 * Initializes the system and starts the user interface.
 */
public class Main {

    /**
     * Main method to start the application.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize the calendar system
            CalendarSystem system = new CalendarSystem();

            // Initialize and start the user interface
            CalendarView view = new CalendarView(system);
            view.start();

        } catch (Exception e) {
            System.err.println("An error occurred while running the application:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}