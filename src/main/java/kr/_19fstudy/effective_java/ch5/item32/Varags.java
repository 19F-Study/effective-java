package kr._19fstudy.effective_java.ch5.item32;

import java.util.List;

public class Varags {
	public static void main(String[] args) {
//		dangerous();
		dangerous(List.of("WOONGS"), List.of("WOOOO"));
	}

//	static void dangerous() {
//		List<String>[] stringLists = new List<String>[1];
//		List<Integer> intList = List.of(42);
//		Object[] objects = stringLists;
//		objects[0] = intList;
		//형변환이 숨어있다.
//		String s = stringLists[0].get(0);
//	}

	static void dangerous(List<String>... stringLists) {
		List<Integer> intList = List.of(42);
		Object[] objects = stringLists;
		objects[0] = intList;
		//형변환이 숨어있다!
		String s = stringLists[0].get(0);
	}

}
