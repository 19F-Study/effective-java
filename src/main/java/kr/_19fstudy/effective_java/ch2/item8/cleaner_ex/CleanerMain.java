package kr._19fstudy.effective_java.ch2.item8.cleaner_ex;

import java.lang.ref.Cleaner;

public class CleanerMain {
	public static void main(String[] args) {
		Cleaner cleaner = Cleaner.create();
		for (int i = 0; i < 10; i++) {
			String id = Integer.toString(i);
			MyObject myObject = new MyObject(id);
			cleaner.register(myObject, new CleanerRunnable(id));
		}

		//myObjects are not reachable anymore
		//do some other memory intensive work
		for (int i = 1; i <= 10000; i++) {
			int[] a = new int[10000];
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
	}

	private static class CleanerRunnable implements Runnable {
		private String id;

		public CleanerRunnable(String id) {
			this.id = id;
		}

		@Override
		public void run() {
			System.out.printf("MyObject with id %s, is gc'ed%n", id);

		}
	}
}