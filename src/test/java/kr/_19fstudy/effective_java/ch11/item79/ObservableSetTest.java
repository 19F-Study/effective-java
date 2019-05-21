package kr._19fstudy.effective_java.ch11.item79;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;


public class ObservableSetTest {

    @Test
    public void testObservableSet1() {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
        set.addObserver((s, e) -> System.out.println(e));
        update(set);
    }

    @Test
    public void testObservableSet2() {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
        set.addObserver(new ObservableSet.SetObserver<Integer>() {
            @Override
            public void added(ObservableSet<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    set.removeObserver(this);
                }
            }
        });
        update(set);
    }

    @Test
    public void testObservableSet3() {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
        set.addObserver(new ObservableSet.SetObserver<Integer>() {
            @Override
            public void added(ObservableSet<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> set.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new AssertionError(e);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });
        update(set);
    }

    private void update(ObservableSet set) {
        for (int i = 0; i < 100; i++) {
            set.add(i);
        }
    }
}