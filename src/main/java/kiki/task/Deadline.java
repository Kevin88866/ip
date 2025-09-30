package kiki.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kiki.exception.KikiException;
import kiki.time.Dates;

/**
 * Represents a task with a specific deadline.
 * <p>
 * Format: {@code deadline <description> /by <time>}
 * </p>
 */
public class Deadline extends Task {
    private final LocalDate deadline;

    /**
     * Constructs a Deadline task.
     *
     * @param task description of the deadline task.
     * @param by   the deadline time.
     */
    public Deadline(String task, LocalDate by) {
        super(task);
        deadline = by;
    }

    /**
     * Convenience constructor kept for storage/backward compatibility.
     * The {@code by} string must be in {@code yyyy-MM-dd} format.
     *
     * @param description task description
     * @param by ISO date string (yyyy-MM-dd)
     * @throws KikiException if the date cannot be parsed
     */
    public Deadline(String description, String by) throws KikiException {
        super(description);
        try {
            this.deadline = LocalDate.parse(by, Dates.INPUT);
        } catch (DateTimeParseException ex) {
            throw new KikiException(" OOPS!!! Invalid date for deadline. Please use yyyy-mm-dd.");
        }
    }

    /** @return due date as {@link LocalDate}. */
    public LocalDate getBy() {
        return deadline;
    }

    /**
     * Returns the ISO representation suitable for storage (yyyy-MM-dd).
     *
     * @return ISO string of the date
     */
    public String getByIso() {
        return deadline.format(Dates.INPUT);
    }

    /**
     * Returns a user-friendly string representation of this deadline.
     *
     * @return string in the format "[D][ ] description (by: deadline)".
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Dates.pretty(deadline) + ")";
    }

    @Override
    public String toSaveString() {
        // Format: D | 1 | description | yyyy-MM-dd
        return "D | " + (isDone ? "1" : "0") + " | " + task + " | " + getByIso();
    }

}
