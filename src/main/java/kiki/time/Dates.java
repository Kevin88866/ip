package kiki.time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Date parsing/formatting helpers used across the app.
 * <p>Input format is ISO {@code yyyy-MM-dd}, output format is {@code MMM d yyyy}
 * (e.g., {@code Oct 15 2019}).</p>
 */
public final class Dates {
    /** Formatter for user input and storage (ISO). */
    public static final DateTimeFormatter INPUT = DateTimeFormatter.ISO_LOCAL_DATE;

    /** Formatter for human-friendly printing (e.g., "Oct 15 2019"). */
    public static final DateTimeFormatter OUTPUT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private Dates() { }

    /**
     * Formats a date using the human-friendly output pattern.
     *
     * @param d the date to format
     * @return formatted string such as "Oct 15 2019"
     */
    public static String pretty(LocalDate d) {
        return d.format(OUTPUT);
    }
}
