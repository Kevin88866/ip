package kiki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import kiki.command.Command;
import kiki.exception.KikiException;
import kiki.parser.Parser;
import kiki.storage.Storage;
import kiki.task.Task;
import kiki.task.TaskList;
import kiki.ui.Ui;

/**
 * Application entry point. Wires together {@link Ui}, {@link Storage},
 * {@link TaskList} and the {@link Parser}, then runs the command loop.
 */
public class Kiki {

    /**
     * Starts the CLI application.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Ui ui = new Ui(new Scanner(System.in));
        Storage storage = new Storage("data", "kiki.txt");
        TaskList tasks;

        // Load any existing tasks from disk
        try {
            ArrayList<Task> loaded = storage.load();
            tasks = new TaskList(loaded);
        } catch (IOException e) {
            ui.showLine();
            System.out.println(" OOPS!!! Your save file is corrupted or unreadable.");
            System.out.println(" Details: " + e.getMessage());
            System.out.println(" Starting with an empty task list.");
            ui.showLine();
            tasks = new TaskList();
        }

        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (KikiException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError(" OOPS!!! Something went wrong. (" + e.getClass().getSimpleName() + ")");
            }
        }

        ui.showBye();
    }
}
