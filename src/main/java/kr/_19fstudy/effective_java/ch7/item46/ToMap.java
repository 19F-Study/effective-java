package kr._19fstudy.effective_java.ch7.item46;

import kr._19fstudy.effective_java.ch7.item42.Operation;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToMap {
	private static final Map<String, Operation> stringToEnum = Stream.of(Operation.values())
		.collect(Collectors.toMap(Operation::toString, e -> e));

	public static void main(String[] args) {

		System.out.println(stringToEnum.values().stream().collect(Collectors.toList()));
		System.out.println(stringToEnum);
	}


}
