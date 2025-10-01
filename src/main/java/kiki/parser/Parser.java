package kiki.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kiki.command.AddDeadlineCommand;
import kiki.command.AddEventCommand;
import kiki.command.AddTodoCommand;
import kiki.command.Command;
import kiki.command.DeleteCommand;
import kiki.command.ExitCommand;
import kiki.command.FindCommand;
import kiki.command.ListCommand;
import kiki.command.MarkCommand;
import kiki.command.OnDateCommand;
import kiki.command.UnmarkCommand;
import kiki.exception.KikiException;
import kiki.time.Dates;

/**
 * Parses raw user input strings into concrete {@link Command} objects.
 * <p>Validation that depends on the current task list (e.g. index range) is
 * deferred to the command's {@code execute} method to keep parsing decoupled
 * from the model.</p>
 */
public class Parser {

    /**
     * Parses a raw command line into a {@link Command}.
     *
     * @param input raw line from the user
     * @return a concrete command to be executed
     * @throws KikiException if the input is syntactically invalid or unknown
     */
    public static Command parse(String input) throws KikiException {
        String s = (input == null) ? "" : input.trim();
        if (s.equals("bye")) {
            return new ExitCommand();
        }
        if (s.equals("list")) {
            return new ListCommand();
        }
        if (s.startsWith("mark")) {
            int idx0 = parseIndex(s, "mark");
            return new MarkCommand(idx0);
        }
        if (s.startsWith("unmark")) {
            int idx0 = parseIndex(s, "unmark");
            return new UnmarkCommand(idx0);
        }
        if (s.startsWith("delete")) {
            int idx0 = parseIndex(s, "delete");
            return new DeleteCommand(idx0);
        }
        if (s.startsWith("todo")) {
            String desc = (s.length() > 4) ? s.substring(5) : "";
            if (desc.trim().isEmpty()) {
                throw new KikiException(" OOPS!!! The description of a todo cannot be empty.");
            }
            return new AddTodoCommand(desc.trim());
        }
        if (s.startsWith("deadline")) {
            int idx = s.indexOf("/by");
            if (idx == -1) {
                throw new KikiException(" OOPS!!! Deadline requires '/by <time>'. "
                        + "Example: deadline return book /by 2025-9-30");
            }
            String[] parts = s.substring(8).trim().split("/by", 2);
            String work = (parts.length >= 1) ? parts[0].trim() : "";
            String by = (parts.length == 2) ? parts[1].trim() : "";
            if (work.isEmpty()) {
                throw new KikiException(" OOPS!!! The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new KikiException(" OOPS!!! The time of a deadline cannot be empty. Use '/by <time>'.");
            }
            return new AddDeadlineCommand(work, by);
        }
        if (s.startsWith("event")) {
            String after = (s.length() > 5) ? s.substring(6) : "";
            String[] pFrom = after.split("/from", 2);
            if (pFrom.length < 2) {
                throw new KikiException(" OOPS!!! Event requires '/from <start>' and '/to <end>'.");
            }
            String work = pFrom[0].trim();
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
            return new AddEventCommand(work, from, to);
        }
        if (s.startsWith("on")) {
            String[] parts = s.split("\\s+");
            if (parts.length < 2) {
                throw new KikiException(" OOPS!!! Please provide a date. Usage: on yyyy-mm-dd");
            }
            try {
                LocalDate date = LocalDate.parse(parts[1], Dates.INPUT);
                return new OnDateCommand(date);
            } catch (DateTimeParseException ex) {
                throw new KikiException(" OOPS!!! Invalid date. Please use yyyy-mm-dd.");
            }
        }
        if (s.startsWith("find")) {
            String q = (s.length() > 4) ? s.substring(5).trim() : "";
            if (q.isEmpty()) {
                throw new KikiException(" OOPS!!! Please provide a keyword. Usage: find <keyword>");
            }
            return new FindCommand(q);
        }

        throw new KikiException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Parses a 1-based index from commands like {@code mark 2}, returning zero-based.
     * <p>Logic mirrors your original {@code parseIndex} (range checking is
     * intentionally deferred to the command execution phase).</p>
     *
     * @param input full command line
     * @param cmd   command word (for error messages)
     * @return zero-based index
     * @throws KikiException if the number is missing or not an integer
     */
    private static int parseIndex(String input, String cmd) throws KikiException {
        int n = inputSplitter(input, cmd);
        return n - 1; // zero-based
    }

    /**
     * Splits a user input string into its command and arguments, and validates that a
     * numeric task index is provided as the second token.
     * <p>
     * This method ensures commands that operate on a specific task (e.g., {@code mark}, {@code unmark}, {@code delete})
     * receive a valid task number. If the input does not contain a second token or if the token
     * is not a valid integer, a {@link KikiException} is thrown with a descriptive message.
     *
     * @param input the raw user input string to parse (e.g., "mark 2")
     * @param cmd   the command keyword being processed, used in error messages
     * @throws KikiException if the input lacks a task number or the task number is not numeric
     */
    public static int inputSplitter(String input, String cmd) throws KikiException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            throw new KikiException(" OOPS!!! Please provide a task number for '" + cmd + "'.");
        }
        int n;
        try {
            n = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new KikiException(" OOPS!!! '" + parts[1] + "' is not a valid task number.");
        }
        return n;
    }
}
