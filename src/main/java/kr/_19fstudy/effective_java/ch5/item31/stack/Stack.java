package kr._19fstudy.effective_java.ch5.item31.stack;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;

public class Stack<E> {

    private E[] elements;

    private int size = 0;

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public Stack() {
//        this.elements = new E[DEFAULT_INITIAL_CAPACITY];
        this.elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        this.elements[this.size++] = e;
    }

    public E pop() {
        if (this.size == 0)
            throw new EmptyStackException();
        E result = this.elements[--this.size];
        this.elements[this.size] = null;
        return result;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

//    public void pushAll(Iterable<E> src) {
//        src.forEach(this::push);
//    }

    public void pushAll(Iterable<? extends E> src) {
        src.forEach(this::push);
    }

    public void popAll(Collection<E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

//    public void popAll(Collection<? super E> dst) {
//        while (!isEmpty()) {
//            dst.add(pop());
//        }
//    }

    private void ensureCapacity() {
        if (this.elements.length == this.size) {
            this.elements = Arrays.copyOf(this.elements, 2 * this.size + 1);
        }
    }

}
