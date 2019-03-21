package kr._19fstudy.effective_java.ch4.item15;

import kr._19fstudy.effective_java.ch4.item15.PublicMemberExample.Employee;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;

public class OutsidePackageClient {

	public static void main(String[] args)
		throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Employee employee = new Employee("WOONGS", "HG", true);

		System.out.println(employee);

		byte[] serializedEmployee;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(employee);
				serializedEmployee = baos.toByteArray();
			}
		}

		String className = Employee.class.getName();

		Class cls = Class.forName(className);


		Field fieldlist[] = cls.getDeclaredFields();


		Class myClass = Class.forName(className);

		Constructor myConstuctor = myClass.getConstructor( new Class[] {String.class , String.class, boolean.class });
		Object myObj = myConstuctor.newInstance("WOONGS", "HG", true);
//		Method method = myClass.getMethod("please");
//		method.setAccessible(true);
//		String result = (String) method.invoke(myObj);


		Method[] methods = myClass.getDeclaredMethods();

		methods[4].setAccessible(true);
		methods[4].invoke(myObj);

		Method pleaseMethod = null;
		for (Method m : methods) {
			if ("please".equals(m.getName())) {
				pleaseMethod = m;
			}
		}

		pleaseMethod.setAccessible(true);
		pleaseMethod.invoke(myObj);

		String s1 = Arrays.toString(serializedEmployee);
		String s2 = new String(serializedEmployee);
		System.out.println("-------------------------------------");

		System.out.println(s1);
		System.out.println(s2);

		System.out.println("-------------------------------------");

		System.out.println(Base64.getEncoder().encodeToString(serializedEmployee));


		try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedEmployee)) {
			try (ObjectInputStream ois = new ObjectInputStream(bis)) {
				Employee deserializedEmployee = (Employee) ois.readObject();
				System.out.println(deserializedEmployee);
			}
		}

		//byte stream 의 특정 부분을 수정하면 데이터를 수정할 수 있다. << 이건 Serialization 만의 문제가 아니지 않나?

	}

}
