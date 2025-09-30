package kiki.command;

import java.time.LocalDate;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.Deadline;
import kiki.task.Event;
import kiki.task.Task;
import kiki.task.TaskList;
import kiki.time.Dates;
import kiki.ui.Ui;

/**
 * Prints all deadlines/events that occur on a specific date.
 * <ul>
 *   <li>Deadline matches if {@code by == date}.</li>
 *   <li>Event matches if {@code from <= date <= to}.</li>
 *   <li>Todos are ignored.</li>
 * </ul>
 */
public class OnDateCommand extends Command {
    private final LocalDate date;

    /**
     * @param date target calendar date
     */
    public OnDateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        ui.showLine();
        System.out.println("  Tasks on " + Dates.pretty(date) + ":");

        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            boolean hit = false;

            if (t instanceof Deadline) {
                hit = ((Deadline) t).getBy().equals(date);
            } else if (t instanceof Event) {
                Event e = (Event) t;
                hit = !date.isBefore(e.getFrom()) && !date.isAfter(e.getTo());
            }

            if (hit) {
                System.out.println("  " + (i + 1) + ". " + t);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("  (none)");
        }
        ui.showLine();
    }
}
