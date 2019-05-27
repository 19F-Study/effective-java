package kr._19fstudy.effective_java.ch11.item81.simplelock;

// ref: http://tutorials.jenkov.com/java-concurrency/locks.html
public class SimpleReentrantLock {

    private boolean locked = false;

    private Thread lockedBy = null;

    private int lockedCount = 0;

    public synchronized void lock() throws InterruptedException {
        Thread callingThread = Thread.currentThread();
        while (this.locked && this.lockedBy != callingThread) {
            wait();
        }
        this.locked = true;
        this.lockedCount++;
        this.lockedBy = callingThread;
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == this.lockedBy) {
            this.lockedCount--;

            if (this.lockedCount == 0) {
                this.locked = false;
                notify();
            }
        }
    }

}
