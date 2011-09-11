package cryptotools;

/* 
 * @(#)ShiftCipher.java	1.0 Sep 2, 2006
 *
 * COPYRIGHT BEN ZOLLER, 2006
 * ALL RIGHTS RESERVED
 */
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java implementation of the classical Shift Cipher cryptosystem. Every letter
 * in a plaintext message is shifted over <em>k</em> (input key) places to
 * produce ciphertext.
 * <p>
 * Ciphertext is decrypted by shifting each letter back <em>k</em> places to
 * produce plaintext.
 * 
 * @author Ben Zoller
 * @version 1.0
 * 
 */
public class ShiftCipher implements Cipher {

	private static final int ALPHABET_SIZE = 26;

	private static final char[] LOWERCASE_ALPHABET = { 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private static final char[] UPPERCASE_ALPHABET = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static final Pattern PLAINTEXT_PATTERN = Pattern.compile("[a-z]+");

	private static final Pattern CIPHERTEXT_PATTERN = Pattern.compile("[A-Z]+");

	private final HashMap<Character, Character> encryptHashMap = new HashMap<Character, Character>(
			26);

	private final HashMap<Character, Character> decryptHashMap = new HashMap<Character, Character>(
			26);

	private final int key;

	/**
	 * Initializes the shift cipher with a key. Sets up the alphabet array list.
	 * 
	 * @param key
	 *            key used in the shift cipher to encrypt and decrypt messages
	 */
	public ShiftCipher(final int key) {
		// check for correct input
		if (key < 0 || key > 25) {
			throw new IllegalArgumentException("Key must be within [0,25]");
		}

		// initialize the key
		this.key = key;

		// create the hashmap based on the key
		createCryptHashMaps();
	}
	
	public String formatPlaintext(String text) {
		text = text.toLowerCase();
		text = text.replaceAll("[^a-z]", "");
		return text;
	}

	/**
	 * Creates encryption and decryption hash maps based on the key.
	 */
	private void createCryptHashMaps() {
		int shiftIndex = key;
		for (int index = 0; index < ALPHABET_SIZE; index++) {

			// correct shift index
			if (shiftIndex == ALPHABET_SIZE) {
				shiftIndex = 0;
			}

			// create hash maps
			encryptHashMap.put(LOWERCASE_ALPHABET[index],
					UPPERCASE_ALPHABET[shiftIndex]);
			decryptHashMap.put(UPPERCASE_ALPHABET[shiftIndex],
					LOWERCASE_ALPHABET[index]);

			// increment shift index
			shiftIndex++;
		}
	}

	/**
	 * Encrypts plaintext into ciphertext based on a key.
	 * 
	 * @param plaintext
	 *            Plaintext (must match the pattern [a-z]+).
	 * @return Ciphertext (matches the pattern [A-Z]+).
	 */
	public String encrypt(final String plaintext) {
		// check for correct input
		final Matcher plaintextMatcher = PLAINTEXT_PATTERN.matcher(plaintext);
		if (!plaintextMatcher.matches()) {
			throw new IllegalArgumentException(
					"Plaintext must contain only a string of characters within [a-z]");
		}

		// encrypt plaintext
		final StringBuilder ciphertextBuilder = new StringBuilder();
		final char[] plainChars = plaintext.toCharArray();
		for (char plainChar : plainChars) {
			Character cipherChar = encryptHashMap.get(plainChar);
			ciphertextBuilder.append(cipherChar);
		}
		return ciphertextBuilder.toString();
	}

	/**
	 * Decrypts ciphertext into plaintext based on a key.
	 * 
	 * @param ciphertext
	 *            Ciphertext (must match the pattern [A-Z]+).
	 * @return Plaintext (matches the pattern [a-z]+).
	 */
	public String decrypt(final String ciphertext) {
		// check for correct input
		final Matcher ciphertextMatcher = CIPHERTEXT_PATTERN
				.matcher(ciphertext);
		if (!ciphertextMatcher.matches()) {
			throw new IllegalArgumentException(
					"Ciphertext must contain only a string of characters within [A-Z]");
		}

		// decrypt ciphertext
		final StringBuilder plaintextBuilder = new StringBuilder();
		final char[] cipherChars = ciphertext.toCharArray();
		for (char cipherChar : cipherChars) {
			Character plainChar = decryptHashMap.get(cipherChar);
			plaintextBuilder.append(plainChar);
		}
		return plaintextBuilder.toString();
	}
	
	public static void main(String[] args) {
		ShiftCipher sc = new ShiftCipher(5);
		String plaintext = "Goodbye World!";
		plaintext = sc.formatPlaintext(plaintext);
		System.out.println(plaintext);
	}
}