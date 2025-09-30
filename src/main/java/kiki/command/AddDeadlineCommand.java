package kiki.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.Deadline;
import kiki.task.TaskList;
import kiki.time.Dates;
import kiki.ui.Ui;


/** Adds a {@link Deadline} task with a due time. */
public class AddDeadlineCommand extends Command {
    private final String desc;
    private final String byStr;

    /**
     * @param desc description of the deadline
     * @param by   due time string (as typed by the user)
     */
    public AddDeadlineCommand(String desc, String by) {
        this.desc = desc;
        this.byStr = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KikiException {
        LocalDate by;
        try {
            by = LocalDate.parse(byStr, Dates.INPUT);
        } catch (DateTimeParseException e) {
            throw new KikiException(" OOPS!!! Please use date format yyyy-mm-dd (e.g., 2019-10-15).");
        }
        Deadline task = new Deadline(desc, by);
        tasks.addTask(task, ui, storage);
    }
}
