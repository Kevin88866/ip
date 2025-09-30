package kiki.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.Event;
import kiki.task.TaskList;
import kiki.time.Dates;
import kiki.ui.Ui;

/** Adds an {@link Event} task with start and end times. */
public class AddEventCommand extends Command {
    private final String desc;
    private final String fromStr;
    private final String toStr;

    /**
     * @param desc description of the event
     * @param from start time string (after "/from")
     * @param to   end time string (after "/to")
     */
    public AddEventCommand(String desc, String from, String to) {
        this.desc = desc;
        this.fromStr = from;
        this.toStr = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        LocalDate from;
        LocalDate to;
        try {
            from = LocalDate.parse(fromStr, Dates.INPUT);
            to = LocalDate.parse(toStr, Dates.INPUT);
        } catch (DateTimeParseException e) {
            throw new KikiException(" OOPS!!! Please use date format yyyy-mm-dd for both /from and /to.");
        }
        Event task = new Event(desc, from, to);
        tasks.addTask(task, ui, storage);
    }
}
