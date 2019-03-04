package kr._19fstudy.effective_java.ch2.item7.call_back;

import java.util.List;

public class CallBackTestMain {
	public static void main(String[] args) {
		Task task = new SimpleTask();
		CallBack callback1 = new CallBack() {
			@Override
			public void call() {
				System.out.println("호출 종료");
			}
		};
		CallBack callback2 = new CallBack() {
			@Override
			public void call() {
				System.out.println("호출 종료");
			}
		};
		task.executeWith(List.of(callback1, callback2));
	}
}
