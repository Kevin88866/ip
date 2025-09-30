package kiki.command;

import java.io.IOException;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.Task;
import kiki.task.TaskList;
import kiki.ui.Ui;

/** Marks a specific task as done. */
public class MarkCommand extends Command {
    private final int index0;

    /**
     * @param index0 zero-based index of the task to mark
     */
    public MarkCommand(int index0) {
        this.index0 = index0;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        if (index0 < 0 || index0 >= tasks.size()) {
            throw new KikiException(" OOPS!!! Task number is out of range. You have " + tasks.size() + " task(S).");
        }
        Task t = tasks.mark(index0);
        ui.messagePrinter(" Nice! I've marked this task as done:\n    " + t);
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            ui.showLine();
            System.out.println(" OOPS!!! Failed to save tasks: " + e.getMessage());
            ui.showLine();
        }
    }
}
