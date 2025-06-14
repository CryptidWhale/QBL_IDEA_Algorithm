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
import java.util.ArrayList;
import java.util.Arrays; // Added for array utility (e.g., Arrays.copyOfRange)
import java.util.List;

/**
 * Implementation for the IDEA encryption algorithm.
 *
 * @author Tolnai Andrei Ciprian (Modified by Greg for better padding and encoding)
 *
 */
public class IDEA extends Encryption {

    /**
     * Constructor, string key.
     *
     * @param keyStr
     */
    public IDEA(String keyStr) {
        this.name = IDEA.class.getSimpleName();
        keySize = 16;
        blockSize = 8; // IDEA operates on 8-byte (64-bit) blocks
        setKey(keyStr);
    }

    // Removed charToAdd - we'll use a proper padding scheme now!

    @Override
    public String decrypt(String text) {
        // Always specify character encoding!
        // The input 'text' here is the *encrypted* string, which likely contains
        // non-printable characters. Using getBytes() on it directly without
        // knowing its original byte representation is risky.
        // It's better to assume the encrypted string is base64 or hex encoded,
        // or that it represents the raw byte data directly.
        // For this example, I'm assuming the 'encrypted' string, when converted
        // to bytes, directly represents the raw ciphertext blocks.
        // In a real system, you'd typically base64 encode the ciphertext to make it
        // safely transmittable as a string.

        byte[] encryptedBytes = text.getBytes(StandardCharsets.ISO_8859_1); // Using ISO_8859_1 to preserve raw byte values if they are non-ASCII

        // We need to ensure encryptedBytes length is a multiple of blockSize.
        // If the original encryption padded correctly, this should already be the case.
        if (encryptedBytes.length % blockSize != 0) {
            System.err.println("Warning: Encrypted text length is not a multiple of block size. Decryption may fail or be incorrect.");
            // For a robust system, you might throw an exception here.
            // For now, we'll proceed but acknowledge the potential issue.
        }

        List<byte[]> encryptedBlockList = new ArrayList<>();
        for (int i = 0; i < encryptedBytes.length; i += blockSize) {
            encryptedBlockList.add(Arrays.copyOfRange(encryptedBytes, i, i + blockSize));
        }

        // Use a ByteBuffer or similar for more efficient byte array concatenation if dealing with very large files.
        // For strings, a byte[] list and then final concatenation is often fine.
        byte[] decryptedConcatenatedBytes = new byte[encryptedBytes.length];
        int currentOffset = 0;

        for (byte[] encryptedBlock : encryptedBlockList) {
            byte[] decryptedBlock = new byte[blockSize]; // Each block is 8 bytes
            decrypt(encryptedBlock, 0, decryptedBlock, 0); // Call the byte-level decrypt method
            System.arraycopy(decryptedBlock, 0, decryptedConcatenatedBytes, currentOffset, blockSize);
            currentOffset += blockSize;
        }

        // Now, remove the PKCS#7 padding
        int paddingLength = decryptedConcatenatedBytes[decryptedConcatenatedBytes.length - 1];
        if (paddingLength > blockSize || paddingLength <= 0) {
            // This indicates invalid padding. Could be corruption or incorrect algorithm.
            System.err.println("Warning: Invalid padding detected during decryption. Returning raw decrypted bytes.");
            // In a real application, you'd likely throw a BadPaddingException here.
            return new String(decryptedConcatenatedBytes, StandardCharsets.UTF_8); // Return as is, or throw
        }

        // Verify padding bytes are consistent
        boolean validPadding = true;
        for (int i = 0; i < paddingLength; i++) {
            if (decryptedConcatenatedBytes[decryptedConcatenatedBytes.length - 1 - i] != paddingLength) {
                validPadding = false;
                break;
            }
        }

        if (!validPadding) {
            System.err.println("Warning: Padding bytes are inconsistent. Returning raw decrypted bytes.");
            return new String(decryptedConcatenatedBytes, StandardCharsets.UTF_8); // Return as is, or throw
        }

        // Trim the padding
        byte[] finalDecryptedBytes = Arrays.copyOfRange(decryptedConcatenatedBytes, 0, decryptedConcatenatedBytes.length - paddingLength);

        // Convert the final decrypted bytes back to a String using a consistent encoding
        return new String(finalDecryptedBytes, StandardCharsets.UTF_8);
    }

    @Override
    public String encrypt(String text) {
        if (text == null || text.isEmpty()) {
            return ""; // Or throw IllegalArgumentException
        }

        // Always work with bytes and specify character encoding!
        byte[] originalBytes = text.getBytes(StandardCharsets.UTF_8);

        // Implement PKCS#7 padding
        int paddingNeeded = blockSize - (originalBytes.length % blockSize);
        if (paddingNeeded == 0) { // If it's already a multiple, we still add a full block of padding
            paddingNeeded = blockSize;
        }

        byte[] paddedBytes = Arrays.copyOf(originalBytes, originalBytes.length + paddingNeeded);
        for (int i = 0; i < paddingNeeded; i++) {
            paddedBytes[originalBytes.length + i] = (byte) paddingNeeded;
        }

        List<byte[]> clearTextBlockList = new ArrayList<>();
        for (int i = 0; i < paddedBytes.length; i += blockSize) {
            clearTextBlockList.add(Arrays.copyOfRange(paddedBytes, i, i + blockSize));
        }

        // Use a ByteBuffer or similar for more efficient byte array concatenation if dealing with very large files.
        byte[] encryptedConcatenatedBytes = new byte[paddedBytes.length];
        int currentOffset = 0;

        for (byte[] clearTextBlock : clearTextBlockList) {
            byte[] encryptedBlock = new byte[blockSize]; // Each block is 8 bytes
            encrypt(clearTextBlock, 0, encryptedBlock, 0); // Call the byte-level encrypt method
            System.arraycopy(encryptedBlock, 0, encryptedConcatenatedBytes, currentOffset, blockSize);
            currentOffset += blockSize;
        }

        // Convert the final encrypted bytes back to a String.
        // For encrypted bytes, it's common to use ISO_8859_1 (Latin-1) as it maps
        // bytes 0-255 directly to Unicode code points U+0000 to U+00FF,
        // effectively preserving the raw byte values without modification.
        // In a real application, you would often Base64 encode this byte array
        // to ensure it's safe for text transmission (e.g., JSON, URL, XML).
        return new String(encryptedConcatenatedBytes, StandardCharsets.ISO_8859_1);
    }

    /**
     * Key routines
     */
    private int[] encryptKeys = new int[52];
    private int[] decryptKeys = new int[52];

    /**
     * Block encryption routines.
     */
    private int[] tempShorts = new int[4]; // 4 shorts = 8 bytes

    /**
     * Encrypt a block of eight bytes.
     *
     * @param clearText
     * @param clearOff
     * @param cipherText
     * @param cipherOff
     */
    private void encrypt(byte[] clearText, int clearOff, byte[] cipherText, int cipherOff) {
        // Ensure input arrays are large enough
        if (clearText.length < clearOff + blockSize || cipherText.length < cipherOff + blockSize) {
            throw new IllegalArgumentException("Input or output buffer too small for block encryption.");
        }
        squashBytesToShorts(clearText, clearOff, tempShorts, 0, 4); // 4 shorts = 8 bytes
        idea(tempShorts, tempShorts, encryptKeys);
        spreadShortsToBytes(tempShorts, 0, cipherText, cipherOff, 4); // 4 shorts = 8 bytes
    }

    /**
     * Decrypt a block of eight bytes.
     *
     * @param cipherText
     * @param cipherOff
     * @param clearText
     * @param clearOff
     */
    private void decrypt(byte[] cipherText, int cipherOff, byte[] clearText, int clearOff) {
        // Ensure input arrays are large enough
        if (cipherText.length < cipherOff + blockSize || clearText.length < clearOff + blockSize) {
            throw new IllegalArgumentException("Input or output buffer too small for block decryption.");
        }
        squashBytesToShorts(cipherText, cipherOff, tempShorts, 0, 4); // 4 shorts = 8 bytes
        idea(tempShorts, tempShorts, decryptKeys);
        spreadShortsToBytes(tempShorts, 0, clearText, clearOff, 4); // 4 shorts = 8 bytes
    }

    /**
     * Set the key.
     *
     * @param key
     */
    @Override // This override annotation is good practice
    protected void setKey(byte[] key) {
        int k1, k2, j;
        int t1, t2, t3;

        // Encryption keys. The first 8 key values come from the 16
        // user-supplied key bytes.
        // Note: key is a byte[16], so 2 * k1 goes up to 14.
        for (k1 = 0; k1 < 8; ++k1) {
            encryptKeys[k1] = ((key[2 * k1] & 0xff) << 8) | (key[2 * k1 + 1] & 0xff);
        }

        // Subsequent key values are the previous values rotated to the
        // left by 25 bits (9 bits left, 7 bits right from a 16-bit word)
        // 0xffff ensures it stays within 16 bits.
        for (; k1 < 52; ++k1) {
            encryptKeys[k1] = ((encryptKeys[k1 - 8] << 9) | (encryptKeys[k1 - 7] >>> 7)) & 0xffff;
        }

        // Decryption keys. These are the encryption keys, inverted and
        // in reverse order.
        k1 = 0;
        k2 = 51;
        // First 4 keys (Round 9 output transformation and Round 8 last key)
        t1 = mulinv(encryptKeys[k1++]); // Z1 * X1 -> Z1^-1 * X1
        t2 = -encryptKeys[k1++];       // Z2 + X2 -> -Z2 + X2
        t3 = -encryptKeys[k1++];       // Z3 + X3 -> -Z3 + X3
        decryptKeys[k2--] = mulinv(encryptKeys[k1++]); // Z4 * X4 -> Z4^-1 * X4
        decryptKeys[k2--] = t3;
        decryptKeys[k2--] = t2;
        decryptKeys[k2--] = t1;

        // Rounds 1-8 decryption keys
        for (j = 1; j < 8; ++j) {
            t1 = encryptKeys[k1++]; // Z5 (for next round input transformation)
            decryptKeys[k2--] = encryptKeys[k1++]; // Z6
            decryptKeys[k2--] = t1; // Z5

            t1 = mulinv(encryptKeys[k1++]); // Z1
            t2 = -encryptKeys[k1++];       // Z2
            t3 = -encryptKeys[k1++];       // Z3
            decryptKeys[k2--] = mulinv(encryptKeys[k1++]); // Z4
            decryptKeys[k2--] = t2;
            decryptKeys[k2--] = t3;
            decryptKeys[k2--] = t1;
        }
        // Last 4 keys (Round 0 output transformation - which is Round 1 input inverse)
        t1 = encryptKeys[k1++];
        decryptKeys[k2--] = encryptKeys[k1++];
        decryptKeys[k2--] = t1;
        t1 = mulinv(encryptKeys[k1++]);
        t2 = -encryptKeys[k1++];
        t3 = -encryptKeys[k1++];
        decryptKeys[k2--] = mulinv(encryptKeys[k1++]);
        decryptKeys[k2--] = t3;
        decryptKeys[k2--] = t2;
        decryptKeys[k2--] = t1;
    }

    /**
     * Run IDEA on one block (8 bytes).
     *
     * @param inShorts  Input block (4 shorts representing 8 bytes).
     * @param outShorts Output block (4 shorts representing 8 bytes).
     * @param keys      The array of round keys (either encryptKeys or decryptKeys).
     */
    private void idea(int[] inShorts, int[] outShorts, int[] keys) {
        int x1, x2, x3, x4, k, t1, t2;

        x1 = inShorts[0];
        x2 = inShorts[1];
        x3 = inShorts[2];
        x4 = inShorts[3];
        k = 0; // Key index

        for (int round = 0; round < 8; ++round) { // 8 full rounds
            x1 = multiplicationModulo65537(x1 & 0xffff, keys[k++]); // Multiply x1 with K1
            x2 = (x2 + keys[k++]) & 0xffff;                         // Add x2 with K2
            x3 = (x3 + keys[k++]) & 0xffff;                         // Add x3 with K3
            x4 = multiplicationModulo65537(x4 & 0xffff, keys[k++]); // Multiply x4 with K4

            t2 = x1 ^ x3;                                           // XOR x1 and x3
            t2 = multiplicationModulo65537(t2 & 0xffff, keys[k++]); // Multiply t2 with K5
            t1 = (t2 + (x2 ^ x4)) & 0xffff;                         // Add t2 with (x2 XOR x4), then modulo 2^16
            t1 = multiplicationModulo65537(t1 & 0xffff, keys[k++]); // Multiply t1 with K6

            t2 = (t1 + t2) & 0xffff;                                // Add t1 and t2, then modulo 2^16

            x1 ^= t1;                                               // XOR x1 with t1
            x4 ^= t2;                                               // XOR x4 with t2
            // Swap x2 and x3 for the next round (except for the last round)
            // But here, t2 becomes new x2, x3 becomes new x3
            // The original IDEA paper has specific swaps:
            // (x1', x2', x3', x4') = (x1 ^ t1, x3 ^ t1, x2 ^ t2, x4 ^ t2) if not last round
            // or (x1 ^ t1, x2 ^ t2, x3 ^ t1, x4 ^ t2) is also a way to think of it depending on specific implementation
            // Let's re-verify the swap for clarity.
            // Original IDEA: (X1', X2', X3', X4') = (X1^T1, X3^T1, X2^T2, X4^T2)
            // The next line effectively performs X2' = X3 ^ T1, X3' = X2 ^ T2
            int next_x2 = x3 ^ t1;
            int next_x3 = x2 ^ t2;
            x2 = next_x2;
            x3 = next_x3;
        }

        // Final output transformation (after 8 rounds, use the last 4 keys)
        outShorts[0] = multiplicationModulo65537(x1 & 0xffff, keys[k++]) & 0xffff;
        outShorts[1] = (x3 + keys[k++]) & 0xffff; // Note: x2 and x3 are swapped in final transformation compared to rounds
        outShorts[2] = (x2 + keys[k++]) & 0xffff; // This swap logic is critical for IDEA
        outShorts[3] = multiplicationModulo65537(x4 & 0xffff, keys[k++]) & 0xffff;
    }

    /**
     * Multiplication modulo 65537.
     *
     * @param a
     * @param b
     * @return
     */
    private static int multiplicationModulo65537(int a, int b) {
        // Ensure inputs are 16-bit
        a &= 0xffff;
        b &= 0xffff;

        if (a == 0) { // 0x0000 in Z_65537* is 0x10000 (65536) in usual arithmetic
            a = 0x10000;
        }
        if (b == 0) {
            b = 0x10000;
        }

        long ab = (long) a * b; // Use long to prevent overflow before modulo
        long r = ab % 65537L;

        return (int) r;
    }

    /**
     * The multiplicative inverse of x, modulo 65537. Uses Euclid's GCD
     * algorithm. It is unrolled twice to avoid swapping the meaning of the
     * registers each iteration, and some subtracts of t have been changed to
     * adds.
     *
     * @param x
     * @return
     */
    private static int mulinv(int x) {
        int t0, t1, q, y;
        if (x <= 1) { // 0 and 1 are self-inverse for Z_65537* (where 0 maps to 65536)
            return x; // For 0, it's 0x10000, for 1, it's 1
        }

        // Euclid's Extended GCD algorithm for (a, m) where m = 65537 (prime)
        // We want a * x + m * y = gcd(a, m) = 1
        // x is the inverse of a modulo m
        t0 = 1; // Represents coefficient for m (initially 1)
        t1 = 0; // Represents coefficient for a (initially 0)

        int modulus = 65537;
        int current_a = modulus;
        int current_b = x; // The number we want inverse of

        // Loop until current_b becomes 0 or 1
        // The original code's unrolling makes it slightly less readable
        // I'll re-implement based on standard extended Euclidean algorithm
        // for clarity, then you can optimize if needed.

        // Initialize variables for extended Euclidean algorithm
        // r_i = q_i * r_{i+1} + r_{i+2}
        // x_i = x_{i-2} - q_{i-1} * x_{i-1}
        // y_i = y_{i-2} - q_{i-1} * y_{i-1}

        int r0 = modulus;
        int r1 = x;
        int x0 = 1;
        int x1 = 0;
        int y0 = 0;
        int y1 = 1;

        while (r1 != 0) {
            q = r0 / r1; // Quotient
            int temp_r = r0 % r1;
            r0 = r1;
            r1 = temp_r;

            int temp_x = x0 - q * x1;
            x0 = x1;
            x1 = temp_x;

            int temp_y = y0 - q * y1;
            y0 = y1;
            y1 = temp_y;
        }

        // At this point, r0 is GCD, and y0 is the inverse.
        // We need to ensure the result is positive and within 16 bits.
        // If x was 0x0000 (interpreted as 0x10000), its inverse is 0x10000.
        // The algorithm usually gives a negative inverse for some cases,
        // so we need to adjust it to be in the positive range [1, 65536].
        return (y0 + modulus) % modulus; // Ensure it's positive and within range
    }
}