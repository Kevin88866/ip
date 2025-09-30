package kiki.ui;

import java.util.Scanner;

/**
 * Handles all user interactions (I/O) for the Kiki CLI application.
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Display welcome/exit and divider lines</li>
 * <li>Read a line of command input from {@code System.in}</li>
 * <li>Render task lists and confirmation/error messages</li>
 * </ul>
 * This class contains no business logic.
 * </p>
 */
public class Ui {
    private static final String horizontalLine = "____________________________________________________________";
    private final Scanner scanner;

    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prints a message surrounded by horizontal dividers.
     *
     * @param message the message to display.
     */
    public void messagePrinter(String message) {
        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
        messagePrinter(" Hello! I'm Kiki.kiki\n  What can I do for you?");
    }

    public void showBye() {
        messagePrinter(" Bye. Hope to see you again soon!");
    }

    public void showError(String msg) {
        messagePrinter(msg);
    }

    public void showLine() {
        System.out.println(horizontalLine);
    }
}
