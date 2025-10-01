package kiki;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import kiki.command.Command;
import kiki.exception.KikiException;
import kiki.parser.Parser;
import kiki.storage.Storage;
import kiki.task.TaskList;
import kiki.ui.Ui;

public class KikiTest {
    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;

    public KikiTest() {
        String dir = "data-test-" + System.nanoTime();
        this.storage = new Storage(dir, "kiki-test.txt");
        this.tasks = new TaskList();
        this.ui = new Ui(new Scanner(new ByteArrayInputStream(new byte[0])));
    }

    private String exec(String raw) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true));
        try {
            Command c = Parser.parse(raw);
            c.execute(tasks, ui, storage);
        } finally {
            System.out.flush();
            System.setOut(originalOut);
        }
        return output.toString();
    }

    private static void assertContains(String haystack, String needle) {
        if (!haystack.contains(needle)) {
            throw new AssertionError("Expected to find: " + needle + "\nBut got:\n" + haystack);
        }
    }

    private static void assertEquals(int a, int b, String msg) {
        if (a != b) {
            throw new AssertionError(msg + " Expected " + b + " but got " + a);
        }
    }

    private void runAll() throws Exception {
        String out1 = exec("todo read book");
        assertContains(out1, "Got it. I've added this task");
        assertContains(out1, "[T]");
        assertContains(out1, "read book");
        assertEquals(tasks.size(), 1, "Size mismatch after todo.");

        String out2 = exec("deadline return book /by 2025-10-15");
        assertContains(out2, "Got it. I've added this task");
        assertContains(out2, "[D]");
        assertContains(out2, "return book");
        assertContains(out2, "(by:");
        assertEquals(tasks.size(), 2, "Size mismatch after deadline.");

        String out3 = exec("event trip /from 2025-10-10 /to 2025-10-12");
        assertContains(out3, "Got it. I've added this task");
        assertContains(out3, "[E]");
        assertContains(out3, "trip");
        assertContains(out3, "from:");
        assertContains(out3, "to:");
        assertEquals(tasks.size(), 3, "Size mismatch after event.");

        String out4 = exec("list");
        assertContains(out4, "Here are the tasks in your list:");
        assertContains(out4, "1.");
        assertContains(out4, "2.");
        assertContains(out4, "3.");

        String out5 = exec("mark 2");
        assertContains(out5, "Nice! I've marked this task as done");
        assertContains(out5, "[D]");
        assertContains(out5, "return book");

        String out6 = exec("unmark 2");
        assertContains(out6, "OK, I've marked this task as not done yet");
        assertContains(out6, "[D]");
        assertContains(out6, "return book");

        String out7 = exec("find book");
        assertContains(out7, "Here are the matching tasks in your list:");
        assertContains(out7, "book");

        String out8 = exec("on 2025-10-11");
        assertContains(out8, "Here are the tasks on 2025-10-11");
        assertContains(out8, "trip");

        String out9 = exec("delete 1");
        assertContains(out9, "Noted. I've removed this task");
        assertContains(out9, "Now you have 2 tasks");

        boolean threw = false;
        try {
            exec("todo   ");
        } catch (KikiException e) {
            threw = true;
            if (!e.getMessage().contains("The description of a todo cannot be empty")) {
                throw new AssertionError("Unexpected error message: " + e.getMessage());
            }
        }
        if (!threw) {
            throw new AssertionError("Expected KikiException for empty todo.");
        }
    }

    public static void main(String[] args) throws Exception {
        KikiTest t = new KikiTest();
        t.runAll();
        System.out.println("All tests passed.");
    }
}
