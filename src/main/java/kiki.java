import java.util.Scanner;
public class kiki {
    private static void MessagePrinter(String message){
        String horizontalLine = "____________________________________________________________";
        System.out.println(horizontalLine);
        System.out.println(" " + message);
        System.out.println(horizontalLine);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MessagePrinter(" Hello! I'm kiki\n  What can I do for you?");
        String input;
        while(!(input = sc.nextLine()).equals("bye")){
            MessagePrinter(input);
        }
        MessagePrinter(" Bye. Hope to see you again soon!");
        sc.close();
    }
}