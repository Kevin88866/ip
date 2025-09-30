package kiki.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kiki.exception.KikiException;
import kiki.time.Dates;
/**
 * Represents an Event task with a start and end time.
 * <p>
 * Format: {@code event <description> /from <start> /to <end>}
 * </p>
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Constructs an Event task.
     *
     * @param task description of the event.
     * @param from start time of the event.
     * @param to end time of the event.
     */
    public Event(String task, LocalDate from, LocalDate to) throws KikiException {
        super(task);
        if (to.isBefore(from)) {
            throw new KikiException(" OOPS!!! Event end date cannot be before start date.");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Convenience constructor for storage/backward compatibility.
     * Both dates must be in {@code yyyy-MM-dd}.
     */
    public Event(String description, String from, String to) throws KikiException {
        super(description);
        try {
            LocalDate f = LocalDate.parse(from, Dates.INPUT);
            LocalDate t = LocalDate.parse(to, Dates.INPUT);
            if (t.isBefore(f)) {
                throw new KikiException(" OOPS!!! Event end date cannot be before start date.");
            }
            this.from = f;
            this.to = t;
        } catch (DateTimeParseException ex) {
            throw new KikiException(" OOPS!!! Invalid date for event. Please use yyyy-mm-dd.");
        }
    }

    /** @return start date. */
    public LocalDate getFrom() {
        return from;
    }

    /** @return end date. */
    public LocalDate getTo() {
        return to;
    }

    /** @return ISO strings for storage. */
    public String getFromIso() {
        return from.format(Dates.INPUT);
    }
    public String getToIso() {
        return to.format(Dates.INPUT);
    }

    /**
     * Returns a user-friendly string representation of this event.
     *
     * @return string in the format "[E][ ] description (from: start to: end)".
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + Dates.pretty(from)
                + " to: " + Dates.pretty(to) + ")";
    }

    @Override
    public String toSaveString() {
        // Format: E | 0 | description | yyyy-MM-dd | yyyy-MM-dd
        return "E | " + (isDone ? "1" : "0") + " | " + task
                + " | " + getFromIso() + " | " + getToIso();
    }
}
