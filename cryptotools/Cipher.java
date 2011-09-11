package cryptotools;

public interface Cipher {
	public String encrypt(final String plaintext);
	public String decrypt(final String ciphertext);
}
