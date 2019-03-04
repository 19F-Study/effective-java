# Item 13, clone 재정의는 주의해서 진행하라

## 참고 및 출처

- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html)
- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Objects.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Objects.html)
- [https://dzone.com/articles/java-cloning-with-example](https://dzone.com/articles/java-cloning-with-example)
- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

### Cloneable, clone

- Cloneable는 복제해도 되는 클래스임을 명시하는 용도의 믹스인 인터페이스지만, 아쉽게도 의도한 목적을 제대로 이루지 못했다. 가장 큰 문제는 clone 메서드가 선언된 곳이 Cloneable이 아닌 Object이고, 그 마저도 protected라는 데 있다. 그래서 Cloneable을 구현하는 것만으로는 외부 객체에서 clone 메서드를 호출할 수 없다. 리플렉션을 사용하면 가능하지만, 100% 성공하는 것도 아니다.
    - 또는 Cloneable interface는 Marker interface(메서드가 없는 인터페이스)라고도 한다. 그래서 cloneable interface를 구현하는 것은 clone()을 구현할 책임이 개발자에게 전가된다.
- Cloneable 인터페이스가 하는 일
    - 이 인터페이스는 Object의 protected 메서드인 clone의 동작 방식을 결정한다.
    - Cloneable을 구현한 클래스의 인스턴스에서 clone을 호출하면 그 객체의 필드들을 하나하나 복사한 객체를 반환하며, 그렇지 않은 클래스의 인스턴스에서 호출하면 CloneNotSupportedException을 던진다. 바람직한 인터페이스의 사용 예시는 아니다.
- 실무에서 Cloneable을 구현한 클래스는 clone 메서드를 public으로 제공하며, 사용자는 당연히 복제가 제대로 이뤄지리라 기대한다. 즉, 이 기대를 만족하도록 clone 메서드를 제공해야 한다.

### clone 메서드의 허술한 일반 규약

- 이 객체의 복사본을 생성해 반환한다. '복사'의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다. 일반적인 의도는 다음과 같다. 어떤 객체 x에 대해 다음 식은 참이다.
    - x.clone() ≠ x
        - *이 식은 두 객체를 가르키는 메모리 주소 레퍼런스 값이 다르다는 말이다.*
- 또한 다음 식도 참이다.
    - x.clone().getClass() == x.getClass()
        - *이 식은 두 객체의 클래스가 서로 같다는 말이다.*
- 하지만 이상의 요구를 반드시 만족해야 하는 것은 아니다.
- 한편 다음 식도 일반적으로 참이지만, 역시 필수는 아니다.
    - x.clone().equals(x)
        - *이 식은 clone() 메소드를 호출해 얻은 객체가 기존 객체와 같은 state(data)를 가지지만 이후에는 달라질 수 있음을 의미한다.*
- 관례상, 이 메서드가 반환하는 객체는 super.clone을 호출해 얻어야 한다. 이 클래스와 (Object를 제외한) 모든 상위 클래스가 이 관례를 따른다면 다음 식은 참이다.
    - x.clone().getClass() == x.getClass()
- 관례상, 반환된 객체와 원본 객체는 독립적이어야 한다. 이를 만족하려면 super.clone으로 얻은 객체의 필드 중 하나 이상을 반환 전에 수정해야 할 수도 있다.
- 강제성이 없다는 점만 빼면 생성자 연쇄(constructor chaining)와 살짝 비슷한 메커니즘이다.

### 제대로 동작하는 clone 메서드를 가진 상위 클래스를 상속해 Cloneable을 구현해보자

1. 참조 타입을 갖지 않는 간단한 예
    - 먼저 super.clone을 호출한다. 그렇게 얻은 객체는 원본의 거의 완벽한 복제본일 것이다.
    - 공변 변환 타이핑(covariant return typing) 이용
        - 재정의한(Override) 메서드의 반환 타입은 상위 클래스의 메서드가 반환하는 타입의 하위 타입일 수 있다. 이 방식을 통해 재정의한 clone 메서드의 반환 타입을 바꾸어주고 클라이언트가 형변환하지 않아도 되게끔 해주자.

            @Override
            public PhoneNumber clone() {
                try {
                    return (PhoneNumber) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError(); // 일어날 수 없는 일이다.
                }
            }

        - super.clone 호출을 try-catch 블록으로 감싼 이유는 Object의 clone 메서드가 검사 예외(checked exception)인 CloneNotSupportedException을 던지도록 선언되었기 때문이다.
    - PhoneNumber 클래스와 같은 클래스는 clone을 쉽게 구현할 수 있다.
2. 배열을 인스턴스 변수로 갖는 예
    - 그런데 가변 객체를 참조하는 순간 clone의 구현은 재앙으로 돌변한다. clone 메서드가 단순히 super.clone 결과를 그대로 반환한다면, 기본 타입의 경우 복제가 제대로 되겠지만 배열과 같은 경우 원본 인스턴스와 같은 배열을 참조할 것이다. 이럴 경우 원본이나 복제본 중 하나를 수정하면 다른 하나도 수정되어 불변식을 해치게 된다. 따라서 프로그램이 이상하게 동작할 수 있다.
    - clone 메서드는 사실상 생성자와 같은 효과를 낸다. 즉, clone은 원본 객체에 아무런 해를 끼치지 않는 동시에 복제된 객체의 불변식을 보장해야 한다. 때문에 배열과 같은 정보를 복사할 때는 재귀적으로 clone을 호출해서 복사하는 것이다.

            @Override
            public Stack clone() {
                try {
                    Stack result = (Stack) super.clone();
                    result.elements = elements.clone();
                    return result;
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError();
                }
            }

    - 그런데 위의 경우에서 elements가 final이었다면 어떻게 되었을까? 제대로 동작할 수 없다. 따라서 Cloneable 아키텍처는 '가변 객체를 참조하는 필드는 final로 선언하라'는 일반 용법과 충돌한다.
3. clone을 재귀적으로 호출하는 것만으로도 충분하지 않은 경우
    - 복제본은 자신만의 버킷 배열을 갖지만, 이 배열은 원본과 같은 연결 리스트를 참조한다. 따라서 HashTable.Entry가 깊은 복사(deep copy)를 지원하도록 보강한 뒤 각각에 대해 deep copy를 해주어야 한다.

            @Override
            public HashTable clone() {
                try {
                    HashTable result = (HashTable) super.clone();
                    result.buckets = new Entry[buckets.length];
                    for (int i = 0; i < buckets.length; i++)
                        if (buckets[i] != null)
                            result.buckets[i] = buckets[i].deepCopy();
                    return result;
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError();
                }
            }

    - 하지만 이 방법은 스택 오버플로를 일으킬 위험이 있기 때문에 이 문제를 피하려면 deepCopy를 재귀 호출 대신 반복자를 써서 순회하는 방향으로 수정해야 한다.
4. 고수준 API를 이용하는 방법
    1. super.clone을 호출하여 얻은 객체의 모든 필드를 초기 상태로 설정한다.
    2. 원복 객체의 상태를 다시 생성하는 고수준 메서드들을 호출한다.
    - 이 방법을 이용하면 간단하고 우아한 코드를 얻을 수 있지만 느리다. 또한 Cloneable 아키텍처와는 어울리지 않는 방식이다.

### 구현에서 주의점

- clone 함수에서는 재정의할 수 있는 메서드를 호출하지 않아야 한다. 만약 clone이 하위 클래스에서 재정의한 메서드를 호출하면, 하위 클래스는 복제 과정에서 자신의 상태를 교정할 기회를 잃게 되어 원본과 복제본의 상태가 달라질 가능성이 크다.
- Object의 clone 메서드는 CloneNotSupportedException을 던진다고 선언했지만 재정의한 메서드는 그렇지 않다. public인 clone 메서드에서는 throws 절을 없애야 한다.
- 상속해서 쓰기 위한 클래스 설계 방식 두가지 중 어느쪽에서든, 상속용 클래스는 Cloneable을 구현해서는 안된다.
- Cloneable을 구현한 스레드 안전 클래스를 작성할 때는 clone 메서드 역시 적절히 동기화해줘야 한다.