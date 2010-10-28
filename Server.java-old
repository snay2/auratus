import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Creates a server instance for single-connection peer-to-peer chat
 *
 * @author Steven C. Nay
 * @since 01 Feb 2007
 */
public class Server {

    private static Comm comm;
    private static ClientGui gui;

    /**
     * Runs the server
     * @param args Nothing required
     */
    public static void main(String[] args) {
        try {
            //Get the user's information
            Scanner console = new Scanner(System.in);
            System.out.print("Enter the port to open: ");
            int port = console.nextInt();
            System.out.print("Enter your name: ");
            String name = console.nextLine();
            //Create the ServerComm object
            comm = new ServerComm(new ServerSocket(port));
            gui = new ClientGui(comm);
            gui.setVisibility(true);
            while (true) {
                //Try to read input from the comm object
                try {
                    String text = comm.nextLine();
                    gui.putMessage(text);
                } catch (Exception e) {}
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
        }
    }
}
