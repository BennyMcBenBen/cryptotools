package cryptotools;
public class ModularExponentiator {
	public static int computeModExp(int b, int e, int m) {
		int result = 1;

		while (e > 0) {
			if ((e & 1) == 1) {
				result = (result * b) % m;
			}
			e >>= 1;
			b = (b * b) % m;
		}

		return result;
	}
	
	public static void main(String[] args) {
		int me = computeModExp(2, 31, 65537);
		System.out.println(me);
	}
}
