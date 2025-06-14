/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Encryption;

/**
 *
 * @author User
 */
public class Utils {

    /**
     * Converts an array of bytes into an array of 16-bit integers (shorts).
     * Assumes Big-Endian byte order (most significant byte first).
     * E.g., byte[0], byte[1] combine to form outShorts[0], where byte[0] is the MSB.
     *
     * @param inBytes The input byte array.
     * @param inOff The offset in the input byte array to start reading from.
     * @param outShorts The output integer array (will hold 16-bit values).
     * @param outOff The offset in the output integer array to start writing to.
     * @param shortLen The number of 16-bit integers (shorts) to process.
     */
    protected static void squashBytesToShorts(byte[] inBytes, int inOff, int[] outShorts, int outOff, int shortLen) {
        for (int i = 0; i < shortLen; ++i) {
            // (byte[i*2] << 8) is the MSB, (byte[i*2+1]) is the LSB
            outShorts[outOff + i] = ((inBytes[inOff + i * 2] & 0xff) << 8) | (inBytes[inOff + i * 2 + 1] & 0xff);
        }
    }

    /**
     * Converts an array of 16-bit integers (shorts) into an array of bytes.
     * Assumes Big-Endian byte order (most significant byte first).
     * E.g., inShorts[0] will be split into outBytes[0] (MSB) and outBytes[1] (LSB).
     *
     * @param inShorts The input integer array (containing 16-bit values).
     * @param inOff The offset in the input integer array to start reading from.
     * @param outBytes The output byte array.
     * @param outOff The offset in the output byte array to start writing to.
     * @param shortLen The number of 16-bit integers (shorts) to process.
     */
    protected static void spreadShortsToBytes(int[] inShorts, int inOff, byte[] outBytes, int outOff, int shortLen) {
        for (int i = 0; i < shortLen; ++i) {
            outBytes[outOff + i * 2] = (byte) ((inShorts[inOff + i] >>> 8) & 0xff); // MSB
            outBytes[outOff + i * 2 + 1] = (byte) ((inShorts[inOff + i]) & 0xff);   // LSB
        }
    }

    /**
     * Converts an array of bytes into an array of 16-bit integers (shorts).
     * Assumes Little-Endian byte order (least significant byte first).
     * E.g., byte[0], byte[1] combine to form outShorts[0], where byte[0] is the LSB.
     *
     * @param inBytes The input byte array.
     * @param inOff The offset in the input byte array to start reading from.
     * @param outShorts The output integer array (will hold 16-bit values).
     * @param outOff The offset in the output integer array to start writing to.
     * @param shortLen The number of 16-bit integers (shorts) to process.
     */
    protected static void squashBytesToShortsLittle(byte[] inBytes, int inOff, int[] outShorts, int outOff, int shortLen) {
        for (int i = 0; i < shortLen; ++i) {
            // (byte[i*2]) is the LSB, (byte[i*2+1] << 8) is the MSB
            outShorts[outOff + i] = ((inBytes[inOff + i * 2] & 0xff)) | ((inBytes[inOff + i * 2 + 1] & 0xff) << 8);
        }
    }

    /**
     * Converts an array of 16-bit integers (shorts) into an array of bytes.
     * Assumes Little-Endian byte order (least significant byte first).
     * E.g., inShorts[0] will be split into outBytes[0] (LSB) and outBytes[1] (MSB).
     *
     * @param inShorts The input integer array (containing 16-bit values).
     * @param inOff The offset in the input integer array to start reading from.
     * @param outBytes The output byte array.
     * @param outOff The offset in the output byte array to start writing to.
     * @param shortLen The number of 16-bit integers (shorts) to process.
     */
    protected static void spreadShortsToBytesLittle(int[] inShorts, int inOff, byte[] outBytes, int outOff, int shortLen) {
        for (int i = 0; i < shortLen; ++i) {
            outBytes[outOff + i * 2] = (byte) ((inShorts[inOff + i]) & 0xff);       // LSB
            outBytes[outOff + i * 2 + 1] = (byte) ((inShorts[inOff + i] >>> 8) & 0xff); // MSB
        }
    }

    /**
     * Converts an array of bytes into an array of 32-bit integers.
     * Assumes Big-Endian byte order.
     *
     * @param inBytes The input byte array.
     * @param inOff The offset in the input byte array.
     * @param outInts The output integer array.
     * @param outOff The offset in the output integer array.
     * @param intLen The number of 32-bit integers to process.
     */
    public static void squashBytesToInts(byte[] inBytes, int inOff, int[] outInts, int outOff, int intLen) {
        for (int i = 0; i < intLen; ++i) {
            outInts[outOff + i] = ((inBytes[inOff + i * 4] & 0xff) << 24) | ((inBytes[inOff + i * 4 + 1] & 0xff) << 16)
                    | ((inBytes[inOff + i * 4 + 2] & 0xff) << 8) | ((inBytes[inOff + i * 4 + 3] & 0xff));
        }
    }

    /**
     * Converts an array of 32-bit integers into an array of bytes.
     * Assumes Big-Endian byte order.
     *
     * @param inInts The input integer array.
     * @param inOff The offset in the input integer array.
     * @param outBytes The output byte array.
     * @param outOff The offset in the output byte array.
     * @param intLen The number of 32-bit integers to process.
     */
    public static void spreadIntsToBytes(int[] inInts, int inOff, byte[] outBytes, int outOff, int intLen) {
        for (int i = 0; i < intLen; ++i) {
            outBytes[outOff + i * 4] = (byte) ((inInts[inOff + i] >>> 24) & 0xff);
            outBytes[outOff + i * 4 + 1] = (byte) ((inInts[inOff + i] >>> 16) & 0xff);
            outBytes[outOff + i * 4 + 2] = (byte) ((inInts[inOff + i] >>> 8) & 0xff);
            outBytes[outOff + i * 4 + 3] = (byte) ((inInts[inOff + i]) & 0xff);
        }
    }

    /**
     * Tests if a number is even using bitwise AND.
     *
     * @param x The long number to test.
     * @return True if the number is even, false otherwise.
     */
    public static boolean even(long x) {
        return (x & 1) == 0;
    }

    /**
     * Tests if a number is odd using bitwise AND.
     *
     * @param x The long number to test.
     * @return True if the number is odd, false otherwise.
     */
    public static boolean odd(long x) {
        return (x & 1) != 0;
    }

    /**
     * Counts the number of 1-bits (set bits) in a byte.
     *
     * @param x The byte to analyze.
     * @return The count of set bits.
     */
    public static int countOnes(byte x) {
        return countOnes(x & 0xffL); // Promote to long to avoid sign extension issues
    }

    /**
     * Counts the number of 1-bits (set bits) in an int.
     *
     * @param x The int to analyze.
     * @return The count of set bits.
     */
    public static int countOnes(int x) {
        return countOnes(x & 0xffffffffL); // Promote to long to handle unsigned int behavior
        // Alternative for modern Java (Java 8+): return Integer.bitCount(x);
    }

    /**
     * Counts the number of 1-bits (set bits) in a long.
     *
     * @param x The long to analyze.
     * @return The count of set bits.
     */
    public static int countOnes(long x) {
        int count = 0;
        while (x != 0) {
            if (odd(x)) {
                ++count;
            }
            x >>>= 1; // Unsigned right shift
        }
        return count;
        // Alternative for modern Java (Java 8+): return Long.bitCount(x);
    }
}