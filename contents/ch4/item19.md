# Item 19, 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라 

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트
- [https://google.github.io/guava/releases/19.0/api/docs/](https://google.github.io/guava/releases/19.0/api/docs/)
- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractCollection.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractCollection.html)
- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractList.html#removeRange(int,int)](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractList.html#removeRange(int,int))

### 효율적인 하위 클래스를 큰 어려움 없이 만들 수 있게 하는 방법

- 문서화
    - 메서드를 재정의하면 어떤 일이 일어나는지 정확히 정리하여 문서로 남겨야 한다. 달리 말하면, 상속용 클래스는 재정의할 수 있는 메서드들을 내부적으로 어떻게 이용하는지(자기사용) 문서로 남겨야 한다.
        - 클래스의 API로 공개된 메서드에서 클래스 자신의 또 다른 메서드를 호출할 수도 있다. 그런데 마침 호출되는 메서드가 재정의 가능 메서드라면 그 사실을 호출하는 메서드의 API 설명에 직시해야 한다.
            - 어떤 순서로 호출하는지, 각각의 호출 결과가 이어지는 처리에 어떤 영향을 주는지도 담아야 한다.
        - 재정의 가능: public과 protected 메서드 중 final이 아닌 모든 메서드
    - API 문서의 메서드 설명 끝에서 종종 Implementation Requirements로 시작하는 절이 있는데 이 부분이 그 메서드의 내부 동작 방식을 설명하는 곳이다.
    - 위와 같은 내용은 "좋은 API 문서란 '어떻게'가 아닌 '무엇'을 하는지를 설명해야 한다"라는 격언과 대치된다. 상속이 캡슐화를 해치기 때문에 일어나는 안타까운 현실이다. 클래스를 안전하게 상속할 수 있도록 하려면 내부 구현 방식을 설명해야만 한다.
    - 참고
        - 주로 Abstract 클래스에 관련된 문서화가 되어있다.
        - [https://google.github.io/guava/releases/19.0/api/docs/](https://google.github.io/guava/releases/19.0/api/docs/)
            - ForwardingCollection
        - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractCollection.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractCollection.html)

### 클래스의 내부 동작 과정 중간에 끼어들 수 있는 훅(hook)을 잘 선별하여 protected 메서드 형태로 공개해야 할 수도 있다.

- ex) java.util.AbstractList의 removeRagne
    - AbstractList.clear
    - [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractList.html#removeRange(int,int)](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractList.html#removeRange(int,int))
- 상속용 클래스를 설계할 때 어떤 메서드를 protected로 노출해야 할까?
    - 심사숙고해서 잘 예측해본 다음, 실제 하위 클래스를 만들어 시험해보는 것이 최선이다. (당황)
    - protected 메서드 하나하나가 내부 구현에 해당하므로 그 수는 가능한 한 적어야 한다.
    - 한편으로는 너무 적게 노출해서 상속으로 얻는 이점마저 없애지 않도록 주의해야 한다.
- 상속용 클래스를 만들 때 주의할 점
    - 상속용 클래스를 시험하는 방법은 직접 하위 클래스를 만들어보는 것이 '유일'하다.
        - 꼭 필요한 protected 멤버를 놓쳤다면 하위 클래스를 작성할 때 그 빈 자리가 확연히 드러난다.
        - 하위 클래스를 여러 개 만들 때까지 전혀 쓰이지 않는 protected 멤버는 사실 private이었어야 할 가능성이 크다.
        - 상속용으로 설계한 클래스는 배포 전에 반드시 하위 클래스를 만들어 검증해야 한다.
    - 상속용 클래스의 생성자는 직접적으로든 간접적으로든 재정의 가능 메서드를 호출해서는 안 된다. 이 규칙을 어기면 프로그램이 오작동할 것이다. 상위 클래스의 생성자가 하위 클래스의 생성자보다 먼저 실행되므로 하위 클래스에서 재정의한 메서드가 하위 클래스의 생성자보다 먼저 호출된다. 이때 그 재정의한 메서드가 하위 클래스의 생성자에서 초기화하는 값에 의존한다면 의도대로 동작하지 않을 것이다.
        - 예시 자료, 디버거
    - Cloneable과 Serializable 인터페이스는 상속용 설계의 어려움을 한층 더해준다.
        - 가능하면 하지 말자
        - clone과 readObject 메서드는 생성자와 비슷한 효과를 낸다. 즉, clone와 readObject 모두 직접적으로든 간접적으로든 재정의 가능 메서드를 호출해서는 안 된다.
            - readObject의 경우 하위 클래스의 상태가 미처 다 역직렬화되기 전에 재정의한 메서드부터 호출하게 된다.
            - clone의 경우 하위 클래스의 clone 메서드가 복제본의 상태를 올바른 상태로 수정하기 전에 재정의한 메서드를 호출한다. clone의 경우 잘못되변 복제본 뿐 아니라 원본 객체에도 피해를 줄 수 있다. (원본 객체의 내부 자료구조를 잘못해서 참조하고 있을 경우)
        - Serializable을 구현한 상속용 클래스가 readResolve나 writeReplace 메서드를 갖는다면 이 메서드들은 private이 아닌 protected로 선언해야 한다. private으로 선언한다면 하위 클래스에서 무시되기 때문이다.

### 클래스를 상속용으로 설계하려면 엄청난 노력이 들고 그 클래스에 안기는 제약도 상당하다.

- 상속용으로 설계하지 않은 클래스는 상속을 금지하는 것이 좋다.
- 상속을 금지하는 방법
    - 더 쉬운 방법은 클래스를 final로 선언하는 방법이다.
    - 모든 생성자를 private이나 package-private으로 선언하고 public 정적 팩터리를 만들어주는 방법이다.
- 구체 클래스가 표준 인터페이스를 구현하지 않았는데 상속을 금지하면 사용하기에 상당히 불편해진다. 이런 클래스라도 꼭 상속을 허용해야겠다면 클래스 내부에서는 재정의 가능 메서드를 사용하지 않게 만들고 이 사실을 문서로 남기게 해야한다.