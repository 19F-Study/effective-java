package kr._19fstudy.effective_java.ch5.item27;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lecture {

	private int size;

	private Object[] elements;

	public <T> T[] toArray(T[] a) {
		//unchecked cast warning 이 발생하는 이유는?
		if (a.length < size)
			return (T[]) Arrays.copyOf(elements, size, a.getClass());
		System.arraycopy(elements, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}

	public <T> T[] toArrayAfter(T[] a) {
		if (a.length < size) {
			@SuppressWarnings("unchecked")
			T[] result = (T[]) Arrays.copyOf(elements, size, a.getClass());
			return result;
		}
		System.arraycopy(elements, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}
}

//	String[] b = (String[]) Arrays.copyOf(elements, size, a.getClass());
//	Set<T> c = new HashSet<>();
// Object -> T[] 로 Cast 하니까 T[] 를 다시 사용하는 쪽에서 위험할 수 있다. 떄문에 Warning 으로?