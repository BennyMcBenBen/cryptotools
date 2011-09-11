package cryptotools;
public class FermatFactorizer {

	public static FermatFactor factor(final int n) {
		int bound = (int) Math.sqrt(n) + 1;
		int x = 1;
		int r, y = 0, z;

		// check for even n
		if (n % 2 == 0) {
			// n is even
			return new FermatFactor(2, n / 2);
			
		} else {
			// n is odd
			
			// this while test can't be right. work it out
			while (y - x < bound) {

				z = n + x * x;
				y = (int) Math.sqrt(z);
				r = z - y * y;

				if (r == 0) {
					// fermat factorization found for n
					return new FermatFactor(y - x, y + x);
				}

				x++;
			}

			// n is prime
			return new FermatFactor(1, n);
		}
	}

	public static void main(String[] args) {
		FermatFactor f = FermatFactorizer.factor(83);
		System.out.println(f);
	}

}
