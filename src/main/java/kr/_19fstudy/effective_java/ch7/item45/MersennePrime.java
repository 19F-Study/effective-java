package kr._19fstudy.effective_java.ch7.item45;

import java.math.BigInteger;
import java.util.stream.Stream;

public class MersennePrime {
	public static void main(String[] args) {
		//primes() 리턴 값은?
//		primes().forEach(System.out::println);

		//2^p - 1 값을 출력함.
		primes().map(p -> BigInteger.TWO.pow(p.intValueExact()).subtract(BigInteger.ONE))
			.filter(mersenne -> mersenne.isProbablePrime(50))
			.limit(20)
			.forEach(System.out::println);

		//p 의 값을 알고 싶다면?
		primes().map(p -> BigInteger.TWO.pow(p.intValueExact()).subtract(BigInteger.ONE))
			.filter(mersenne -> mersenne.isProbablePrime(50))
			.limit(20)
			.forEach(mp -> System.out.println(mp.bitLength() + ": " + mp));

	}

	private static Stream<BigInteger> primes() {
		return Stream.iterate(BigInteger.TWO, BigInteger::nextProbablePrime);
	}

}
