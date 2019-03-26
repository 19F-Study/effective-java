package kr._19fstudy.effective_java.ch5.item26.generic_with_wildcard;

import java.util.HashSet;
import java.util.Set;

public class GenericWithWildCard {

	public static void main(String[] args) {
		Set<Integer> s1 = new HashSet();
		Set<Integer> s2 = new HashSet();

		s1.add(1);
		s1.add(3); //both s1 and s2
		s1.add(5); //both s1 and s2
		s1.add(7); //both s1 and s2
		s1.add(9);

		s2.add(2);
		s2.add(3); //both s1 and s2
		s2.add(5); //both s1 and s2
		s2.add(7); //both s1 and s2

		System.out.println(numElementsInCommon(s1, s2));
		System.out.println(numElementsInCommonWithWildCard(s1, s2));

		Set<String> stringSet = new HashSet<>();
		stringSet.add("WOONGS");

		numElementsInCommonWithWildCard(stringSet, stringSet);

		Set<?> wildCardSet = new HashSet<>();
		//왜 아무것도 못들어가지
		//제네릭의 타입 안정성을 위해서 막은것인가!
//		wildCardSet.add("STRING");
//		wildCardSet.add(0);


	}

	static int numElementsInCommon(Set s1, Set s2) {
		int result = 0;
		for (Object o1 : s1) {
			if (s2.contains(o1)) {
				result++;
			}
		}
		return result;
	}


	static int numElementsInCommonWithWildCard(Set<?> s1, Set<?> s2) {
		int result = 0;
		for (Object o1 : s1) {
			if (s2.contains(o1)) {
				result++;
			}
		}
		return result;
	}

	static <T> int ex(Set<T> s1, Set<T> s2) {
		int result = 0;
		for (Object o1 : s1) {
			if (s2.contains(o1)) {
				result++;
			}
		}
		return result;
	}
}
