import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Creates a threaded chat server by opening 10 ports with server connections
 *
 * @author Steven C. Nay
 * @since 05 Feb 2007
 */
public class ThreadedServer {

    //Contants
    private static final int MAX_CONNECTIONS = 10;
    private static final int PORT = 3421;
    private static final int QUEUE_SIZE = 100;
    //  legnth of sleep between iterations of message loops (ms)
    private static final int SLEEP_LENGTH = 100;
    //  determines how large the queue can get before being dumped
    private static final int MAX_QUEUE_SIZE = 150;
    //Server socket
    private static ServerSocket socket = null;
    //Output object and message queue
    private static OutputThread output;
    private static List<String> messages;

    /**
     * Runs the threaded server
     *
     * @param args Nothing needed here
     */
    public static void main(String[] args) {
        messages = Collections.synchronizedList(
                new ArrayList<String>(QUEUE_SIZE));
        //connections = new Collections.synchronizedList(
        //        new LinkedList<ServerComm>(MAX_CONNECTIONS));
        try {
            socket = new ServerSocket(PORT);
        } catch (Exception e) {
            System.err.println("Unable to open ServerSocket");
            System.exit(1);
        }
        for (int i = 1; i <= MAX_CONNECTIONS; i++) {
            openConnection(socket);
        }
        output = new OutputThread();
        output.start();
    }

    /**
     * Opens a new server connection
     *
     * @param s ServerSocket to connect to
     * @return New ServerThread object, or null if it fails
     */
    public static ServerThread openConnection(ServerSocket s) {
        ServerThread server = null;
        try {
            server = new ServerThread(s);
            server.start();
        } catch (Exception e) {
            System.err.println("Unable to open server connections\n" + e);
        }
        return server;
    }

    /**
     * Private class to handle server threads
     */
    private static class ServerThread extends Thread {

        private ServerSocket server;
        private Comm comm;
        private InputThread input;
    
        /**
         * Creates a new server thread
         *
         * @param server ServerSocket to use for connections
         */
        public ServerThread(ServerSocket server) {
            this.server = server;
        }

        /**
         * Returns the Comm object used by this ServerThread
         *
         * @return Comm object
         */
        public Comm getComm() {
            return comm;
        }

        /**
         * Initializes and runs the thread
         *
         * @throws IOException if the socket cannot be opened or if the user
         *      is denied access
         */
        public void run() {
            try {
                //Attempt to create a ServerComm object on this port
                comm = new ServerComm(server);
                //Create an InputThread
                input = new InputThread(comm);
                input.start();
                //Add to the OutputThread queue
                output.add(comm);
                //Add a line to the message queue informing users that this user
                // is now in the chat
                try {
                    synchronized(messages) {
                        messages.add("User " + comm.getUsername() + " has"
                                + " joined the chat.");
                    }
                } catch (Exception e) {}
            } catch (Exception e) {
                System.err.println("Connection denied or not established");
            }
        }
    }

    /**
     * Private class to handle threaded input
     */
    private static class InputThread extends Thread {

        private Comm comm;

        /**
         * Creates a new input thread
         *
         * @param comm Comm object to read from
         */
        public InputThread(Comm comm) {
            this.comm = comm;
        }

        /**
         * Runs the thread
         */
        public void run() {
            while (comm != null) {
                //Try to read input from the comm object
                try {
                    String text = comm.nextLine();
                    synchronized(messages) {
                       messages.add(text);
                    }
                    Thread.sleep(SLEEP_LENGTH);
                } catch (Exception e) {
                    //The connection has died.
                    //Add a message to the queue to let users know someone has left.
                    synchronized(messages) {
                        messages.add("User " + comm.getUsername() + " has left"
                                + " the chat.");
                    }
                    comm = null;
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Private class to handle threaded output
     */
    private static class OutputThread extends Thread {

        //Array to hold the connection objects
        private Comm[] connections;
        //Number of open connections
        private int num_conn;
        //Current position in the message log
        private int pos;

        /**
         * Creates a new output thread
         */
        public OutputThread() {
            connections = new Comm[MAX_CONNECTIONS];
            num_conn = 0;
            //Set the position to the last position currently in the list
            synchronized(messages) {
                pos = messages.size();
            }
        }

        /**
         * Adds a Comm object to the list of connections
         *
         * @param comm Comm object to add
         * @return True if the object was added, false otherwise
         */
        public boolean add(Comm comm) {
            //Check that we have room
            if (num_conn < connections.length - 1) {
                connections[num_conn++] = comm;
                return true;
            }
            return false;
        }

        /**
         * Checks all the connections and removes dead ones
         */
        private void checkConnections() {
            for (int c = 0; c < connections.length; c++) {
                if ((connections[c] != null
                            && !connections[c].isAlive())) {
                //connections[c] == null ||
                /*
                    if (!connections[c].isAlive()) {
                        connections[c] = null;
                        System.out.println("Removing dead connection.");
                        for (int i = c; i < connections.length; i++) {
                            connections[i] = connections[i + 1];
                        }
                    }
                    
                } else {
                */
                    //Attempt to open a new connection in this index
                    connections[c] = openConnection(socket).getComm();
                }
            }
        }

        /**
         * Checks the log and dumps it if its size exceeds MAX_QUEUE_SIZE
         */
        private void checkLogSize() {
            synchronized(messages) {
                if (messages.size() > MAX_QUEUE_SIZE) {
                    messages.clear();
                }
            }
        }

        /**
         * Runs the thread
         */
        public void run() {
            while(true) {
                try {
                    synchronized(messages) {
                        int size = messages.size();
                        if (size > pos) { //if we have new messages
                            //Get the rest of the messages
                            for (int i = pos; i < size; i++) {
                                //Go through each connection
                                for (int c = 0; c < num_conn; c++) {
                                    //If this is a valid connection object,
                                    // print the message to its stream
                                    if (connections[c] != null) {
                                        connections[c].putLine(messages.get(i));
                                    }
                                }
                            }
                        }
                        pos = size;
                    }
                    //Clear out any dead connections
                    checkConnections();
                    //Dump the log if it is too large
                    checkLogSize();
                    Thread.sleep(SLEEP_LENGTH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
