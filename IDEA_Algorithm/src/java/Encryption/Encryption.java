/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Encryption;

/**
 *
 * @author User
 */
import java.nio.charset.StandardCharsets; // Import for explicit character encoding

public abstract class Encryption extends Utils {

    protected String name;

    /**
     * How big a block is in bytes.
     */
    protected int blockSize;

    /**
     * How big a key is in bytes. Key-less ciphers use 0. Variable-length-key ciphers
     * also use 0.
     */
    protected int keySize;

    /**
     * Encryption method. Encrypts a plain text string.
     *
     * @param text The plain text string to encrypt.
     * @return The encrypted text string (ciphertext).
     */
    public abstract String encrypt(String text);

    /**
     * Decryption method. Decrypts a ciphertext string.
     *
     * @param text The ciphertext string to decrypt.
     * @return The decrypted plain text string.
     */
    public abstract String decrypt(String text);

    /**
     * Utility routine to turn a string into a key byte array of the right length.
     * This method derives a fixed-length key from a variable-length string.
     * For production-grade security, consider a robust Key Derivation Function (KDF)
     * like PBKDF2 for password-based key generation.
     *
     * @param keyStr The input string from which to derive the key.
     * @return A byte array representing the derived key.
     */
    protected byte[] makeKey(String keyStr) {
        byte[] key;
        if (keySize == 0) {
            // If keySize is 0, the key length will be the length of the string bytes
            key = keyStr.getBytes(StandardCharsets.UTF_8); // Use UTF-8 for consistent byte conversion
        } else {
            // Otherwise, initialize key with the specified keySize
            key = new byte[keySize];
        }
        int i, j;

        // Initialize key bytes to 0
        for (j = 0; j < key.length; ++j) {
            key[j] = 0;
        }

        // XOR characters of keyStr (as bytes) into the key array to distribute entropy
        byte[] keyStringBytes = keyStr.getBytes(StandardCharsets.UTF_8); // Convert string to bytes once with UTF-8
        for (i = 0, j = 0; i < keyStringBytes.length; ++i, j = (j + 1) % key.length) {
            key[j] ^= keyStringBytes[i];
        }

        return key;
    }

    /**
     * Sets the cryptographic key for the cipher.
     *
     * @param key The key as a byte array.
     */
    protected abstract void setKey(byte[] key);

    /**
     * Utility routine to set the key from a string.
     * Internally calls makeKey to convert the string to a byte array,
     * then delegates to the abstract setKey(byte[] key) method.
     *
     * @param keyStr The key as a string.
     */
    protected void setKey(String keyStr) {
        setKey(makeKey(keyStr));
    }

    /**
     * Returns the name of the encryption algorithm.
     *
     * @return The name of the algorithm.
     */
    public String getName() {
        return name;
    }
}