package kr._19fstudy.effective_java.ch4.item15.NestedPackagePrivate;

public class NestedExample {

	Animal animal;

	//같은 패키지의 다른 클래스가 접근하지 못하도록..
	private static class Animal {

	}
}
