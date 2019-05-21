package kr._19fstudy.effective_java.ch11.item78;

import java.util.concurrent.TimeUnit;

public class ThreadStopExample {
	private static boolean stopRequested;

	private static synchronized void requestStop() {
		stopRequested = true;
	}

	private static synchronized boolean stopRequested() {
		return stopRequested;
	}

	public static void main(String[] args) throws InterruptedException {

		Thread backgroundThread = new Thread(() -> {
			int i = 0;
			while (!stopRequested()) {
				i++;
			}
			System.out.println("EXITED!");
		});
		backgroundThread.start();

		TimeUnit.SECONDS.sleep(1);

		requestStop();
	}
}
