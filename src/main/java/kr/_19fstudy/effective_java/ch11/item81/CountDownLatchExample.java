package kr._19fstudy.effective_java.ch11.item81;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class CountDownLatchExample {

    public long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        // 모든 작업자 스레드들이 준비가 됐음을 타이머 스레드에 통지할 때 사용한다.
        // 통지를 끝낸 작업자 스레드들은 두 번째 래치인 start가 열리기를 기다린다.
        CountDownLatch ready = new CountDownLatch(concurrency);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                ready.countDown();
                try {
                    start.await();
                    action.run();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        // executor에 concurrency 만큼 job이 execute될 때까지 기다린다.
        ready.await();
        // 마지막 작업자 스레드가 ready.countDown()을 호출하고 나면
        // 타이머 스레드가 시작 시간을 기록한다.
        long startNanos = System.nanoTime();
        // 실제 action이 run을 실행하게 한다.
        start.countDown();
        // 모든 job이 끝날 때까지 기다린다.
        done.await();
        return System.nanoTime() - startNanos;
    }

}
