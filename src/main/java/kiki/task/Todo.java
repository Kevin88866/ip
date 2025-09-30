package kiki.task;

/**
 * Represents a simple todo task without deadline or time range.
 * <p>
 * Format: {@code todo <description>}
 * </p>
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task.
     *
     * @param task description of the todo task.
     */
    public Todo(String task) {
        super(task);
    }

    /**
     * Returns a user-friendly string representation of this todo.
     *
     * @return string in the format "[T][ ] description".
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toSaveString() {
        return String.join(" | ", "T", isDone ? "1" : "0", task);
    }
}
