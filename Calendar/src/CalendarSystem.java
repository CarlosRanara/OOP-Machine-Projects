import java.util.ArrayList;
import java.util.List;

/**
 * Main system class that manages accounts and calendars.
 * Handles user authentication and system-wide operations.
 */
public class CalendarSystem {
    private List<Account> accounts;
    private List<Calendar> publicCalendars;
    private Account currentUser;

    /**
     * Initializes the calendar system.
     */
    public CalendarSystem() {
        this.accounts = new ArrayList<>();
        this.publicCalendars = new ArrayList<>();
        this.currentUser = null;
    }

    /**
     * Creates a new account with unique username.
     * @param username the desired username
     * @param password the password for the account
     * @return true if account created successfully, false if username exists
     */
    public boolean createAccount(String username, String password) {
        // Check if username already exists
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                return false;
            }
        }

        Account newAccount = new Account(username, password);
        accounts.add(newAccount);
        return true;
    }

    /**
     * Authenticates a user and sets them as current user.
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return true if login successful, false otherwise
     */
    public boolean login(String username, String password) {
        for (Account account : accounts) {
            if (account.authenticate(username, password)) {
                currentUser = account;
                return true;
            }
        }
        return false;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Deletes the current user's account by setting it inactive.
     * Removes private calendars but keeps public ones.
     */
    public void deleteCurrentAccount() {
        if (currentUser != null) {
            currentUser.setActive(false);

            // Remove private calendars owned by this account
            List<Calendar> toRemove = new ArrayList<>();
            for (Calendar cal : currentUser.getCalendars()) {
                if (cal.getOwner().equals(currentUser) && !cal.isPublic()) {
                    toRemove.add(cal);
                    // Remove from other accounts that added this calendar
                    removeCalendarFromAllAccounts(cal);
                }
            }

            for (Calendar cal : toRemove) {
                currentUser.removeCalendar(cal);
            }

            currentUser = null;
        }
    }

    /**
     * Removes a calendar from all accounts that have added it.
     * @param calendar the calendar to remove
     */
    private void removeCalendarFromAllAccounts(Calendar calendar) {
        for (Account account : accounts) {
            account.removeCalendar(calendar);
        }
        publicCalendars.remove(calendar);
    }

    /**
     * Creates a new calendar for the current user.
     * @param name the name of the calendar
     * @param isPublic whether the calendar should be public
     * @return the created calendar, or null if no current user
     */
    public Calendar createCalendar(String name, boolean isPublic) {
        if (currentUser == null) {
            return null;
        }

        Calendar newCalendar = new Calendar(name, currentUser, isPublic);
        currentUser.addCalendar(newCalendar);

        if (isPublic) {
            publicCalendars.add(newCalendar);
        }

        return newCalendar;
    }

    /**
     * Gets all public calendars available for adding.
     * @return list of public calendars
     */
    public List<Calendar> getPublicCalendars() {
        return new ArrayList<>(publicCalendars);
    }

    /**
     * Adds a public calendar to the current user's calendar list.
     * @param calendar the public calendar to add
     * @return true if added successfully, false otherwise
     */
    public boolean addPublicCalendar(Calendar calendar) {
        if (currentUser == null || !calendar.isPublic()) {
            return false;
        }

        currentUser.addCalendar(calendar);
        return true;
    }

    /**
     * Deletes a calendar owned by the current user.
     * @param calendar the calendar to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteCalendar(Calendar calendar) {
        if (currentUser == null || !calendar.getOwner().equals(currentUser)) {
            return false;
        }

        // Cannot delete the default calendar (named after username)
        if (calendar.getName().equals(currentUser.getUsername())) {
            return false;
        }

        // Remove from all accounts and public list
        removeCalendarFromAllAccounts(calendar);
        return true;
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}