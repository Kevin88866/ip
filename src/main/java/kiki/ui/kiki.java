package kiki.ui;

import kiki.exception.KikiException;
import kiki.task.Deadline;
import kiki.task.Event;
import kiki.task.Task;
import kiki.task.Todo;
import kiki.storage.Storage;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class kiki {
    private static final String horizontalLine = "____________________________________________________________";
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Storage storage = new Storage("data","kiki.txt");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MessagePrinter(" Hello! I'm Kiki.kiki\n  What can I do for you?");

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
        while(!(input = sc.nextLine()).equals("bye")){
            try{
                //use handle function to avoid the program crashed because of invalid input
                //and exception can tell user how to input in correct demand
                handle(input);
            }catch(KikiException e){
                //print the message in KikiException
                MessagePrinter(e.getMessage());
            }catch(Exception e){
                //Exception is used when the exception isn't cover by KikiException
                MessagePrinter(" OOPS!!! Something went wrong. (" + e.getClass().getSimpleName() + ")");
            }
        }
        MessagePrinter(" Bye. Hope to see you again soon!");
        sc.close();
    }

    //handle input
    //will throw KikiException when something get wrong
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

        //length < 2 means the input doesn't have number after mark/unmark.
        if(parts.length < 2){
            throw new KikiException(" OOPS!!! Please provide a task number for '" + cmd + "'.");
        }

        int n;

        try{
            //parseInt will throw NumberFormatException if the input is not an integer ( like 'a',1.2,'abc')
            n = Integer.parseInt(parts[1]);
        }catch(NumberFormatException e){
            throw new KikiException(" OOPS!!! '" + parts[1] + "' is not a valid task number.");
        }

        //this means the number is invalid
        if(n < 1 || n > tasks.size()){
            throw new KikiException(" OOPS!!! Kiki.task.Task number is out of range. You have " + tasks.size() + " task(S).");
        }

        return n-1;
    }

    //just use to print the message between two horizontal line
    private static void MessagePrinter(String message){
        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }

    //print all tasks
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
        saveList(tasks);
    }

    private static void markTask(int index) {
        tasks.get(index).markDone();
        MessagePrinter(" Nice! I've marked this task as done:\n    " + tasks.get(index));
        saveList(tasks);
    }

    private static void printTask(Task task){
        MessagePrinter(" Got it. I've added this task:\n    " + task + "\n  Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void saveList(ArrayList<Task> tasks){
        try {
            storage.save(tasks);
        } catch (IOException e) {
            System.out.println(horizontalLine);
            System.out.println(" OOPS!!! Failed to save tasks: " + e.getMessage());
            System.out.println(horizontalLine);
        }
    }

    private static void addTodo(String input) throws KikiException{
        String work;

        if(input.length() > 4){
            work = input.substring(5);
        }else{
            work = "";
        }

        //when the length<=4, the work is empty and throw exception
        if(work.isEmpty()){
            throw new KikiException(" OOPS!!! The description of a todo cannot be empty.");
        }

        Todo task = new Todo(work);
        tasks.add(task);
        printTask(task);
        saveList(tasks);
    }

    private static void addDeadline(String input) throws KikiException{
        int index = input.indexOf("/by");
        String work = "", by = "";

        //when index = -1, the app can't find "/by", throw exception
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
        saveList(tasks);
    }

    private static void addEvent(String input) throws KikiException{
        String afterKeyword;

        if(input.length() > 5){
            afterKeyword = input.substring(6);
        }else{
            afterKeyword = "";
        }

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
        saveList(tasks);
    }
}