package kr._19fstudy.effective_java.ch6.item40;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Bigram {
	private final char first;
	private final char second;

	public Bigram(char first, char second) {
		this.first = first;
		this.second = second;
	}

	//다중정의 됨!
	//HashSet 에서 객체의 equal 여부를 판단하기 위해서 아래 메서드가 호출되지 않았다!
	public boolean equals(Bigram b) {
		return b.first == first && b.second == second;
	}

	public int hashCode() {
		return 31 * first + second;
	}

	//toString 은 Override 하지 않아도 호출된다! 왜?
	public String toString() {
		return "first : " + first + ", second : " + second;
	}

	//size 가 260인 이유는?
	public static void main(String[] args) {

		Set<Bigram> s = new HashSet<>();
		// "a ~ z" 넣기를 10번 반복
		for (int i = 0; i < 10; i++) {
			//a ~ z 넣기
			for (char ch = 'a' ; ch <= 'z' ; ch++)
				s.add(new Bigram(ch, ch));
		}
		System.out.println(s.size());
		s.forEach((value) -> {
			System.out.println(value);
		});
	}
}
