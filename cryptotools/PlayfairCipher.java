package cryptotools;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Playfair cipher: When creating the table, follows the omit Q rule (rather
 * than combine i and j).
 * 
 * @author Ben Zoller
 * 
 */
public class PlayfairCipher implements Cipher {
	// TODO: implement other rules (combine i and j by replacing j with i)
	// TODO: implement other filler characters (take as input in constructor)
	// TODO: get rid of whitespace for the user (better implemented in UI?)
	// yes - because want to change input text area - not outputted by
	// background classes
	// TODO: add final where possible

	private static class Index {
		final int x, y;

		public Index(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	private static final Pattern PLAINTEXT_PATTERN = Pattern.compile("[a-z]+");

	private static final Pattern CIPHERTEXT_PATTERN = Pattern.compile("[A-Z]+");

	private static final char[] LOWERCASE_ALPHABET = { 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z' }; // note q missing

	private HashSet<Character> usedCharacters;

	private char[][] table = new char[5][5];

	private HashMap<Character, Index> tableMap;

	public PlayfairCipher(String key) {
		// check that key only contains letters
		key = key.trim();
		final Matcher plaintextMatcher = PLAINTEXT_PATTERN.matcher(key);
		if (!plaintextMatcher.matches()) {
			throw new IllegalArgumentException(
					"Key must contain only a string of characters within [a-z]");
		}

		LinkedList<Character> newKey = removeDuplicateCharacters(key);
		LinkedList<Character> unusedCharacters = getUnusedCharacters();

		initializeTable(newKey, unusedCharacters);
	}

	private LinkedList<Character> removeDuplicateCharacters(final String key) {
		// remove 'q' and duplicate letters from key
		LinkedList<Character> newKey = new LinkedList<Character>();
		usedCharacters = new HashSet<Character>();
		usedCharacters.add('q');
		char[] keyCharArray = key.toCharArray();
		for (char c : keyCharArray) {
			if (!usedCharacters.contains(c)) {
				// add character to the HashSet
				usedCharacters.add(c);

				// add it to the new key
				newKey.add(c);
			}
		}
		return newKey;
	}

	private LinkedList<Character> getUnusedCharacters() {
		LinkedList<Character> unusedCharacters = new LinkedList<Character>();
		for (char c : LOWERCASE_ALPHABET) {
			if (!usedCharacters.contains(c)) {
				unusedCharacters.add(c);
			}
		}
		return unusedCharacters;
	}

	private void initializeTable(LinkedList<Character> newKey,
			LinkedList<Character> unusedCharacters) {
		// initialize table
		// first add key with duplicates removed
		tableMap = new HashMap<Character, Index>(26);
		int i = 0, j = 0;
		while (!newKey.isEmpty()) {
			char c = newKey.removeFirst();
			table[i][j] = c;
			tableMap.put(c, new Index(i, j));

			// increment
			i++;
			if (i > 4) {
				i -= 5;
				j++;
			}
		}

		// add unused characters in alphabetical order
		while (!unusedCharacters.isEmpty()) {
			char c = unusedCharacters.removeFirst();
			table[i][j] = c;
			tableMap.put(c, new Index(i, j));

			// increment
			i++;
			if (i > 4) {
				i -= 5;
				j++;
			}
		}
	}

	public String getTableString() {
		StringBuilder tableString = new StringBuilder();
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < 5; i++) {
				tableString.append(table[i][j] + " ");
			}
			tableString.append("\n");
		}
		return tableString.toString();
	}

	public String encrypt(final String plaintext) {
		// check for correct input
		final Matcher plaintextMatcher = PLAINTEXT_PATTERN.matcher(plaintext);
		if (!plaintextMatcher.matches()) {
			throw new IllegalArgumentException("Plaintext must contain only a "
					+ "string of characters within [a-z] (q's will be removed)");
		}

		// encrypt plaintext
		String text = formatPlaintext(plaintext);

		StringBuilder ciphertext = new StringBuilder();

		for (int i = 0, j = 1; j < text.length(); i += 2, j += 2) {
			Index iIndex = tableMap.get(text.charAt(i));
			Index jIndex = tableMap.get(text.charAt(j));

			if (iIndex.getY() == jIndex.getY()) {
				// if the letters appear on the same row of the table, replace
				// them with the letters to their immediate right respectively
				// (wrapping around to the left side of the row if a letter in
				// the original pair was on the right side of the row)
				int y = iIndex.getY();
				int ix = (iIndex.getX() + 1) % 5;
				int jx = (jIndex.getX() + 1) % 5;
				ciphertext.append(table[ix][y]);
				ciphertext.append(table[jx][y]);
			} else if (iIndex.getX() == jIndex.getX()) {
				// if the letters appear on the same column of your table,
				// replace them with the letters immediately below respectively
				// (wrapping around to the top side of the column if a letter in
				// the original pair was on the bottom side of the column)
				int x = iIndex.getX();
				int iy = (iIndex.getY() + 1) % 5;
				int jy = (jIndex.getY() + 1) % 5;
				ciphertext.append(table[x][iy]);
				ciphertext.append(table[x][jy]);
			} else {
				// if the letters are not on the same row or column, replace
				// them with the letters on the same row respectively but at the
				// other pair of corners of the rectangle defined by the
				// original pair
				int ix = iIndex.getX();
				int iy = iIndex.getY();
				int jx = jIndex.getX();
				int jy = jIndex.getY();
				ciphertext.append(table[jx][iy]);
				ciphertext.append(table[ix][jy]);
			}
		}

		return ciphertext.toString().toUpperCase();
	}

	public String formatPlaintext(String text) {
		// normal plaintext formatting
		text = text.toLowerCase();
		text = text.replaceAll("[^a-z]", "");
		
		// replace all 'q' letter with blank spaces
		// 'q' is not within the 5x5 grid
		text = text.replace("q", "");

		// group plaintext into groups of two letters
		// add 'x' between duplicate groups
		StringBuilder textBuilder = new StringBuilder();
		int i = 0, j = 1;
		for (; j < text.length(); i += 2, j += 2) {
			textBuilder.append(text.charAt(i));

			if (text.charAt(i) == text.charAt(j)) {
				// cannot have same letters in pair
				textBuilder.append('x');
			}

			textBuilder.append(text.charAt(j));
		}

		if (i < text.length()) {
			// if text has an odd length
			textBuilder.append(text.charAt(i));
		}

		if (textBuilder.length() % 2 == 1) {
			// if textBuilder has odd length
			textBuilder.append('x');
		}

		// string is now ready to be encrypted
		return textBuilder.toString();
	}

	public String decrypt(final String ciphertext) {
		// check for correct input
		final Matcher ciphertextMatcher = CIPHERTEXT_PATTERN
				.matcher(ciphertext);
		if (!ciphertextMatcher.matches()) {
			throw new IllegalArgumentException(
					"Ciphertext must contain only a string of characters within [A-Z]");
		}
		if (ciphertext.contains("Q")) {
			throw new IllegalArgumentException("Ciphertext cannot contain 'Q'");
		}
		if (ciphertext.length() % 2 == 1) {
			throw new IllegalArgumentException("Ciphertext must be of even length");
		}
		
		// decrypt ciphertext
		// assuming ciphertext properly formatted
		String text = ciphertext.toLowerCase();
		
		StringBuilder plaintext = new StringBuilder();
		for (int i = 0, j = 1; j < text.length(); i += 2, j += 2) {
			Index iIndex = tableMap.get(text.charAt(i));
			Index jIndex = tableMap.get(text.charAt(j));

			if (iIndex.getY() == jIndex.getY()) {
				// if the letters appear on the same row of the table, replace
				// them with the letters to their immediate right respectively
				// (wrapping around to the left side of the row if a letter in
				// the original pair was on the right side of the row)
				int y = iIndex.getY();
				int ix = (iIndex.getX() - 1) % 5;
				int jx = (jIndex.getX() - 1) % 5;
				plaintext.append(table[ix][y]);
				plaintext.append(table[jx][y]);
			} else if (iIndex.getX() == jIndex.getX()) {
				// if the letters appear on the same column of your table,
				// replace them with the letters immediately below respectively
				// (wrapping around to the top side of the column if a letter in
				// the original pair was on the bottom side of the column)
				int x = iIndex.getX();
				int iy = (iIndex.getY() + 4) % 5;
				int jy = (jIndex.getY() + 4) % 5;
				plaintext.append(table[x][iy]);
				plaintext.append(table[x][jy]);
			} else {
				// if the letters are not on the same row or column, replace
				// them with the letters on the same row respectively but at the
				// other pair of corners of the rectangle defined by the
				// original pair
				int ix = iIndex.getX();
				int iy = iIndex.getY();
				int jx = jIndex.getX();
				int jy = jIndex.getY();
				plaintext.append(table[jx][iy]);
				plaintext.append(table[ix][jy]);
			}
		}
		return plaintext.toString();
	}

	public static void main(String[] args) {
		PlayfairCipher pc = new PlayfairCipher("nancy");
		System.out.print(pc.getTableString());
		String ciphertext = pc.encrypt("iloveyou");
		System.out.println(ciphertext);
		String plaintext = pc.decrypt(ciphertext);
		System.out.println(plaintext);
	}
}
