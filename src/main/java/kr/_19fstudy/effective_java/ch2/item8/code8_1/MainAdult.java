package kr._19fstudy.effective_java.ch2.item8.code8_1;

public class MainAdult {
	public static void main(String[] args) throws Exception {
		try (Room myRoom = new Room(7)) {
			System.out.println("안녕~");
		}
	}
}
