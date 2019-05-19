package kr._19fstudy.effective_java.ch11.item79;

import org.junit.Test;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;


public class ObservableSetNewTest {

    @Test
    public void testObservableSetNew1() {
        ObservableSetNew<Integer> set = new ObservableSetNew<>(new HashSet<>());
        set.addObserver((s, e) -> System.out.println(e));
        update(set);
    }

    @Test
    public void testObservableSetNew2() {
        ObservableSetNew<Integer> set = new ObservableSetNew<>(new HashSet<>());
        set.addObserver(new ObservableSetNew.SetObserver<Integer>() {
            @Override
            public void added(ObservableSetNew<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    set.removeObserver(this);
                }
            }
        });
        update(set);
    }

    @Test
    public void testObservableSetNew3() {
        ObservableSetNew<Integer> set = new ObservableSetNew<>(new HashSet<>());
        set.addObserver(new ObservableSetNew.SetObserver<Integer>() {
            @Override
            public void added(ObservableSetNew<Integer> set, Integer element) {
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

    private void update(ObservableSetNew set) {
        for (int i = 0; i < 100; i++) {
            set.add(i);
        }
    }

}