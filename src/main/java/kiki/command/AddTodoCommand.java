package kiki.command;

import kiki.storage.Storage;
import kiki.task.TaskList;
import kiki.task.Todo;
import kiki.ui.Ui;

/** Adds a {@link Todo} task. */
public class AddTodoCommand extends Command {
    private final String desc;

    /**
     * @param desc todo description (non-empty)
     */
    public AddTodoCommand(String desc) {
        this.desc = desc;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Todo task = new Todo(desc);
        tasks.addTask(task, ui, storage);
    }
}
