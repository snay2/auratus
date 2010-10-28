/* vim: set tw=80 ts=4 sts=4 sw=4 et: */
import java.io.IOException;

/**
 * Basic communications object
 *
 * @author Steven C. Nay
 * @since 01 Feb 2007
 */
public interface Comm {

    /**
     * Minimum allowable username length
     */
    public static final int MIN_NAME_LENGTH = 2;

    /**
     * Maximum allowable username length
     */
    public static final int MAX_NAME_LENGTH = 15;

    /**
     * Gets the user's or remote user's name (depending on the implementation)
     * @return Username
     */
    public String getUsername();

    /**
     * Gets the remote user's hostname/IP address
     * @return Hostname/IP address
     */
    public String getHost();

    /**
     * Checks if the connection is alive
     * @return True if alive, false otherwise
     */
    public boolean isAlive();

    /**
     * Closes the connection
     * @return True on success, false otherwise
     */
    public boolean close();

    /**
     * Gets the next line on the input stream
     * @return Next line on the stream
     * @throws IOException if no data on the stream
     */
    public String nextLine() throws IOException;

    /**
     * Puts data on the output stream
     * @param data Data to put
     * @throws IOException if output stream cannot be written
     */
    public void putLine(String data) throws IOException;
}
