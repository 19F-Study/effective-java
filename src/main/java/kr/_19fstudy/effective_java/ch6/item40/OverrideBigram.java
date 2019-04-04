package kr._19fstudy.effective_java.ch6.item40;

import java.util.HashSet;
import java.util.Set;

public class OverrideBigram {
	private final char first;
	private final char second;

	public OverrideBigram(char first, char second) {
		this.first = first;
		this.second = second;
	}

	//method does not override or implement a method from a supertype
//	@Override
//	public boolean equals(OverrideBigram b) {
//		return b.first == first && b.second == second;
//	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof OverrideBigram)) {
			return false;
		}
		OverrideBigram b = (OverrideBigram) o;
		return b.first == first && b.second == second;
	}

	@Override
	public int hashCode() {
		return 31 * first + second;
	}

	@Override
	public String toString() {
		return "first : " + first + ", second : " + second;
	}

	public static void main(String[] args) {
		Set<OverrideBigram> s = new HashSet<>();
		// "a ~ z" 넣기를 10번 반복
		for (int i = 0; i < 10; i++) {
			//a ~ z 넣기
			for (char ch = 'a' ; ch <= 'z' ; ch++)
				s.add(new OverrideBigram(ch, ch));
		}
		System.out.println(s.size());
		s.forEach((value) -> {
			System.out.println(value);
		});
	}
}
