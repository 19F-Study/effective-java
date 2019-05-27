# Item 81, wait와 notify보다는 동시성 유틸리티를 애용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

## Java Monitor, Wait, Notify

- Java Monitor
    - 참고: [https://www.programcreek.com/2011/12/monitors-java-synchronization-mechanism/](https://www.programcreek.com/2011/12/monitors-java-synchronization-mechanism/)
        - What is a Monitor?
            - 모니터는 빌딩에서 한 명만 들어갈 수 있는 특별한 방과 같다. 그 방은 대개 데이터와 코드를 담고 있다.
            - 특별한 방(Monitor)에 들어가려면 고객은 먼저 복도(Entry Set)에서 기다려야 한다. 그러면 스케줄러가 특정한 알고리즘(e.g. FIFO)에 따라 한 사람을 고를 것이다. 만약 어떤 일로 인해 중단 된다면 그는 대기실(Wait Set)으로 보내지고 나중에 다시 특별한 방에 들어갈 수 있게 스케줄 될 것이다.
            - 간단히 말해 모니터는 스레드가 특수한 공간에 접근하는 것을 모니터링하는 기능이다. 하나의 스레드만 보호된 데이터 또는 코드에 엑세스 하는 것을 보장한다.
        - How is it implemented in Java?
            - JVM에서 모든 객체와 클래스는 논리적으로 모니터와 연관되어 있다. 모니터의 상호 배제(mutual exclusion) 기능을 구현하기 위해 락(lock, 때때로 mutex 라고도 불림)이 각 객체와 클래스에 연관되어 있다.
            - 만약 한 스레드가 어떤 데이터에 대해 락을 갖고 있다면 그 스레드가 락을 놓아주기 전까지 다른 어떤 스레드도 락을 획득할 수 없다. 그러나 우리가 멀티 쓰레딩 프로그래밍을 할 때마다 직접 세마포어(락)과 관련된 코드를 직접 작성해야 된다면 매우 불편할 것이다. JVM이 우릴 위해 그런 것들을 해준다.
            - 데이터에 둘 이상의 스레드가 동시에 접근할 수 없는 영역을 의미하는 모니터를 위해 자바는 synchronized 구문과 synchronized 메서드를 제공한다.
        - In Java synchronization code, which part is monitor?
            - 우리는 각각의 객체 및 클래스가 모니터와 연관되어 있다는 것을 안다. 각각의 객체가 모니터를 갖고 있다고 말하는 것이 좋다고 생각한다. 각각의 객체는 고유한 임계 영역(critical section)을 갖고 있고 스레드의 순서를 모니터링 할 수 있기 때문이다.
            - 서로 다른 스레드간의 협업을 위해 Java는 wait()와 notify()를 제공한다. 이 메서드들을 통해 스레드를 멈추고 깨어나게 할 수 있다.
            - 이 메서드들은 synchronized 구문과 synchroinzed 메서드에서만 호출할 수 있다. 그 이유는 어떤 메서드가 상호 배제를 필요로 하지 않을 경우에는 모니터나 서로 다른 스레드 간의 협업이 필요하지 않고 모든 스레드가 그 메서드에 자유롭게 접근해도 되기 때문이다.
    - 참고: [https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)
        - Intrinsic Locks and Synchronization
            - 자세한 내용은 본문 참고
            - 간단 요약
                - 모든 객체는 그 객체와 연관된 고유한 intrinsic lock를 갖고 있다.
                - 객체의 synchronized method를 호출하면 자동으로 그 객체(Object)의 고유한 intrinsic lock를 이용한다.
                - static synchronized method를 호출하면 그 클래스(Class)의 intrinsic lock을 이용한다.
                - synchronized statements를 이용할 경우엔 intrinsic lock를 제공할 객체를 반드시 명시해주어야 한다.
                    - ex)
                        ```java
                        public void addName(String name) {
                            synchronized(this) {
                                lastName = name;
                                nameCount++;
                            }
                        // but also needs to avoid synchronizing invocations of 
                        // other objects' methods.
                            nameList.add(name);
                        }
                        ```

                - 같은 락을 이용해 synchronized 될 필요가 없는 필드를 다룰 때
                    - ex)
                        ```java
                        public class MsLunch {
                            private long c1 = 0;
                            private long c2 = 0;
                            private Object lock1 = new Object();
                            private Object lock2 = new Object();
                        
                            public void inc1() {
                                synchronized(lock1) {
                                    c1++;
                                }
                            }
                        
                            public void inc2() {
                                synchronized(lock2) {
                                    c2++;
                                }
                            }
                        }
                        ```
                - 스레드는 락을 다른 스레드가 갖고 있으면 그 락을 획득할 수 없다. 그러나 스레드는 자신이 갖고 있는 락은 획득할 수 있다. 스레드가 같은 락을 한 번 이상 획득하는 것을 허용하는 것을 reentrant synchronization 이라고 한다.
                    - [https://docs.oracle.com/javase/tutorial/essential/concurrency/newlocks.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/newlocks.html)
    - [http://www.csc.villanova.edu/~mdamian/threads/javamonitors.html](http://www.csc.villanova.edu/~mdamian/threads/javamonitors.html)
- Object.wait
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html#wait(long,int)](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html#wait(long,int))
- Object.notify
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html#notify()](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html#notify())
- Object.notifyAll
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html#notifyAll()](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html#notifyAll())
- Examples
    - [http://happinessoncode.com/2017/10/05/java-object-wait-and-notify/](http://happinessoncode.com/2017/10/05/java-object-wait-and-notify/)
    - [https://www.baeldung.com/java-wait-notify](https://www.baeldung.com/java-wait-notify)
- wait와 notify는 올바르게 사용하기가 아주 까다로우니 고수준 동시성 유틸리티를 사용하자.

## java.util.concurrent

- java.util.concurrent의 패키지는 크게 Executors, Queues, Timing, Synchronizers, Concurrent Collections로 구성되어 있다. 그리고 하위 패키지로는 java.util.concurrent.locks, java.util.concurrent.atomic가 있다. 각각의 package-summary에 보면 각 패키지에 대한 설명이 나와있다. 특히 java.util.concurrent 패키지에는 자바 명세와 함께 java.util.concurrent 패키지와 그 서브 패키지들이 동기화에 관해 보장하는 내용이 있으니 읽어 보길 권장한다(Memory Consistency Properties 부분).
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/package-summary.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/package-summary.html)
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/locks/package-summary.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/locks/package-summary.html)
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/package-summary.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/package-summary.html)

### Concurrent Collections, 동시성 컬렉션

- 동시성 컬렉션은 List, Queue, Map 같은 표준 컬렉션 인터페이스에 동시성을 가미해 구현한 고성능 컬렉션이다. 높은 동시성에 도달하기 위해 동기화를 각자의 내부에서 수행한다. 따라서 동시성 컬렉션에서 동시성을 무력화하는 건 불가능하며, 외부에서 락을 추가로 사용하면 오히려 속도가 느려진다.
    - 동시성 컬렉션에서 동시성을 무력화하는 것은 불가능하다. 따라서 여러 메서드를 원자적으로 묶어서 호출하는 일 역시 불가능하다. 그래서 여러 기분 동작을 하나의 원자적 동작으로 묶는 '상태 의존적 수정' 메서드들이 추가되었다.
        - ex) Map의 putIfAbsent(key, value)
    - 동시성 컬렉션은 동기화한 컬렉션보다 낫다. Collections.synchronizedMap 보다는 ConcurrentHashMap을 사용하는 게 훨씬 좋다.
        - 이 패키지의 일부 클래스와 함께 사용되는 "Concurrent" 접두어는 유사한 "synchronized" 된 클래스와 몇 가지 차이점을 나타낸다. 예를 들어 Collections.synchroinzedMap는 동기화된다(synchroinzed). 그러나 ConcurrentHashMap은 말 그대로 동시(concurrent)이다. Concurrent Collection은 thread-safe이지만 하나의 배타적인 lock에 의해 관리되지 않는다. 특히 ConcurentHashMap의 경우, 동시 읽기와 대량의 동시 쓰기를 안전하게 허용한다. Synchronized 된 클래스의 경우 이에 비해 확장성이 떨어진다. 그러나 컬렉션으로의 모든 접근을 하나의 락으로 제어해야 할 때 유용할 수 있다. 여러 개의 스레드가 공통 컬렉션에 접근할 것이 예상될 때 "Concurrent" 버전이 일반적으로 더 바람직하다. 그리고 unsynchronized(동기화되지 않은) 컬렉션은 컬렉션이 공유되지 않는 경우 또는 락을 들고 있을 때만 액세스할 수 있는 경우에 적합하다.
            - java.util.concurrent의 package-summary.html 참고
- 컬렉션 인터페이스 중 일부는 작업이 성공적으로 완료될 때까지 기다리도록 확장되었다. Queue를 확장한 BlockingQueue에 추가된 메서드 중 take는 큐의 첫 원소를 꺼낸다. 이때 만약 큐가 비었다면 새로운 원소가 추가될 때까지 기다린다(blocking). 이런 특성 덕에 BlockingQueue는 작업 큐(생산자-소비자 큐)로 쓰기에 적합하다.
    - wait, notify를 이용해 복잡하게 구현하던 BlockingQueue를 java.util.concurrent 패키지에서 제공하는 BlockingQueue로 쉽게 바꿀 수 있다.

### Synchronizers, 동기화 장치

- 동기화 장치는 스레드가 다른 스레드를 기다릴 수 있게 하여 서로 작업을 조율할 수 있게 도와주는 도구이다.
- java.util.concurrent에서 제공하는 동기화 장치의 종류
    - Semaphore는 전통적인 동시성 도구이다.
    - CountDownLatch는 주어진 신호, 이벤트 또는 조건이 유지될 때까지 차단하기 위한 매우 간단하지만 매우 일반적인 유틸리티다.
    - CyclicBarrier는 일부 유형의 병렬 프로그래밍에서 유용한 재설정 가능한 다중방향 동기화 지점이다.
    - Phaser는 다중 스레드 간의 단계적 계산을 제어하는 데 사용될 수 있는 유연한 형태의 장벽을 제공한다.
    - Exchanger는 두 개의 스레드가 랑데부 지점에서 객체를 교환할 수 있도록 하며, 여러 개의 파이프라인 설계에 유용하다.
- CountDownLatch
    - CountDownLatch는 일회성 장벽으로, 하나 이상의 스레드가 또 다른 하나 이상의 스레드 작업이 끝날  때까지 기다리게 한다.
    - 생성자를 통해 받는 int 값은 countDown 메서드를 몇 번 호출해야 대기 중은 스레드들을 깨우는지(진행시키는지)를 결정한다.
    - 코드 예제) CountDownLatchExample.java
        - 몇몇 세부 사항
            - time 메서드에 넘겨진 실행자(executor)는 concurrency 매개변수로 지정한 동시성 수준만큼의 스레드를 생성할 수 있어야 한다. 그렇지 못하면 이 메서드는 스레드 기아 교착상태에 빠져 결코 끝나지 않을 것이다.
            - InterruptedExecption을 캐치한 작업자 스레드는 Thread.currentThread().interrupt() 관용구를 사용해 인터럽트(interrupt)를 되살리고 자신은 run 메서드에서 빠져나온다. 이렇게 해야 실행자가 인터럽트를 적절하게 처리할 수 있다.
            - System.nanoTime vs System.currentTimeMillis

### 레거시 코드 - wait와 notify를 이용하는 경우

- 새로운 코드라면 wait와 notify를 직접 쓰는 게 아닌 동시성 유틸리티를 써야 한다. 그러나 이미 wait와 notify를 이용하도록 작성된 레거시 코드를 다뤄야 할 때도 있을 것이다. 그럴 때를 위한 몇 가지 조언이다.

- wait 메서드를 사용하는 표준 방식
    ```java
    synchronized(obj) {
        while (<조건이 충족되지 않았다>) {
             obj.wait(); // (락을 놓고, 깨어나면 다시 잡는다.)
        }
        ... // 조건이 충족됐을 때의 동작을 수행한다.
    }
    ```

- wait 메서드를 사용할 때는 반드시 대기 반복문(wait loop) 관용구를 사용하라. 반복문 밖에서는 절대로 호출하지 말자.
- 대기 전에 조건을 검사하여 조건이 이미 충족되었다면 wait를 건네뛰게 한 것은 응답 불가 상태를 예방하는 조치다. 만약 조건이 이미 충족되었는데 스레드가 notify(혹은 notifyAll) 메서드를 먼저 호출한 후 대기 상태로 빠지면, 스레드를 다시 깨울 수 있다고 보장할 수 없다.
- 한편, 대기 후에 조건을 검사하여 조건이 충족되지 않았다면 다시 대기하게 하는 것은 안전 실패를 막는 조치다. 만약 조건이 충족되지 않았는데 스레드가 동작을 이어가면 락이 보호하는 불변식을 깨뜨릴 위험이 있다.

- 조건이 만족되지 않아도 스레드가 깨어날 수 있는 상황
    - 스레드가 notify를 호출한 다음 대기 중이던 스레드가 깨어나는 사이에 다른 스레드가 락을 얻어 그 락이 보호하는 상태를 변경한다.
    - 조건이 만족되지 않았음에도 다른 스레드가 실수로 혹은 악의적으로 notify를 호출한다. 공개된 객체를 락으로 사용해 대기하는 클래스는 이런 위험에 노출된다. 외부에 노출된 객체의 동기화된 메서드 안에서 호출하는 wait는 모두 이 문제에 영향을 받는다.
    - 깨우는 스레드는 지나치게 관대해서, 대기 중인 스레드 중 일부만 조건이 충족되어도 notifyAll을 호출해 모든 스레드를 깨울 수도 잇다.
    - 대기 중인 스레드가 (드물게)) notify 없이도 깨어나는 경우가 있다. 허위 각성(spurious wakeup)이라는 현상이다.

- notify vs notifyAll
    - notify는 스레드 하나만 깨우며, notifyAll은 모든 스레드를 깨운다.
    - 일반적으로 언제나 notifyAll을 사용하라는 게 합리적이고 안전한 조언이 될 것이다.
    - 모든 스레드가 같은 조건을 기다리고, 조건이 한 번 충족될 때마다 단 하나의 스레드만 혜택을 받을 수 있다면 notifyAll 대신 notify를 사용해 최적화할 수 있다.
    - 하지만 이상의 전제조건들이 만족될지라도 notify 대신 notifyAll을 사용해야 하는 이유가 있다. 외부로 공개된 객체에 실수로 학은 악의적으로 notify를 호출하는 상황에 대비하기 위해 wait를 반복문 안에서 호출했듯, notify 대신 notifyAll을 사용하면 관련 없는 스레드가 실수로 혹은 악의적으로 wait를 호출하는 공격으로부터 보호할 수 있다.