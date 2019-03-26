package kr._19fstudy.effective_java.ch5.item32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class PickTwo {
	public static void main(String[] args) throws Exception {
		//String[] result = pickTwo("GOOD", "FAST", "CHEAP");

		//String[] result2 = toArrayReturnSubArray("WOOGNS");

//		String s = returnOnlyOne("WOONGS");
//		String s2 = returnOnlyOne(1);
//		System.out.println(returnOnlyOne(1).getClass().getName());
	}

	static <T> T[] toArray(T... args) {
		return args;
	}

	static <T> T[] pickTwo(T a, T b, T c) throws Exception {
		switch(ThreadLocalRandom.current().nextInt(3)) {
			case 0: return toArray(a, b);
			case 1: return toArray(a, c);
			case 2: return toArray(b, c);
		}
		throw new Exception();
	}


	//타입이 결졍되기 때문에 컴파일 시점에 타입 문제를 잡을 수 있다.
	static <T> T returnOnlyOne(T... args) {
		return args[0];
	}

	//일부를 리턴하기 위해서 arraycopy 를 사용해도 동일한 ClassCastException 발생. 이 방법이 아닌듯..
	static <T> T[] toArrayReturnSubArray(T... args) {
		T[] newArray = (T[])new Object[1];
		System.arraycopy(args, 0, newArray, 0, 1);
		return newArray;
	}

}
