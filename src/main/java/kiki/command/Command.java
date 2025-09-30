package kiki.command;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.TaskList;
import kiki.ui.Ui;

/**
 * Base type for all user commands parsed from input.
 * <p>Each command encapsulates its behavior in {@link #execute} and can signal
 * termination by overriding {@link #isExit()}.</p>
 */
public abstract class Command {
    /**
     * Performs the command logic.
     *
     * @param tasks   current task list model
     * @param ui      UI for printing messages
     * @param storage persistent storage for saving changes
     * @throws KikiException if user input is invalid or execution fails
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException;

    /**
     * Indicates whether the app should terminate after executing this command.
     *
     * @return {@code true} if this is an exit command; otherwise {@code false}
     */
    public boolean isExit() {
        return false;
    }
}
