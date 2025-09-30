package kiki.task;

/**
 * Represents an abstract task in the task manager.
 * <p>
 * This is the base class for all task types, including:
 * <ul>
 *     <li>{@link Todo}</li>
 *     <li>{@link Deadline}</li>
 *     <li>{@link Event}</li>
 * </ul>
 * Each task has a description and a completion status.
 * </p>
 */
public abstract class Task {
    protected String task;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     *
     * @param task the description of the task.
     */
    public Task(String task) {
        this.task = task;
        this.isDone = false;
    }

    public void markDone() {
        isDone = true;
    }

    public void markNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon for the task.
     *
     * @return "X" if done, otherwise a space.
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /** @return the textual description of this task. */
    public String getDescription() {
        return task;
    }

    /** Concrete toString used by subclasses via super.toString(). */
    public String toString() {
        return getStatusIcon() + " " + task;
    }

    public abstract String toSaveString();
}
