/* vim: set tw=80 ts=4 sts=4 sw=4 et: */
import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Object to handle communication with the server. Encapsulates server
 * information and merely exposes methods to get and put data on the treams
 *
 * @author Steven C. Nay
 * @since 01 Feb 2007
 */
public class ClientComm implements Comm {

    //User information
    private String username;
    //Server information
    private String host;
    private int port;
    private Socket socket;
    //I/O streams
    private PrintStream out;
    private Scanner in;

    /**
     * Creates a ClientComm object with the specified host and port
     *
     * @param host IP address or hostname of server
     * @param port Port on server to connect to
     * @param username Username to identify to server
     * @throws IOException if the server connection process fails
     */
    public ClientComm(String host, int port, String username) throws IOException {
        this.username = username;
        this.host = host;
        this.port = port;
        try {
            //Create the socket
            socket = new Socket(this.host, this.port);
            //Get the I/O streams
            out = new PrintStream(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());
            //Send this user's name
            out.println(username);
            //Wait for authorization
            if (Integer.parseInt(in.nextLine()) != 0) {
                throw new IOException("Access denied by server on " + host);
            }
        } catch (Exception e) {
            //If creating the socket failed, we can't continue
            throw new IOException("Connection to server not established");
        }
    }

    /**
     * Checks if the connection is still open
     *
     * @return True if connection is alive, false otherwise
     */
    public boolean isAlive() {
        //TODO: Figure out how to get this to really return whether the
        // connection is up. This may require more sophisticated code
        // for pinging the client or something of that sort
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
     * Gets the local user's username
     *
     * @return Username
     */
    public String getUsername() {
        return username;
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
            return in.nextLine();
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
