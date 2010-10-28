/* vim: set tw=80 ts=4 sts=4 sw=4 et: */
import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Creates a client instance for communicating with a compatable Server
 * instance.
 *
 * @author Steven C. Nay
 * @since 05 Feb 2007
 */
public class Client {

    private static Comm comm;
    private static ClientGui gui;

    /**
     * Runs the client
     * @param args Pass in connection parameters as follows:
     *      - Host:     IP address or hostname of server instance
     *      - Port:     Port number to connect on
     *      - Username: Screen name for the server. May be rejected if invalid
     *                  or already in use
     */
    public static void main(String[] args) {

        System.out.println(Application.NAME);
        Scanner console = new Scanner(System.in);

        //Get initialization settings from the command-line arguments
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String you = args[2];

        try {
            //Create the ClientComm object
            comm = new ClientComm(host, port, you);
            //Create the GUI
            gui = new ClientGui(comm);
            gui.setVisibility(true);
            //Set up the loop to read input from the comm object
            while (true) {
                try {
                    //Get the next line; will block until a line comes
                    String text = comm.nextLine();
                    gui.putMessage(text);
                } catch (Exception e) {
                    //The connection has died
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "The connection with the server was lost");
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            //If the connection fails, we can't go on.
            JOptionPane.showMessageDialog(null, 
                    "Error sending message: Connection could not be opened");
            System.exit(1);
        }
    }
}
