package kr._19fstudy.effective_java.ch11.item78;

import java.util.concurrent.TimeUnit;

public class ThreadCanNotStopExample {

	private static boolean stopRequested;

	public static void main(String[] args) throws InterruptedException {
		Thread backgroundThread = new Thread(() -> {
			int i = 0;
			while (!stopRequested) {
				i++;
				//system out 을 찍으면 종료된다. 왜?
				//System.out.println(i);
			}
			System.out.println("EXITED!");
		});
		backgroundThread.start();

		TimeUnit.SECONDS.sleep(1);

		stopRequested = true;
	}
}
