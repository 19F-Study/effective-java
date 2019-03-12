package kr._19fstudy.effective_java.ch4.item18.after;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class NewInstrumentedHashSet<E> {
    private int addCount = 0;
    private HashSet<E> s; // composition

    public NewInstrumentedHashSet(HashSet<E> s) {
        this.s = s;
    }

    // forwarding method
    public boolean add(E e) {
        addCount++;
        return s.add(e); // forwarding
    }

    // forwarding method
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return s.addAll(c); // forwarding
    }

    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        NewInstrumentedHashSet s = new NewInstrumentedHashSet<>(new HashSet<String>());
        s.addAll(Arrays.asList("A","B","C"));
        System.out.println(s.getAddCount()); // 3
    }
}
