package cryptotools;

public class GcdFinder {

	public static int computeGcd(int a, int b) {
		if (a < b) {
			// if a < b, swap a & b
			a ^= b;
			b ^= a;
			a ^= b;
		}

		return computeGcdHelper(a, b);
	}

	/**
	 * Assumes a > b.
	 */
	private static int computeGcdHelper(final int a, final int b) {
		int q = a / b;
		int r = a - q * b;
		if (r == 0) {
			return b;
		} else {
			return computeGcdHelper(b, r);
		}
	}

	public static void main(String[] args) {
		int gcd = GcdFinder.computeGcd(117649, 17);
		System.out.println(gcd);
	}
}
