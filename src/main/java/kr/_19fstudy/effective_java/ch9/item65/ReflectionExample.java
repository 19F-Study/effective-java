package kr._19fstudy.effective_java.ch9.item65;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ReflectionExample {

	public static void main(String[] args) {
		Class<? extends Set<String>> cl = null;
		try {
			cl = (Class<? extends Set<String>>) Class.forName(args[0]);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}

		Constructor<? extends Set<String>> cons = null;
		try {
			cons = cl.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			System.out.println(e);
		}

		Set<String> s = null;
		try {
			s = cons.newInstance();
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			System.out.println(e);
		}

		s.addAll(Arrays.asList(args).subList(1, args.length));
		System.out.println(s);
	}

	//java.util.LinkedHashSet
	//
}
