package kiki.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import kiki.exception.KikiException;
import kiki.task.Deadline;
import kiki.task.Event;
import kiki.task.Task;
import kiki.task.Todo;

/**
 * Handles persistent storage of tasks to and from a text file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Constructs a Storage object for a given directory and file.
     *
     * @param dir  directory where the file is stored.
     * @param file filename for the tasks' data.
     */
    public Storage(String dir, String file) {
        this.filePath = Paths.get(dir, file);
    }

    /**
     * Loads tasks from the file.
     *
     * @return a list of tasks (empty if file does not exist).
     * @throws IOException if the file is corrupted or unreadable.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(filePath)) {
                Path parent = filePath.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                return tasks;
            }

            try (BufferedReader br = Files.newBufferedReader(filePath)) {
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    lineNumber += 1;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] p = line.split("\\s*\\|\\s*");

                    if (p.length < 3) {
                        throw new IOException("Corrupted save file at line " + lineNumber + ": " + line);
                    }

                    String type = p[0].trim();
                    boolean done = "1".equals(p[1].trim());

                    switch(type) {
                    case "T": {
                        Task t = new Todo(p[2]);
                        if (done) {
                            t.markDone();
                        }
                        tasks.add(t);
                        break;
                    }
                    case "D": {
                        if (p.length >= 4) {
                            try {
                                Task t = new Deadline(p[2], p[3]);
                                if (done) {
                                    t.markDone();
                                }
                                tasks.add(t);
                            } catch (KikiException e) {
                                throw new IOException("Invalid event dates (expect yyyy-mm-dd). Line: " + line, e);
                            }
                        } else {
                            throw new IOException("Corrupted save file at line " + lineNumber + ": " + line);
                        }
                        break;
                    }
                    case "E": {
                        if (p.length >= 5) {
                            try {
                                Task t = new Event(p[2], p[3], p[4]);
                                if (done) {
                                    t.markDone();
                                }
                                tasks.add(t);
                            } catch (KikiException e) {
                                throw new IOException("Invalid event dates (expect yyyy-mm-dd). Line: " + line, e);
                            }
                        } else {
                            throw new IOException("Malformed event record: " + line);
                        }
                        break;
                    }
                    default: {
                        throw new IOException("Unknown task type at line " + lineNumber + ": " + type);
                    }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: failed to load tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves tasks to the file.
     *
     * @param tasks the list of tasks to save.
     * @throws IOException if the file cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter bw = Files.newBufferedWriter(filePath,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Task t : tasks) {
                bw.write(t.toSaveString());
                bw.newLine();
            }
        }
    }
}
