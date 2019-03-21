package kr._19fstudy.effective_java.ch5.item26.raw_type_vs_object;

import java.util.ArrayList;
import java.util.List;

public class UnSafeExample {

	public static void main(String[] args) {
		List<String> strings = new ArrayList<>();
//		unsafeAdd(strings, 0);
//		safeAdd(strings, 0);

	}

	private static void unsafeAdd(List list, Object o) {
		list.add(o);
	}

	private static void safeAdd(List<Object> list, Object o) {
		list.add(o);
	}
}
