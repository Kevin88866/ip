import java.util.Scanner;
import java.util.ArrayList;

class Task{
    private String task;
    private boolean isDone = false;
    public Task(String task){ this.task = task;}
    public void markDone(){ isDone = true;}
    public void markNotDone(){ isDone = false;}
    public String getTask(){ return "[" + (isDone? "X":" ") + "] " + task;}
}

public class kiki {
    private static final String horizontalLine = "____________________________________________________________";
    private static ArrayList<Task> tasks = new ArrayList<>();

    private static void MessagePrinter(String message){

        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MessagePrinter(" Hello! I'm kiki\n  What can I do for you?");
        String input;
        while(!(input = sc.nextLine()).equals("bye")){
            if(input.equals("list")){
                System.out.println(horizontalLine);
                System.out.println(" Here are the tasks in your list:");
                for(int i = 0; i < tasks.size(); i++) System.out.println(" " + (i+1) + "." + tasks.get(i).getTask());
                System.out.println(horizontalLine);
            }else if(input.startsWith("mark")){
                int index = Integer.parseInt(input.substring(5)) - 1;
                if(index >= 0 && index < tasks.size()){
                    tasks.get(index).markDone();
                    MessagePrinter(" Nice! I've marked this task as done:\n    " + tasks.get(index).getTask());
                }else{
                    MessagePrinter("Invalid task number");
                }
            }else if(input.startsWith("unmark")){
                int index = Integer.parseInt(input.substring(7)) - 1;
                if(index >= 0 && index < tasks.size()){
                    tasks.get(index).markNotDone();
                    MessagePrinter(" OK, I've marked this task as not done yet:\n    " + tasks.get(index).getTask());
                }else{
                    MessagePrinter("Invalid task number");
                }
            }else{
                tasks.add(new Task(input));
                MessagePrinter(" added: " + input);
            }
        }
        MessagePrinter(" Bye. Hope to see you again soon!");

        sc.close();
    }
}