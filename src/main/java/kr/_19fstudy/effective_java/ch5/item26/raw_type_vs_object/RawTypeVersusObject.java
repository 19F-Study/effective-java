package kr._19fstudy.effective_java.ch5.item26.raw_type_vs_object;

import java.util.ArrayList;
import java.util.List;

public class RawTypeVersusObject {
	public static void main(String[] args) {

		List<String> strings = new ArrayList<>();
		strings.add("STRING");

		List<Integer> integers = new ArrayList<>();
		integers.add(1);

//		1.parameterIsRawType(strings);
//		2.parameterIsGenericObject(strings);
//		3.parameterIsWildCardGeneric(strings);

//		parameterIsRawType(integers);
//		parameterIsGenericObject(integers);
//		parameterIsWildCardGeneric(integers);

	}

	public static void parameterIsRawType(List s) {
		System.out.println("PARAMETER IS RAW TYPE");
	}

	public static void parameterIsGenericObject(List<Object> s) {
		System.out.println("PARAMETER IS OBJECT");
	}

	public static void parameterIsWildCardGeneric(List<?> s) {
		System.out.println("PARAMETER IS WHLDCARD");
	}

}
