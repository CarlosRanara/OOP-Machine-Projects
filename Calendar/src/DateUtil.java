
// File: DateUtil.java
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date operations and calendar display.
 */
public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Gets the current date in yyyy-MM-dd format.
     * @return current date string
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * Gets the current year and month.
     * @return current YearMonth object
     */
    public static YearMonth getCurrentYearMonth() {
        return YearMonth.now();
    }

    /**
     * Formats a date string for display.
     * @param year the year
     * @param month the month (1-12)
     * @param day the day
     * @return formatted date string
     */
    public static String formatDate(int year, int month, int day) {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Gets the number of days in a given month and year.
     * @param year the year
     * @param month the month (1-12)
     * @return number of days in the month
     */
    public static int getDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    /**
     * Gets the day of week for the first day of the month (1=Monday, 7=Sunday).
     * @param year the year
     * @param month the month (1-12)
     * @return day of week (1-7)
     */
    public static int getFirstDayOfWeek(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        return firstDay.getDayOfWeek().getValue();
    }

    /**
     * Validates if a date string is in correct format and valid.
     * @param dateStr the date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if a time string is in correct HH:mm format.
     * @param timeStr the time string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidTime(String timeStr) {
        if (timeStr == null || timeStr.length() != 5 || timeStr.charAt(2) != ':') {
            return false;
        }

        try {
            int hour = Integer.parseInt(timeStr.substring(0, 2));
            int minute = Integer.parseInt(timeStr.substring(3, 5));
            return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}