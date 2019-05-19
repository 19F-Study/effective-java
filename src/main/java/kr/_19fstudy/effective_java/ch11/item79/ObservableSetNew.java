package kr._19fstudy.effective_java.ch11.item79;

import kr._19fstudy.effective_java.ch4.item18.after.ForwardingSet;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;


public class ObservableSetNew<E> extends ForwardingSet<E> {

    @FunctionalInterface public interface SetObserver<E> {
        // ObservableSet에 원소가 더해지면 호출된다.
        void added(ObservableSetNew<E> set, E element);
    }

    private final List<ObservableSetNew.SetObserver<E>> observers = new CopyOnWriteArrayList<>();

    public ObservableSetNew(Set<E> s) {
        super(s);
    }

    public void addObserver(ObservableSetNew.SetObserver<E> observer) {
        observers.add(observer);
    }

    public boolean removeObserver(ObservableSetNew.SetObserver<E> observer) {
        return observers.remove(observer);
    }

    public void notifyElementAdded(E element) {
        for (ObservableSetNew.SetObserver<E> observer : observers) {
            observer.added(this, element);
        }
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
