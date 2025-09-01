import java.util.Scanner;
import java.util.ArrayList;

public class kiki {
    private static final String horizontalLine = "____________________________________________________________";
    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MessagePrinter(" Hello! I'm kiki\n  What can I do for you?");
        String input;
        while(!(input = sc.nextLine()).equals("bye")){
            if(input.equals("list")){
                printList();
            }else if(input.startsWith("mark")){
                markTask(input);
            }else if(input.startsWith("unmark")){
                unmarkTask(input);
            }else if(input.startsWith("todo")){
                addTodo(input);
            }else if(input.startsWith("deadline")){
                addDeadline(input);
            }else if(input.startsWith("event")){
                addEvent(input);
            }
        }
        MessagePrinter(" Bye. Hope to see you again soon!");
        sc.close();
    }

    private static void MessagePrinter(String message){
        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }

    private static void printList() {
        System.out.println(horizontalLine);
        System.out.println("  Here are the tasks in your list:");
        for(int i = 0; i < tasks.size(); i++) System.out.println("  " + (i+1) + ". " + tasks.get(i).toString());
        System.out.println(horizontalLine);
    }

    private static void unmarkTask(String input) {
        int index = Integer.parseInt(input.substring(7)) - 1;
        if(index >= 0 && index < tasks.size()){
            tasks.get(index).markNotDone();
            MessagePrinter(" OK, I've marked this task as not done yet:\n    " + tasks.get(index).toString());
        }else{
            MessagePrinter("Invalid task number");
        }
    }

    private static void markTask(String input) {
        int index = Integer.parseInt(input.substring(5)) - 1;
        if(index >= 0 && index < tasks.size()){
            tasks.get(index).markDone();
            MessagePrinter(" Nice! I've marked this task as done:\n    " + tasks.get(index).toString());
        }else{
            MessagePrinter("Invalid task number");
        }
    }

    private static void printTask(Task task){
        MessagePrinter(" Got it. I've added this task:\n    " + task.toString() + "\n  Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addTodo(String input) {
        Todo task = new Todo(input.substring(5));
        tasks.add(task);
        printTask(task);
    }

    private static void addDeadline(String input) {
        int index = input.indexOf("/by");
        Deadline task = new Deadline(input.substring(9,index-1), input.substring(index+4));
        tasks.add(task);
        printTask(task);
    }

    private static void addEvent(String input) {
        int indexOfFrom = input.indexOf("/from"), indexOfTo = input.indexOf("/to");
        Event task = new Event(input.substring(6,indexOfFrom-1),input.substring(indexOfFrom+6,indexOfTo-1),input.substring(indexOfTo+4));
        tasks.add(task);
        printTask(task);
    }
}