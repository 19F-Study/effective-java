package kr._19fstudy.effective_java.ch11.item79;


import kr._19fstudy.effective_java.ch4.item18.after.ForwardingSet;

import java.util.*;

public class ObservableSet<E> extends ForwardingSet<E> {
    @FunctionalInterface public interface SetObserver<E> {
        // ObservableSet에 원소가 더해지면 호출된다.
        void added(ObservableSet<E> set, E element);
    }

    private final List<SetObserver<E>> observers = new ArrayList<>();

    public ObservableSet(Set<E> s) {
        super(s);
    }

    public void addObserver(SetObserver<E> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }

    public void notifyElementAdded(E element) {
        // Case 1
        synchronized (observers) {
            for (SetObserver<E> observer : observers) {
                observer.added(this, element);
            }
        }

        // Case 2
//        List<SetObserver<E>> snapshot = null;
//        synchronized (observers) {
//            snapshot = new ArrayList<>(observers);
//        }
//        for (SetObserver<E> observer : snapshot) {
//            observer.added(this, element);
//        }
    }

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (added) {
            notifyElementAdded(e);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) {
            // notifyElementAdded를 호출한다.
            result |= add(e);
        }
        return result;
    }
}
