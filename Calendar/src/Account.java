import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user account in the digital calendar system.
 * Each account has credentials and associated calendars.
 */
class Account {
    private String username;
    private String password;
    private boolean active;
    private List<Calendar> calendars;

    /**
     * Creates a new account with username and password.
     * @param username unique identifier for the account
     * @param password authentication credential
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
        this.calendars = new ArrayList<>();

        // Create default private calendar with username
        Calendar defaultCalendar = new Calendar(username, this, false);
        this.calendars.add(defaultCalendar);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    /**
     * Adds a calendar to this account's list.
     * @param calendar the calendar to add
     */
    public void addCalendar(Calendar calendar) {
        if (!calendars.contains(calendar)) {
            calendars.add(calendar);
        }
    }

    /**
     * Removes a calendar from this account's list.
     * @param calendar the calendar to remove
     */
    public void removeCalendar(Calendar calendar) {
        calendars.remove(calendar);
    }

    /**
     * Authenticates the account with provided credentials.
     * @param username provided username
     * @param password provided password
     * @return true if credentials match and account is active
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) &&
                this.password.equals(password) &&
                this.active;
    }
}