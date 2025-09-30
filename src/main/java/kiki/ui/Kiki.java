package kiki.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import kiki.exception.KikiException;
import kiki.storage.Storage;
import kiki.task.Deadline;
import kiki.task.Event;
import kiki.task.Task;
import kiki.task.Todo;


/**
 * The main entry point of the Kiki task manager application.
 * <p>
 * Kiki is a simple CLI-based task manager that allows users to:
 * <ul>
 *   <li>Add tasks: Todo, Deadline, Event</li>
 *   <li>Mark/unmark tasks as done</li>
 *   <li>Delete tasks</li>
 *   <li>View all tasks</li>
 * </ul>
 * Tasks are persisted locally using {@link kiki.storage.Storage}.
 * </p>
 */
public class Kiki {
    private static final String horizontalLine = "____________________________________________________________";
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static final Storage storage = new Storage("data", "kiki.txt");

    /**
     * Starts the Kiki application.
     * <p>
     * Loads previously saved tasks (if available), and enters a loop
     * to continuously process user input until "bye" is typed.
     * </p>
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        messagePrinter(" Hello! I'm Kiki.kiki\n  What can I do for you?");

        //load tasks from kiki.txt
        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println(horizontalLine);
            System.out.println(" OOPS!!! Your save file is corrupted or unreadable.");
            System.out.println(" Details: " + e.getMessage());
            System.out.println(" Starting with an empty task list.");
            System.out.println(horizontalLine);
            tasks = new ArrayList<>();
        }

        String input;
        //the loop will never stop until the input is "bye"
        while (!(input = sc.nextLine()).equals("bye")) {
            try {
                //use handle function to avoid the program crashed because of invalid input
                //and exception can tell user how to input in correct demand
                handle(input);
            } catch (KikiException e) {
                //print the message in KikiException
                messagePrinter(e.getMessage());
            } catch (Exception e) {
                //Exception is used when the exception isn't cover by KikiException
                messagePrinter(" OOPS!!! Something went wrong. (" + e.getClass().getSimpleName() + ")");
            }
        }
        messagePrinter(" Bye. Hope to see you again soon!");
        sc.close();
    }

    /**
     * Handles a single line of user input and executes the corresponding command.
     *
     * @param input the raw user input.
     * @throws KikiException if the command is invalid or missing required arguments.
     */
    private static void handle(String input) throws KikiException {
        input = input.trim();
        if (input.equals("list")) {
            printList();
        } else if (input.startsWith("mark")) {
            int index = parseIndex(input, "mark");
            markTask(index);
        } else if (input.startsWith("unmark")) {
            int index = parseIndex(input, "unmark");
            unmarkTask(index);
        } else if (input.startsWith("todo")) {
            addTodo(input);
        } else if (input.startsWith("deadline")) {
            addDeadline(input);
        } else if (input.startsWith("event")) {
            addEvent(input);
        } else if (input.startsWith("delete")) {
            int index = parseIndex(input, "delete");
            deleteTask(index);
        } else {
            throw new KikiException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses a task index from user input.
     *
     * @param input the full command string.
     * @param cmd the command keyword (e.g. "mark", "delete").
     * @return the zero-based index of the task in the list.
     * @throws KikiException if the index is missing, invalid, or out of range.
     */
    private static int parseIndex(String input, String cmd) throws KikiException {
        String[] parts = input.split("\\s+");

        //length < 2 means the input doesn't have number after mark/unmark.
        if (parts.length < 2) {
            throw new KikiException(" OOPS!!! Please provide a task number for '" + cmd + "'.");
        }

        int n;

        try {
            //parseInt will throw NumberFormatException if the input is not an integer ( like 'a',1.2,'abc')
            n = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new KikiException(" OOPS!!! '" + parts[1] + "' is not a valid task number.");
        }

        //this means the number is invalid
        if (n < 1 || n > tasks.size()) {
            throw new KikiException(" OOPS!!! Task number is out of range. You have " + tasks.size() + " task(S).");
        }

        return n - 1;
    }

    /**
     * Prints a message surrounded by horizontal dividers.
     *
     * @param message the message to display.
     */
    private static void messagePrinter(String message) {
        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }

    /** Prints all tasks in the current list to the console. */
    private static void printList() {
        System.out.println(horizontalLine);
        System.out.println("  Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + tasks.get(i));
        }

        System.out.println(horizontalLine);
    }
    /**
     * Marks a task as not done.
     *
     * @param index index of the task to unmark.
     */
    private static void unmarkTask(int index) {
        tasks.get(index).markNotDone();
        messagePrinter(" OK, I've marked this task as not done yet:\n    " + tasks.get(index));
        saveList(tasks);
    }

    /**
     * Marks a task as done.
     *
     * @param index index of the task to mark.
     */
    private static void markTask(int index) {
        tasks.get(index).markDone();
        messagePrinter(" Nice! I've marked this task as done:\n    " + tasks.get(index));
        saveList(tasks);
    }

    /**
     * Prints confirmation when a task is added.
     *
     * @param task the task that was added.
     */
    private static void printTask(Task task) {
        messagePrinter(" Got it. I've added this task:\n    "
                + task + "\n  Now you have "
                + tasks.size() + " tasks in the list.");
    }

    /**
     * Saves all tasks to disk using {@link Storage}.
     *
     * @param tasks list of tasks to save.
     */
    private static void saveList(ArrayList<Task> tasks) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            System.out.println(horizontalLine);
            System.out.println(" OOPS!!! Failed to save tasks: " + e.getMessage());
            System.out.println(horizontalLine);
        }
    }

    /**
     * Adds a new Todo task.
     *
     * @param input user input containing the todo description.
     * @throws KikiException if the description is empty.
     */
    private static void addTodo(String input) throws KikiException {
        String work;

        if (input.length() > 4) {
            work = input.substring(5);
        } else {
            work = "";
        }

        //when the length<=4, the work is empty and throw exception
        if (work.isEmpty()) {
            throw new KikiException(" OOPS!!! The description of a todo cannot be empty.");
        }

        Todo task = new Todo(work);
        tasks.add(task);
        printTask(task);
        saveList(tasks);
    }

    /**
     * Adds a new Deadline task.
     *
     * @param input user input containing description and deadline in the format:
     *              "deadline &lt;description&gt; /by &lt;time&gt;"
     * @throws KikiException if description or time is missing.
     */
    private static void addDeadline(String input) throws KikiException {
        int index = input.indexOf("/by");
        String work = "";
        String by = "";

        //when index = -1, the app can't find "/by", throw exception
        if (index == -1) {
            throw new KikiException(" OOPS!!! Deadline requires '/by <time>'. "
                    + "Example: deadline return book /by Sunday");
        } else {
            String[] parts = input.substring(8).trim().split("/by", 2);

            if (parts.length == 2) {
                work = parts[0].trim();
                by = parts[1].trim();
            }
        }

        if (work.isEmpty()) {
            throw new KikiException(" OOPS!!! The description of a deadline cannot be empty.");
        }

        if (by.isEmpty()) {
            throw new KikiException(" OOPS!!! The time of a deadline cannot be empty. Use '/by <time>'.");
        }

        Deadline task = new Deadline(work, by);
        tasks.add(task);
        printTask(task);
        saveList(tasks);
    }

    /**
     * Adds a new Event task.
     *
     * @param input user input containing description, start, and end times in the format:
     *              "event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;"
     * @throws KikiException if description, start, or end time is missing.
     */
    private static void addEvent(String input) throws KikiException {
        String afterKeyword;

        if (input.length() > 5) {
            afterKeyword = input.substring(6);
        } else {
            afterKeyword = "";
        }

        String[] pFrom = afterKeyword.split("/from", 2);

        if (pFrom.length < 2) {
            throw new KikiException(" OOPS!!! Event requires '/from <start>' and '/to <end>'.");
        }

        String work = pFrom[0].trim();
        Event task = parseEvent(pFrom, work);
        tasks.add(task);
        printTask(task);
        saveList(tasks);
    }


    private static Event parseEvent(String[] pFrom, String work) throws KikiException {
        String[] pTo = pFrom[1].split("/to", 2);

        if (pTo.length < 2) {
            throw new KikiException(" OOPS!!! Event requires '/to <end>'.");
        }

        String from = pTo[0].trim();
        String to = pTo[1].trim();

        if (work.isEmpty()) {
            throw new KikiException(" OOPS!!! The description of an event cannot be empty.");
        }

        if (from.isEmpty()) {
            throw new KikiException(" OOPS!!! The start time of an event cannot be empty. Use '/from <start>'.");
        }

        if (to.isEmpty()) {
            throw new KikiException(" OOPS!!! The end time of an event cannot be empty. Use '/to <end>'.");
        }

        return new Event(work, from, to);
    }

    /**
     * Deletes a task from the list.
     *
     * @param index index of the task to delete.
     */
    private static void deleteTask(int index) {
        messagePrinter(" Noted. I've removed this task:\n    "
                + tasks.get(index) + System.lineSeparator()
                + "  Now you have " + (tasks.size() - 1) + " tasks in the list.");
        tasks.remove(index);
        saveList(tasks);
    }
}
