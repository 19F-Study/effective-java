# Item 66, 네이티브 메서드는 신중히 사용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

## JNI란?

- 자바 네이티브 인터페이스(Java Native Interface, JNI)는 자바 프로그램이 네이티브 메서드를 호출하는 기술이다. 여기서 네이티브 메서드란 C나 C++ 같은 네이티브 프로그래밍 언어로 작성한 메서드를 말한다.

- JNI의 주요 쓰임
    - 레지스트리 같은 플랫폼 특화 기능
    - 네이티브 코드로 작성된 기존 라이브러리
    - 성능 개선을 목적으로 성능에 결정적인 영향을 주는 영역만 따로 네이티브 언어로 작성

- 성능을 개선할 목적으로 네이티브 메서드를 사용하는 것은 거의 권장하지 않는다.
    - 자바 초기 시절이라면 이야기가 다르지만, JVM은 그동안 엄청난 속도로 발전해왔다. 대부분 작업에서 지금의 자바는 다른 플랫폼에 견줄만한 성능을 보인다.

- 네이티브 메서드의 단점
    - 네이티브 언어가 안전하지 않으므로 네이티브 메서드를 사용하는 애플리케이션도 메모리 훼손 오류로부터 더 이상 안전하지 않다.
    - 네이티브 언어는 자바보다 플랫폼을 많이 타서 이식성이 낮다.
    - 디버깅도 더 어렵다.
    - 주의하지 않으면 속도가 오히려 느려질 수도 있다.
    - 가비지 컬렉터가 네이티브 메모리는 자동 회수하지 못 하고 심지어 추적조차 할 수 없다.
    - 자바와 네이티브 코드의 경계를 넘나들 때마다 비용도 추가된다.
    - 네이티브 메서드와 자바 코드 사아의 '접착 코드(glue code)'를 작성해야 하는데, 이는 귀찮은 작업이고 가독성도 떨어진다.

## JNI More

- [https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaNativeInterface.html](https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaNativeInterface.html)