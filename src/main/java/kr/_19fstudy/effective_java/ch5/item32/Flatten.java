package kr._19fstudy.effective_java.ch5.item32;

import java.util.ArrayList;
import java.util.List;

public class Flatten {

	public static void main(String[] args) {
//		List<Object> result = flatten(List.of("WOONGS"), List.of(1));

//		List<String> result = flatten2(List.of(List.of(1), List.of("WOONGS")));
//		List<Integer> result2 = flatten2(List.of(List.of(1), List.of("WOONGS")));
		System.out.println(flatten2(List.of(List.of(1), List.of("WOONGS"))).getClass().getName());
		//꺼내서 쓸때 문제가 생길 가능성이 있다.!
		//List<Object> 와 List<String> 은 상속관계가 없다고 했는데 아래 예제는 왜 컴파일 에러가 안나지.
		//List<E> 를 반환할때는 List 의 원소에 대한 상속 관계는 적용 되는 것인가..
		List<Object> result3 = flatten2(List.of(List.of(1), List.of("WOONGS")));


		List<String> strings = example("WOONGS");
		System.out.println(strings.get(0).getClass().getName());
		List<Object> objects = example("WOONGS");
		System.out.println(objects.get(0).getClass().getName());

	}

	@SafeVarargs
	static <T> List<T> flatten(List<? extends T>... lists) {
		List<T> result = new ArrayList<>();
		for (List<? extends T> list : lists)
			result.addAll(list);
		return result;
	}

	static <T> List<T> flatten2(List<List<? extends T>> lists) {
		List<T> result = new ArrayList<>();
		for (List<? extends T> list : lists)
			result.addAll(list);
		return result;
	}

	static <E> List<E> example(E e) {
		List<E> a = new ArrayList<>();
		a.add(e);
		System.out.println("EXAMPLE : " + e.getClass().getName());
		return a;
	}
}
