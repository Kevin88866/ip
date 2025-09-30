package kiki.task;

/**
 * Represents a task with a specific deadline.
 * <p>
 * Format: {@code deadline <description> /by <time>}
 * </p>
 */
public class Deadline extends Task {
    private final String deadline;

    /**
     * Constructs a Deadline task.
     *
     * @param task description of the deadline task.
     * @param by   the deadline time.
     */
    public Deadline(String task, String by) {
        super(task);
        deadline = by;
    }

    /**
     * Returns a user-friendly string representation of this deadline.
     *
     * @return string in the format "[D][ ] description (by: deadline)".
     */
    @Override
    public String toString() {
        return "[D]" + getStatusIcon() + " " + task + " (by: " + deadline + ")";
    }

    /**
     * Returns a string formatted for saving to disk.
     *
     * @return save string in the format "D | doneFlag | description | by".
     */
    @Override
    public String toSaveString() {
        return String.join(" | ", "D", isDone ? "1" : "0", task, deadline);
    }
}
