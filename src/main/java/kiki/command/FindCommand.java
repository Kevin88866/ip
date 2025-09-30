package kiki.command;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.Task;
import kiki.task.TaskList;
import kiki.ui.Ui;

/**
 * Searches for tasks whose description contains a given keyword (case-insensitive).
 * <p>Result numbering is 1-based and refers to the match order, not the original indices.</p>
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a search command.
     *
     * @param keyword the substring to search for (case-insensitive)
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        final String q = keyword.toLowerCase();

        ui.showLine();
        System.out.println("  Here are the matching tasks in your list:");

        int shown = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            String desc = t.getDescription();
            if (desc != null && desc.toLowerCase().contains(q)) {
                System.out.println("  " + (shown + 1) + ". " + t);
                shown++;
            }
        }

        if (shown == 0) {
            System.out.println("  (no match)");
        }
        ui.showLine();
    }
}
