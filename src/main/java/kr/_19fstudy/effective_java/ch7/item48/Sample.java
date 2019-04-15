package kr._19fstudy.effective_java.ch7.item48;

import java.math.BigInteger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

public class Sample {

    public static void main(String[] args) {

    }

    private static void mersennePrimeNumberExample() {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
//                .parallel()
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println)
        ;
    }

    private static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    private static long calculatePiExample(long n) {
        return LongStream.rangeClosed(2, n)
//                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count()
                ;
    }

}
