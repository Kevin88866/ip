package kiki.command;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.TaskList;
import kiki.ui.Ui;

/** Command that signals the application to exit. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException { }

    @Override
    public boolean isExit() {
        return true;
    }
}
