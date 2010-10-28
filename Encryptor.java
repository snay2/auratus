/* vim: set tw=80 ts=4 sts=4 sw=4 et: */
/**
 * Interface for various encryption programs.
 *
 * @author Corey Christensen
 * @since 31 Jan 2007
 */
public interface Encryptor {

    /**
     * Encrypts a string
     *
     * @param message String to encrypt
     * @return Encrypted string
     */
	public String encrypt(String message);

    /**
     * Decrypts a string
     *
     * @param message String to decrypt
     * @return Decrypted string
     */
	public String decrypt(String message);

    /**
     * Generates a new encryption key
     */
	public void generateNewKey();

    /**
     * Sets the encryption key
     *
     * @param key New key to use
     */
	public void setKey(String key);

    /**
     * Returns the encryption key currently used
     *
     * @return Encryption key
     */
	public String getKey();
}
