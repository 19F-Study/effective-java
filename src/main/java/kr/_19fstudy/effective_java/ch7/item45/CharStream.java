package kr._19fstudy.effective_java.ch7.item45;

public class CharStream {
	public static void main(String[] args) {
		"HELLO WORLD!".chars().forEach(System.out::println);
		"HELLO WORLD!".chars().forEach(x -> System.out.print((char) x));
	}
}
