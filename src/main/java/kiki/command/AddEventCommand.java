package kiki.command;

import kiki.storage.Storage;
import kiki.task.Event;
import kiki.task.TaskList;
import kiki.ui.Ui;

/** Adds an {@link Event} task with start and end times. */
public class AddEventCommand extends Command {
    private final String desc;
    private final String from;
    private final String to;

    /**
     * @param desc description of the event
     * @param from start time string (after "/from")
     * @param to   end time string (after "/to")
     */
    public AddEventCommand(String desc, String from, String to) {
        this.desc = desc;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Event task = new Event(desc, from, to);
        tasks.addTask(task, ui, storage);
    }
}
