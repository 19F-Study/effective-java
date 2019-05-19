# Item 79. 과도한 동기화는 피하라.

> 과도한 동기화는 성능을 떨어뜨리고, 교착상태에 빠뜨리고, 심지어 예측할 수 없는 동작을 낳기도 한다. - p420

<br>

## 응답 불가와 안전 실패를 피하려면 동기화 메서드나 동기화 블록 안에서는 제어를 절대로 클라이언트에 양도하면 안된다.
* alien method (외계인 메서드) 는 클라이언트에게 제어를 양도하는 대표적인 사례이다.
    * 동기화된 영역 안에서는 재정의할 수 있는 메서드 (외계인 메서드) 는 호출하면 안된다.
    * 동기화된 영역 안에서는 클라이언트가 넘겨준 함수 객체 (외계인 메서드) 를 호출해서도 안된다.
    * 그 메서드가 무슨 일을 할지 알 수 없으므로 통제할 수도 없다. 그래서 이 메서드가 하는 일에 따라 동기화된 영역은 예외를 일으키거나, 교착상태에 빠지거나,데이터를 훼손할 수도 있다.
* 예제 코드를 살펴보도록 하자.

```java
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
        synchronized (observers) {
            for (SetObserver<E> observer : observers) {
                observer.added(this, element);
            }
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
```

Case 1) ConcurrentModificationException
```java
@Test(expected = ConcurrentModificationException.class)
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

private void update(ObservableSet set) {
    for (int i = 0; i < 100; i++) {
        set.add(i);
    }
}
```
* 위 코드를 보면 0부터 23까지 출력한 후 관찰자 자신을 구독해지한 다음 조용히 종료할 것이다. 그런데 실제로 실행해보면 ConcurrentModificationException 이 발생한다.
* 관찰자의 added 메서드 호출이 일어난 시점이 notifyElementAdded가 관찰자들의 리스트를 순회하는 도중이기 때문이다.
* added 메서드는 ObservableSet의 removeObserver 메서드를 호출하고, 이 메서드는 다시 observers.remove 메서드를 호출한다. 리스트에서 원소를 제거하려 하는데, 마침 지금은 이 리스트를 순회중이므로, 허용되지 않은 동작이다.
* notifyElementAdded() 메서드에서 수행하는 순회는 동기화 블록 안에 있으므로 동시 수정이 일어나지 않도록 보장하지만, 정작 자신이 콜백을 거쳐 되돌아와 수정을 하는 것까지 막지는 못한다.

Case 2) Deadlock
```java
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
```
* 구독해지를 하는 관찰자를 작성하는데, removeObserver를 직접 호출하지 않고, 별도의 스레드로 호출한다.
* 백그라운드 스레드가 set.removeObserver를 호출하면, 관찰자를 잠그려 시도하지만 락을 얻을 수 없다. 메인 스레드가 이미 락을 쥐고 있기 때문이다. 그와 동시에 메인 스레드는 백그라운드 스레드가 관찰자를 제거하기만을 기다리는 중이다. 이것은 교착상태이다.

<br>

## 그렇다면 불변식이 임시로 깨진 경우라면 어떻게 될까?
* 자바의 락은 재진입(reentrant)을 허용하므로 교착상태에 빠지지는 않는다. 다만, 외계인 메서드를 호출하는 스레드가 이미 락을 쥐고 있으므로 다음번 락 획득도 성공한다. 그 락이 보하는 데이터에 대해 개념적으로 관련이 없는 다른 작업이 진행 중인데도 말이다. 이것때문에 응답 불가(교착상태) 가 될 상황을 (안전 실패)로 변모시킬 수 있다.
* 외계인 메서드 호출을 동기화 블록 바깥으로 옮겨서 해결할 수 있다. notifyElementAdded 메서드에서라면, 관찰자 리스트를 복사해 쓰면 락 없이도 안전하게 순회할 수 있다. 이 방식을 적용하면 앞서의 두 예제에서 예외 발생과 교착상태 증상이 사라진다.
```java
public void notifyElementAdded(E element) {
    List<SetObserver<E>> snapshot = null;
    synchronized (observers) {
        snapshot = new ArrayList<>(observers);
    }
    // 동기화 영역 바깥에서 호출되는 외계인 메서드를 열린 호출 (open call) 이라고 한다.
    // 외계인 메서드는 얼마나 오래 실행될지 예측할 수 없기 때문에, 동기화 영역 안에서 호출된다면 그동안 다른 스레드는 보호된 자원을 사용하지 못하고 대기해야만 한다.
    // 따라서, 열린 호출은 실패 방지 효과외에도 동시성 효율을 크게 개선해준다.
    for (SetObserver<E> observer : snapshot) {
        observer.added(this, element);
    }
}
```

<br>

## 더 나은 방법은 자바 동시성 컬렉션 라이브러리의 CopyOnWriteArrayList 를 사용하는 것이다.
* CopyOnWriteArrayList는 ArrayList를 구현한 클래스로, 내부를 변경하는 작업은 항상 깨끗한 복사본을 만들어 수행하도록 구현했다.
* 내부의 배열은 절대 수정되지 않으니 순회할 때 락이 필요 없어서 매우 빠르다.
* 다른 용도로 쓰인다면 느리겠지만, 수정할 일은 드물고 순회만 빈번히 일어나는 관찰자 리스트 용도로는 최적이다.

```java
public class ObservableSetNew<E> extends ForwardingSet<E> {
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
}
```

<br>

## 기본 규칙은 동기화 영역에서는 가능한 한 일을 적게 하는 것이다.
* 락을 얻고, 공유 데이터를 검사하고, 필요하면 수정하고, 락을 놓는다.
* 오래 걸리는 작업이라면 동기화 영역 바깥으로 옮기는 방법을 찾아보자.

<br>

## 지금까지는 정확성에 대해 알아봤다면, 이제 성능 측면도 알아보도록 하자.
* 멀티코어가 일반화된 오늘날, 과도한 동기화가 초래하는 진짜 비용은 락을 얻는 데 드는 CPU 시간이 아니다. 바로 경쟁하느라 낭비하는 시간, 즉 병렬로 실행할 기회를 잃고, 모든 코어가 메모리를 일관되게 보기 위한 지연 시간이 진짜 비용이다.
* 가상 머신 (JVM)의 코드 최적화를 제한한다는 점도 과도한 동기화의 또 다른 숨은 비용이다.

**가변 클래스를 작성하려거든 다음 두 선택지 중 하나를 따르자.**
1. 동기화를 전혀 하지 말고, 그 클래스를 동시에 사용해야 하는 클래스가 외부에서 알아서 동기화하게 하자. ( java.util )
2. 동기화를 내부에서 수행해 스레드 안전한 클래스로 만들자. 단 클라이언트가 외부에서 객체 전체에 락을 거는 것보다 동시성을 월등히 개선할 수 있을 때만 두 번째 방법을 선택해야 한다. ( java.util.concurrent )

**case**
* StringBuffer 인스턴스는 거의 항상 단일 스레드에서 쓰였음에도 불구하고 내부적으로 동기화를 수행했다. 뒤늣게 StringBuilder가 등장한 이유이기도 하다. (StringBuilder는 그저 동기화 하지 않은 StringBuffer이다.
* 비슷한 이유로, 스레드 안전한 의사 난수 발생기인 java.util.Random은 동기화하지 않은 버전인 java.util.concurrent.ThreadLocalRandom으로 대체되었다.
* 선택하기 어렵다면, 동기화하지 말고, 대신 문서에 스레드 안전하지 않다고 명기하자.

<br>

## 핵심 정리
* 교착상태와 데이터 훼손을 피하려면 동기화 영역 안에서 외계인 메서드를 절대 호출하지 말자. 일반화해서 이야기하자면, 동기화 영역 안에서의 작업은 최소한으로 줄이자.
* 가변 클래스를 설계할 때는 스스로 동기화해야 할지 고민하자. 합당한 이유가 있을 때만 내부에서 동기화하고, 동기화 했는지 여부를 문서에 명확히 밝히자.






