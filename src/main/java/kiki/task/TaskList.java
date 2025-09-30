package kiki.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kiki.storage.Storage;
import kiki.ui.Ui;

/**
 * Mutable collection of {@link Task} objects with common operations used by the app.
 * <p>Acts as the in-memory model for the task list.</p>
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from an existing list (defensively copied).
     *
     * @param existing tasks to initialize from; may be {@code null}
     */
    public TaskList(ArrayList<Task> existing) {
        this.tasks = (existing == null) ? new ArrayList<>() : new ArrayList<>(existing);
    }

    /**
     * @return number of tasks currently stored
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at an index.
     *
     * @param index zero-based index
     * @return the task at {@code index}
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param t task to add
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes and returns the task at an index.
     *
     * @param index zero-based index
     * @return the removed task
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks a task as done.
     *
     * @param index zero-based index
     * @return the updated task
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task mark(int index) {
        Task t = tasks.get(index);
        t.markDone();
        return t;
    }

    /**
     * Marks a task as not done.
     *
     * @param index zero-based index
     * @return the updated task
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task unmark(int index) {
        Task t = tasks.get(index);
        t.markNotDone();
        return t;
    }

    /**
     * Returns an unmodifiable view of the tasks.
     *
     * @return read-only list view of tasks
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns a fresh {@link ArrayList} copy of the tasks.
     *
     * @return copy of tasks
     */
    public ArrayList<Task> asArrayList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Adds a new {@link Todo} task to the given task list, displays a confirmation message,
     * and attempts to save the updated task list to storage.
     * <p>
     * If saving fails due to an {@link IOException}, an error message will be printed
     * and the failure will be communicated to the user through the {@link Ui}.
     *
     * @param task     the {@link Todo} task to add
     * @param ui       the user interface handler for printing messages and lines
     * @param storage  the storage handler used to persist the updated task list
     */
    public void addTask(Task task, Ui ui, Storage storage) {
        tasks.add(task);
        ui.messagePrinter(" Got it. I've added this task:\n    "
                + task + "\n  Now you have "
                + tasks.size() + " tasks in the list.");
        try {
            storage.save(this.asList());
        } catch (IOException e) {
            ui.showLine();
            System.out.println(" OOPS!!! Failed to save tasks: " + e.getMessage());
            ui.showLine();
        }
    }

}
