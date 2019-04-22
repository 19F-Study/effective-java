# Item 52, 다중정의는 신중히 사용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

## 오버로딩 vs 오버라이딩

- 함수를 오버로딩 했을 때 어떤 메서드가 호출될지는 컴파일 타임의 타입에 정해진다. 런타임의 타입은 어떤 함수를 실행시킬지에 영향을 미치지 않는다. 즉, 오버로딩 된 메소드들 사이에서 어떤 함수를 실행할지 선택하는 것은 static하다.
- 반면 오버라이딩된 메서드들 사이에서 어떤 메서드를 실제로 실행시킬지는 선택하는 것은 dynamic하다. 오버라이딩된 메서드가 있을 경우 어떤 메서드가 실행될지는 객체의 런타임의 타입에 의해 정해진다.

## 오버로딩된 메소드가 혼란을 일으키는 상황을 피해야 한다.

- 가장 안전하고 보수적으로 가려면 매개변수 수가 같은 오버로딩 함수는 만들지 말자.
- 특히, 메서드가 가변인수를 사용할 경우에 보수적인 정책은 오버로딩을 아예 하지 않는 것이다.
- 그러면 어떻게? 메서드를 오버로딩하기 보단 다른 이름을 주면 된다.
    - ex) ObjectOutputStream class
        - write 를 오버로딩하지 않고 writeBoolean(boolean), writeInt(int) 그리고 writeLong(long)와 같이 만들었다.
- 생성자의 경우는 이름을 다르게 지을 수 없다. 따라서 두 번째 생성자부터는 무조건 오버로딩이 된다.
    - 여러 이름을 가진 생성자를 만들 수는 없다. 그러나 생성자 대신 정적 팩터리를 만들 수 있다.
    - 그래도 완전히 오버로딩 하는 것을 피할 수는 없다. 오버로딩을 할 때 안전할 수 있는 방법들을 살펴보자.
- 매개변수 수가 같은 오버로딩 메서드가 많더라도, 그 중 어느 것이 주어진 매개변수 집합을 처리할 지가 명확히 구분된다면 헷갈릴 일이 없다. 즉, 매개변수 중 하나 이상이 근본적으로 다르다(radically different)면 헷갈릴 일이 없다. 이 조건만 충족하면 어느 오버로딩 메서드를 호출할지가 매개변수들의 런타임 타입만으로 결정된다.
    - 근본적으로 다름(radically different)
        - 두 타입의 null이 아닌 값을 서로 어느 쪽으로든 형변환을 할 수 없음.

## 주의할 점들

- 자바 5 이후 주의할 점
    - 이전에는 모든 primitive type는 모든 참조 타입과 radically different 했다. 그러나 오토박싱이 도입된 이후로는 그렇지 않다.
    - SetList 의 예
        - List<E>라는 interface에서 remove(E)와 remove(int)가 있다면 혼란을 초래할 수 있다. 자바 5 이전에는 Object와 int가 radiaclly different 했지만 제네릭과 오토박싱이 생긴 이후로 더이상 이 둘은 radically different 하지 않다.
- 자바 8 이후 주의할 점
    - ExecutorService::submit 메소드는 Callable<T>를 받을 수도 있고 Runnable를 받을 수도록 오버로딩 되어 있다. 그래서 System.out::println 같은 함수를 submit에 인자로 넘기면 제대로 컴파일되지 않는다.
    
        ```
        // 정상적으로 컴파일 됨
        new Thread(System.out::println).start();
        
        ExecutorService exec = Executors.newCachedThreadPool();
        // 컴파일되지 않음
        exec.submit(System.out::println);
        ```

    - 사실 이 문제는 다중정의 해소 알고리즘이 원하는 대로 동작하지 않는 한 예다 (뭔가 복잡한 사정이 있으나 컴파일러 제작자가 아니기 때문에 넘어갔다). 핵심은 같은 인수 위치에 다른 함수형 인터페이스를 받을 수 있도록 함수를 오버로딩하지 말자는 것이다. 서로 다른 함수형 인터페이스는 radically different 하지 않기 때문이다.
- 기타
    - 배열 타입과 Object외의 클래스 타입은 radically different하다. 또한 배열 타입과 Serializable과 Cloneable 외의 인터페이스 타입도 radically different하다.  한편, String과 Throwable처럼 상위/하위 관계가 아닌 두 클래스는 관련 없다(unrelated)라고 한다. 그리고 관련 없는 클래스는 radically different하다.