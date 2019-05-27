package kr._19fstudy.effective_java.ch11.item81.simplelock;

// ref: http://tutorials.jenkov.com/java-concurrency/locks.html
public class SimpleLock {

    private boolean locked = false;

    public synchronized void lock() throws InterruptedException {
        while (this.locked) {
            wait();
        }
        this.locked = true;
    }

    public synchronized void unlock() {
        this.locked = false;
        notify();
    }

}
