/* vim: set tw=80 ts=4 sts=4 sw=4 et: */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Creates a client GUI to handle chat communication
 *
 * @author Steven C. Nay
 * @since 05 Feb 2007
 */
public class ClientGui implements ActionListener {

    //Sizing constants
    private static final int WINDOW_WIDTH = 475;
    private static final int WINDOW_HEIGHT = 500;
    private static final int TEXT_ROWS = 25;
    private static final int TEXT_COLS = 40;
    //Controls
    private JFrame frame;
    private JLabel name;
    private JTextField send;
    private JTextArea messages;
    private JScrollPane scroll;
    //Comm object
    private Comm comm;

    /**
     * Creates a ClientGui interface
     * @param comm Comm object to use for communication
     */
    public ClientGui(Comm comm) {
        this.comm = comm;
        //Frame
        frame = new JFrame(Application.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        //Main panel
        JPanel main = new JPanel();
        //Create a BorderLayout with 10px padding around the components
        main.setLayout(new BorderLayout(10, 10));
        //Name label
        name = new JLabel();
        name.setHorizontalAlignment(SwingConstants.CENTER);
        main.add(name, BorderLayout.NORTH);
        redrawTitle();
        //Messages box
        messages = new JTextArea();
        messages.setBorder(BorderFactory.createLineBorder(Color.black));
        messages.setRows(TEXT_ROWS);
        messages.setColumns(TEXT_COLS);
        messages.setEditable(false);        //Not editable
        messages.setLineWrap(true);         //Enable word wrapping
        messages.setWrapStyleWord(true);    //Wrap on word boundaries
        //Scroll pane for messages box
        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().add(messages);
        main.add(scroll, BorderLayout.CENTER);
        //Send box
        send = new JTextField();
        send.setBorder(BorderFactory.createLineBorder(Color.black));
        send.setColumns(TEXT_COLS);
        send.addActionListener(this);
        main.add(send, BorderLayout.SOUTH);
        //Finalize the frame.
        frame.getContentPane().add(main);
        frame.pack();
    }

    /**
     * Redraws the title label at the top of the screen
     */
    public void redrawTitle() {
        //Draw the title label again with username and host
        name.setText(comm.getUsername() + ": Chat on " + comm.getHost());
    }

    /**
     * Sets the visibility of the frame.
     * @param visible true to show, false to hide
     */
    public void setVisibility(boolean visible) {
        //Show the frame and set the focus to the send box
        frame.setVisible(visible);
        if (visible) {
            send.requestFocusInWindow();
        }
    }

    /**
     * Adds a line to the message queue
     * @param message Message to display
     */
    public void putMessage(String message) {
        //Get the scroll bar
        JScrollBar vbar = scroll.getVerticalScrollBar();
        //Determine if the scroll bar is at the bottom
        boolean auto_scroll = (1.25 * (vbar.getValue()) 
                >= (vbar.getMaximum() - vbar.getVisibleAmount()));
        //Append the message string to the messages text box
        messages.setText(messages.getText() + message + "\n");
        //Check to see if the bar is at the bottom before autoscrolling
        if (auto_scroll) {
            messages.setCaretPosition(messages.getDocument().getLength());
        }
    }

    /**
     * Listener for actions. Called when an event occurs on the send box
     * (usually pressing enter)
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == send) {
            //Trim whitespace off the text and make sure it's not empty
            String text = send.getText().trim();
            if (!text.equals("")) {
                //Try to send the text in the box over the connection
                try {
                    //Check if this is a command first
                    if (text.equals("\\q")) {   //QUIT
                        //TODO: Add some stuff here to let us tell the server
                        // we're closing the connection.
                        comm.close();
                        System.exit(0);
                    }
                    //Send the text over the connection
                    comm.putLine(text);
                    //NOTE: The line will be printed to the messages box when
                    // the server's response comes back
                } catch (Exception e) {
                    //Print any communications exceptions
                    e.printStackTrace();
                }
            }
            //Clear the text box and make sure it has the focus
            send.setText("");
            send.requestFocusInWindow();
        }
    }
}
