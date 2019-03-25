package kr._19fstudy.effective_java.ch5.item31.stack;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Stack<Number> stack = new Stack<>();
        Iterable<Integer> integers = () -> List.of(1, 2, 3).iterator();
        stack.pushAll(integers);
    }

}
