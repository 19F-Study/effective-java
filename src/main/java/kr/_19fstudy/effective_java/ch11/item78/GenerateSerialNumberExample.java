package kr._19fstudy.effective_java.ch11.item78;

public class GenerateSerialNumberExample {

	private static volatile int nextSerialNumber = 0;

	public static void main(String[] args) {

	}

	public static int generateSerialNumber() {
		return nextSerialNumber++;
	}
}
