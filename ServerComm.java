/* vim: set tw=80 ts=4 sts=4 sw=4 et: */
import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Creates a server socket for communication with a client.
 *
 * @author Steven C. Nay
 * @since 07 Feb 2007
 */
public class ServerComm implements Comm {

    //TODO: See if it is possible to combine more of this code into an abstract
    // class or something.  There is a lot of duplication between ServerComm
    // and ClientComm, but I don't know how to combine them. I think now the 
    // only real difference between them is the constructor; everthing else 
    // should be the same.

    //User information
    private String remote_name;
    //Server information
    private Socket socket;
    private String host;
    private int port;
    //I/O streams
    private PrintStream out;
    private Scanner in;

    /**
     * Creates a server using the provided ServerSocket. Passing a ServerSocket
     * allows multiple connections to be made on the same port.
     *
     * @param server Server socket to listen on
     * @throws IOException if the server startup process fails
     */
    public ServerComm(ServerSocket server) throws IOException {
        this.port = port;
        try {
            //Accept a connection on the ServerSocket
            socket = server.accept();
            //Get the I/O streams
            out = new PrintStream(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());
            //Read in the user's name
            remote_name = in.nextLine();
            host = socket.getInetAddress().toString();
            //check whether name length is acceptable
            boolean allow =
                    (remote_name.length() >= Comm.MIN_NAME_LENGTH) &&
                    (remote_name.length() <= Comm.MAX_NAME_LENGTH);
            if (allow) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Connection requested by " + remote_name + " on " +
                        host + ". Allow?", "Allow incoming chat",
                        JOptionPane.YES_NO_OPTION);
                allow = (response == JOptionPane.YES_OPTION);
            }
            //Allow or deny the connection
            if (!allow) {
                out.println(1); //Send failure code
                throw new IOException("Authorization failed");
            } else {
                out.println(0); //Send success code
            }
            //Print out the username on the console.
            //TODO: Create a GUI to manage the server instance. Low-priority
            System.out.println(host + ": " + remote_name);
        } catch (Exception e) {
            throw new IOException("Connection lost");
        }
    }

    /**
     * Checks if the connection is still open
     *
     * @return True if connection is alive, false otherwise
     */
    public boolean isAlive() {
        //TODO: Fix this.  See ClientComm.java for more info.
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * Closes the connection
     *
     * @return True on success, false otherwise
     */
    public boolean close() {
        try {
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Gets the remote user's name
     *
     * @return Username
     */
    public String getUsername() {
        return remote_name;
    }

    /**
     * Gets the server's hostname/IP address
     *
     * @return hostname/IP address
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the next line on the input stream
     *
     * @return Next line on the stream
     * @throws IOException if no data on the stream
     */
    public String nextLine() throws IOException {
        if (in.hasNextLine()) {
            return remote_name + ": " + in.nextLine();
        } else {
            throw new IOException("No data on input stream");
        }
    }

    /**
     * Puts data on the output stream
     *
     * @param data Data to put
     * @throws IOException if output stream cannot be written
     */
    public void putLine(String data) throws IOException {
        try {
            out.println(data);
        } catch (Exception e) {
            throw new IOException("Data cannot be written to output stream");
        }
    }
}
