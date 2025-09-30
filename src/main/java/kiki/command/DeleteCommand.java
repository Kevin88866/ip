package kiki.command;

import java.io.IOException;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.TaskList;
import kiki.ui.Ui;

/** Deletes a task at a given index. */
public class DeleteCommand extends Command {
    private final int index0;

    /**
     * @param index0 zero-based index of the task to delete
     */
    public DeleteCommand(int index0) {
        this.index0 = index0;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        if (index0 < 0 || index0 >= tasks.size()) {
            throw new KikiException(" OOPS!!! Task number is out of range. You have " + tasks.size() + " task(S).");
        }
        var removed = tasks.get(index0);
        ui.messagePrinter(" Noted. I've removed this task:\n    "
                + removed + System.lineSeparator()
                + "  Now you have " + (tasks.size() - 1) + " tasks in the list.");
        tasks.delete(index0);
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            ui.showLine();
            System.out.println(" OOPS!!! Failed to save tasks: " + e.getMessage());
            ui.showLine();
        }
    }
}
