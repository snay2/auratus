import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    private static String host;
    private static String you;
    private static String them;
    private static int port;

    public static void main(String[] args) {
        System.out.println("P2P Ghetto Chat Client");
        System.out.println("Allows you to send single-line instant messages\n"
                + "to a P2PGhettoChat server instance");
        Scanner console = new Scanner(System.in);

        //Get initialization settings
        System.out.print("Enter the IP address of the server: ");
        host = console.nextLine();
        System.out.print("Enter your name: ");
        you = console.nextLine();
        port = 3421;

        //Try to open the socket
        PrintStream out = null;
        Scanner in = null;
        try {
            Socket s = new Socket(host, port);
            out = new PrintStream(s.getOutputStream());
            in = new Scanner(s.getInputStream());
            //Send this user's name
            out.println(you);
            //Get the success/failure code
            System.out.println("Waiting for authorization...");
            if (Integer.parseInt(in.nextLine()) == 1) {
                System.out.println("Access was denied by the server on " + host);
                System.exit(1);
            }
            //Get the server user's name
            String them = in.nextLine();
            //Print the header block
            System.out.println("Starting a chat with " + them + " on " + host);
            System.out.println("=============================================");
            while (true) {
                System.out.print("You: ");
                out.println(console.nextLine());
                String next = in.nextLine();
                if (next == null) {
                    System.out.println("You have logged off. Connection closed.");
                    System.exit(0);
                }
                System.out.println(them + ": " + next);
            }
        } catch (Exception e) {
            System.err.println("Error sending message: Connection closed");
            System.exit(1);
        } finally {
            out.close();
        }
    }
}
