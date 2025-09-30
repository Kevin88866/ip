package kiki.command;

import kiki.storage.Storage;
import kiki.task.Deadline;
import kiki.task.TaskList;
import kiki.ui.Ui;

/** Adds a {@link Deadline} task with a due time. */
public class AddDeadlineCommand extends Command {
    private final String desc;
    private final String by;

    /**
     * @param desc description of the deadline
     * @param by   due time string (as typed by the user)
     */
    public AddDeadlineCommand(String desc, String by) {
        this.desc = desc;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Deadline task = new Deadline(desc, by);
        tasks.addTask(task, ui, storage);
    }
}
