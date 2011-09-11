package cryptotools;

public interface Cipher {
	String encrypt(String plaintext);

	String decrypt(String ciphertext);
}
