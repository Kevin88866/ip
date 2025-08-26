import java.util.Scanner;
public class kiki {
    private static final String horizontalLine = "____________________________________________________________";
    private static String[] tasks = new String[100];
    private static int count = 0;

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
                for(int i = 0; i < count; i++) System.out.println((i + 1) + ". " + tasks[i]);
                System.out.println(horizontalLine);
            }else{
                tasks[count] = input;
                count++;
                MessagePrinter(" added: " + input);
            }
        }
        MessagePrinter(" Bye. Hope to see you again soon!");

        sc.close();
    }
}