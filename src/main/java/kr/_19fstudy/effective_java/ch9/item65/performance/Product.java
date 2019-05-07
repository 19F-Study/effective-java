package kr._19fstudy.effective_java.ch9.item65.performance;

import org.apache.commons.lang3.time.StopWatch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class Product {
	private int price;
	public Product(int price) {
		this.price = price;
	}

	public int getPrice() {
		return this.price;
	}

	private static final int TEST_COUNT = 10_000_000;
	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		for (int i = 0; i < TEST_COUNT ; i++) {
			byInstance(i);
		}
		stopWatch.stop();
		StopWatch stopWatch2 = new StopWatch();

		stopWatch2.start();

		for (int i = 0; i < TEST_COUNT ; i++) {
			byReflection(i);
		}
		stopWatch2.stop();
		System.out.println(stopWatch.toString());
		System.out.println(stopWatch2.toString());

	}


	public static void byInstance(int i) {
		Product product = new Product(i);
		System.out.println(product.getPrice());
	}

	public static void byReflection(int i) {
		Class<?> cl = null;
		try {
			cl = (Class<? extends Set<String>>) Class.forName("kr._19fstudy.effective_java.ch9.item65.performance.Product");
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}

		Constructor<?> cons = null;
		try {
			cons = cl.getDeclaredConstructor(int.class);
		} catch (NoSuchMethodException e) {
			System.out.println(e);
		}

		Object s = null;
		try {
			s = cons.newInstance(i);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			System.out.println(e);
		}

		try {
			Method method = cl.getMethod("getPrice");
			System.out.println(method.invoke(s));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}


