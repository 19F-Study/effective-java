package kr._19fstudy.effective_java.ch2.item7.Code7_1;

public class Main {
	public static void main(String[] args) {
		Stack stack = new Stack();
		java.util.Stack javaStack = new java.util.Stack();

		for (int i = 0 ; i < 10000 ; i ++) {
			stack.push(i);
			javaStack.push(i);
		}

		while(stack.getSize() > 0)
			System.out.println(stack.pop());


		while(javaStack.size() > 0)
			System.out.println(javaStack.pop());

		System.out.println("finished.");
	}
}
