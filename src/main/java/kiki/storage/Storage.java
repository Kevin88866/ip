package kiki.storage;

import kiki.task.Task;
import kiki.task.Deadline;
import kiki.task.Todo;
import kiki.task.Event;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String dir, String file){
        this.filePath = Paths.get(dir, file);
    }

    //load file
    public ArrayList<Task> load() throws IOException{
        ArrayList<Task> tasks = new ArrayList<>();
        try{
            if(!Files.exists(filePath)){
                Path parent = filePath.getParent();
                if(parent != null){
                    Files.createDirectories(parent);
                }
                return tasks;
            }

            try (BufferedReader br = Files.newBufferedReader(filePath)){
                String line;
                int lineNumber = 0;
                while((line = br.readLine()) != null){
                    lineNumber+=1;
                    line = line.trim();
                    if(line.isEmpty()){
                        continue;
                    }
                    String[] p = line.split("\\S*\\|\\s*");

                    if(p.length < 3){
                        throw new IOException("Corrupted save file at line " + lineNumber + ": " + line);
                    }

                    String type = p[0].trim();
                    boolean done = "1".equals(p[1].trim());

                    switch(type){
                    case "T":{
                        Task t = new Todo(p[2]);
                        if(done){
                            t.markDone();
                        }
                        tasks.add(t);
                        break;
                    }
                    case "D":{
                        if(p.length >= 4){
                            Task t = new Deadline(p[2], p[3]);
                            if(done){
                                t.markDone();
                            }
                            tasks.add(t);
                        }else{
                            throw new IOException("Corrupted save file at line " + lineNumber + ": " + line);
                        }
                        break;
                    }
                    case "E":{
                        if(p.length >= 5){
                            Task t = new Event(p[2], p[3], p[4]);
                            if(done){
                                t.markDone();
                            }
                            tasks.add(t);
                        }else{
                            throw new IOException("Corrupted save file at line " + lineNumber + ": " + line);
                        }
                        break;
                    }
                    default:{
                        throw new IOException("Unknown task type at line " + lineNumber + ": " + type);
                    }
                    }
                }
            }
        } catch (IOException e){
            System.err.println("Warning: failed to load tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void save(List<Task> tasks) throws IOException{
        Path parent = filePath.getParent();
        if(parent != null){
            Files.createDirectories(parent);
        }

        try(BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
            for(Task t : tasks){
                bw.write(t.toSaveString());
                bw.newLine();
            }
        }
    }
}
