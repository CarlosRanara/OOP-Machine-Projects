/**
 * Represents a single entry in a calendar with date, time, and details.
 */
class CalendarEntry {
    private String date;
    private String title;
    private String startTime;
    private String endTime;
    private String details;
    private Calendar parentCalendar;

    /**
     * Creates a new calendar entry.
     * @param date the date of the entry (yyyy-mm-dd format)
     * @param title the title of the entry
     * @param startTime the start time (HH:mm format)
     * @param endTime the end time (HH:mm format)
     * @param details additional details about the entry
     * @param parentCalendar the calendar this entry belongs to
     */
    public CalendarEntry(String date, String title, String startTime,
                         String endTime, String details, Calendar parentCalendar) {
        this.date = date;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
        this.parentCalendar = parentCalendar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Calendar getParentCalendar() {
        return parentCalendar;
    }

    /**
     * Compares start times for sorting entries.
     * @param other the other entry to compare with
     * @return negative if this entry is earlier, positive if later, 0 if same
     */
    public int compareStartTime(CalendarEntry other) {
        return this.startTime.compareTo(other.startTime);
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s (%s to %s)",
                date, title, details != null ? details : "No details",
                startTime, endTime);
    }
}