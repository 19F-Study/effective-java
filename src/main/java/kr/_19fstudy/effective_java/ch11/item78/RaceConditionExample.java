package kr._19fstudy.effective_java.ch11.item78;

import java.util.concurrent.TimeUnit;

public class RaceConditionExample {


	public static volatile int seq = 0;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("WAKE UP");
		Thread threadOne = new Thread(() -> {
			//race condition example
			for (int i = 0; i < 100_000 ; i++) {
				System.out.println(getNextSequence());
//				System.out.println(getSynchronizedSequence());
//				getSynchronizedSequence();
			}

		});
		Thread threadTwo = new Thread(() -> {
			//race condition example
			for (int i = 0; i < 100_000 ; i++)
				System.out.println(getNextSequence());
//				System.out.println(getSynchronizedSequence());
//				getSynchronizedSequence();
		});
		threadOne.start();
		threadTwo.start();
	}

	public static int getNextSequence() {
		return seq++;
	}

	public static synchronized int getSynchronizedSequence() {
		return seq++;
	}


}
