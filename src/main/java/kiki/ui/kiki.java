package kiki.ui;

import kiki.exception.KikiException;
import kiki.task.Deadline;
import kiki.task.Event;
import kiki.task.Task;
import kiki.task.Todo;

import java.util.Scanner;
import java.util.ArrayList;

public class kiki {
    private static final String horizontalLine = "____________________________________________________________";
    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MessagePrinter(" Hello! I'm Kiki.kiki\n  What can I do for you?");
        String input;
        while(!(input = sc.nextLine()).equals("bye")){
            try{
                handle(input);
            }catch(KikiException e){
                MessagePrinter(e.getMessage());
            }catch(Exception e){
                MessagePrinter(" OOPS!!! Something went wrong. (" + e.getClass().getSimpleName() + ")");
            }
        }
        MessagePrinter(" Bye. Hope to see you again soon!");
        sc.close();
    }

    private static void handle(String input) throws KikiException{
        input = input.trim();
        if(input.equals("list")){
            printList();
        }else if(input.startsWith("mark")){
            int index = parseIndex(input,"mark");
            markTask(index);
        }else if(input.startsWith("unmark")){
            int index = parseIndex(input,"unmark");
            unmarkTask(index);
        }else if(input.startsWith("todo")){
            addTodo(input);
        }else if(input.startsWith("deadline")){
            addDeadline(input);
        }else if(input.startsWith("event")){
            addEvent(input);
        }else{
            throw new KikiException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static int parseIndex(String input, String cmd) throws KikiException{
        String[] parts = input.split("\\s+");
        if(parts.length < 2){
            throw new KikiException(" OOPS!!! Please provide a task number for '" + cmd + "'.");
        }
        int n;
        try{
            n = Integer.parseInt(parts[1]);
        }catch(NumberFormatException e){
            throw new KikiException(" OOPS!!! '" + parts[1] + "' is not a valid task number.");
        }
        if(n < 1 || n > tasks.size()){
            throw new KikiException(" OOPS!!! Kiki.task.Task number is out of range. You have " + tasks.size() + " task(S).");
        }
        return n-1;
    }

    private static void MessagePrinter(String message){
        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }

    private static void printList() {
        System.out.println(horizontalLine);
        System.out.println("  Here are the tasks in your list:");
        for(int i = 0; i < tasks.size(); i++){
            System.out.println("  " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(horizontalLine);
    }

    private static void unmarkTask(int index) {
        tasks.get(index).markNotDone();
        MessagePrinter(" OK, I've marked this task as not done yet:\n    " + tasks.get(index));
    }

    private static void markTask(int index) {
        tasks.get(index).markDone();
        MessagePrinter(" Nice! I've marked this task as done:\n    " + tasks.get(index));
    }

    private static void printTask(Task task){
        MessagePrinter(" Got it. I've added this task:\n    " + task + "\n  Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addTodo(String input) throws KikiException{
        String work = (input.length() > 4) ? input.substring(5) : "";
        if(work.isEmpty()){
            throw new KikiException(" OOPS!!! The description of a todo cannot be empty.");
        }
        Todo task = new Todo(work);
        tasks.add(task);
        printTask(task);
    }

    private static void addDeadline(String input) throws KikiException{
        int index = input.indexOf("/by");
        String work = "", by = "";
        if(index == -1){
            throw new KikiException(" OOPS!!! Kiki.task.Deadline requires '/by <time>'. Example: deadline return book /by Sunday");
        }else{
            String[] parts = input.substring(8).trim().split("/by",2);
            if(parts.length == 2){
                work = parts[0].trim();
                by = parts[1].trim();
            }
        }
        if(work.isEmpty()){
            throw new KikiException(" OOPS!!! The description of a deadline cannot be empty.");
        }
        if(by.isEmpty()){
            throw new KikiException(" OOPS!!! The time of a deadline cannot be empty. Use '/by <time>'.");
        }
        Deadline task = new Deadline(work, by);
        tasks.add(task);
        printTask(task);
    }

    private static void addEvent(String input) throws KikiException{
        String afterKeyword = (input.length() > 5) ? input.substring(6) : "";
        String[] pFrom = afterKeyword.split("/from",2);
        if(pFrom.length < 2){
            throw new KikiException(" OOPS!!! Kiki.task.Event requires '/from <start>' and '/to <end>'.");
        }
        String work = pFrom[0].trim();
        String[] pTo = pFrom[1].split("/to",2);
        if(pTo.length < 2){
            throw new KikiException(" OOPS!!! Kiki.task.Event requires '/to <end>'.");
        }
        String from = pTo[0].trim();
        String to = pTo[1].trim();
        if(work.isEmpty()){
            throw new KikiException(" OOPS!!! The description of an event cannot be empty.");
        }
        if(from.isEmpty()){
            throw new KikiException(" OOPS!!! The start time of an event cannot be empty. Use '/from <start>'.");
        }
        if(to.isEmpty()){
            throw new KikiException(" OOPS!!! The end time of an event cannot be empty. Use '/to <end>'.");
        }
        Event task = new Event(work,from,to);
        tasks.add(task);
        printTask(task);
    }
}