package kr._19fstudy.effective_java.ch4.item18.before;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;


public class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;

    public InstrumentedHashSet() {}

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount+= c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        InstrumentedHashSet s = new InstrumentedHashSet<>();
        s.addAll(Arrays.asList("A", "B", "C"));
        System.out.println(s.getAddCount());
    }
}


