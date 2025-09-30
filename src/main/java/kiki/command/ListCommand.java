package kiki.command;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.TaskList;
import kiki.ui.Ui;

/** Prints the current task list to the console. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        ui.showLine();
        System.out.println("  Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }
}
