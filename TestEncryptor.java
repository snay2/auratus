/* vim: set tw=80 ts=4 sts=4 sw=4 et: */

/**
 * Dummy encryption program.  Behaves like an encryption program but does
 * not actually encrypt or decrypt anything.
 *
 * @author Corey Christensen
 * @since 08 Feb 2007
 */

public class TestEncryptor implements Encryptor
{
	private String key;

	/**
	 * Creates a new Test Encryptor.
	 * Sets encryption key string to "Testing phase 1...".
	 */
	public TestEncryptor ()
	{
		key = "Testing phase 1...";
	}

	/**
	 * encrypts a string.  Returns the string given to it
	 * unchanged from its original form.
	 *
	 * @param message String to encrypt
	 * @return Encrypted string
	 */
	public String encrypt (String message)
	{
		return message;
	}

	/**
	 * Decrypts a string.  Returns the string given to it
	 * unchanged from its original form.
	 *
	 * @param message String to decrypt
	 * @return Decrypted string
	 */
	public String decrypt (String message)
	{
		return message;
	}

	/**
	 * Generates a new encryption key.  Sets the key string
	 * to "Testing phase 2...".
	 */
	public void generateNewKey ()
	{
		key = "Testing phase 2...";
	}

	/**
	 * Sets the encryption key to the given key.
	 *
	 * @param new_key the new encryption key.
	 */
	public void setKey (String new_key)
	{
		key = new_key;
	}

	/**
	 * returns the current encryption key.
	 *
	 * @return the current encryption key.
	 */
	public String getKey ()
	{
		return key;
	}
}
