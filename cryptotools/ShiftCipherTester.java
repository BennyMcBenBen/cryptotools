package cryptotools;

import java.util.Scanner;

public class ShiftCipherTester {
	public static void main(String[] args) {
		System.out.println("Shift Cipher program");

		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter key:");
		int key = Integer.parseInt(scanner.nextLine());

		ShiftCipher shiftCipher = new ShiftCipher(key);

		System.out.println("Enter plaintext:");
		String plaintext = scanner.nextLine();

		System.out.println("Ciphertext:");
		String ciphertext = shiftCipher.encrypt(plaintext);
		System.out.println(ciphertext);

		System.out.println("Plaintext:");
		System.out.println(shiftCipher.decrypt(ciphertext));
	}
}
