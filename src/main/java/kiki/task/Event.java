package kiki.task;

/**
 * Represents an Event task with a start and end time.
 * <p>
 * Format: {@code event <description> /from <start> /to <end>}
 * </p>
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Constructs an Event task.
     *
     * @param task description of the event.
     * @param from start time of the event.
     * @param to end time of the event.
     */
    public Event(String task, String from, String to) {
        super(task);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a user-friendly string representation of this event.
     *
     * @return string in the format "[E][ ] description (from: start to: end)".
     */
    @Override
    public String toString() {
        return "[E]" + getStatusIcon() + " " + task + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns a string formatted for saving to disk.
     *
     * @return save string in the format "E | doneFlag | description | from | to".
     */
    @Override
    public String toSaveString() {
        return String.join(" | ", "E", isDone ? "1" : "0", task, from, to);
    }
}
