import java.util.ArrayList;
import java.util.List;

/**
 * Represents a calendar that can contain multiple calendar entries.
 * Calendars can be either private or public.
 */
class Calendar {
    private String name;
    private Account owner;
    private boolean isPublic;
    private List<CalendarEntry> entries;

    /**
     * Creates a new calendar.
     * @param name the name of the calendar
     * @param owner the account that owns this calendar
     * @param isPublic whether the calendar is public or private
     */
    public Calendar(String name, Account owner, boolean isPublic) {
        this.name = name;
        this.owner = owner;
        this.isPublic = isPublic;
        this.entries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Account getOwner() {
        return owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<CalendarEntry> getEntries() {
        return entries;
    }

    /**
     * Adds an entry to this calendar.
     * @param entry the calendar entry to add
     */
    public void addEntry(CalendarEntry entry) {
        entries.add(entry);
    }

    /**
     * Removes an entry from this calendar.
     * @param entry the calendar entry to remove
     */
    public void removeEntry(CalendarEntry entry) {
        entries.remove(entry);
    }

    /**
     * Gets all entries for a specific date.
     * @param date the date to search for (format: yyyy-mm-dd)
     * @return list of entries on that date
     */
    public List<CalendarEntry> getEntriesForDate(String date) {
        List<CalendarEntry> dateEntries = new ArrayList<>();
        for (CalendarEntry entry : entries) {
            if (entry.getDate().equals(date)) {
                dateEntries.add(entry);
            }
        }
        return dateEntries;
    }
}