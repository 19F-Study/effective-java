package kr._19fstudy.effective_java.ch2.item7.out_of_memory;

import kr._19fstudy.effective_java.ch2.item8.cleaner_ex.MyObject;

import java.util.HashMap;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		HashMap hashMap = new HashMap();

		Thread t = new Thread(() -> {
			int i = 0;
			while (true) {
				byte[] b = new byte[1000000];

				hashMap.put(i, new MyObject(new String(b)));
				i++;
				System.out.println(i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		t.run();

		t.join();

	}
}
