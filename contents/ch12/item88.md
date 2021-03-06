# 아이템 88. readObject 메서드는 방어적으로 작성하라

### 예제
- 불볍인 날짜 점위 클래스를 만드느라 가변인 Date 필드를 이용 
- 불변식을 지키고 불변 유지를 위해 생성자와 접근자에서 Date 객체를 방어적으로 복사
- 이 클래스를 직렬화 해보자.
    1. Period 객체의 물리적 표현이 논리적 표현과 부합하므로 기본 직렬화 형태를 사용해도 괜찮을까?
        - 이 방법으로는 클래스의 주요한 불변식을 더는 보장할 수 없다!
    2. 왜?
        - readObject 메서드가 실질적으로는 또 다른 public 생성자이기 때문
        - 따라서 보통의 생성자처럼 readObject 메서드에서도 인수가 유효한지 검사해야하고, 필요하면 매개 변수를 방어적으로 복사해야 함
        - readObject가 이 작업을 수행하지 못하면 공격자는 아주 손쉽게 해당 클래스의 불변식을 꺠뜨릴 수 있음
        - readObject는 매개변수로 바이트 스트림을 받는 생성자이므로, 불변식을 깨뜨릴 의도로 임의 생성한 바이트 스트림을 건네면 문제가 발생함
- 위의 문제를 해결하기 위해
    1. Period의 readObject 메서드가 defaultReadObject를 호출한 다음 역직렬화된 객체가 유효한지 검사해야함
    2. 유효성 검사 실패시 InvalidObjectException를 통해 역직렬화가 일어나는 것을 방지
- 그래도 남아있는 문제
    - 위의 작업으로 공격자가 허용되지 않는 Period 인스턴스를 생성하는 일을 막을 수 있지만, 정상 Period 인스턴스에서 시작된 바이트 스트림 끝에 private Date 필드의 참조를 추가하면 가변 Period 인스턴스를 만들 수 있음
    - 공격자는 이 악의적인 객체 참조를 읽어 Period 내부의 정보를 얻을 수 있고, 이를 이용해 수정할 수 있음
    - 불변이 아니게 된다!
- 문제의 근원은 Period의 readObject 메서드가 방어적 복사를 충분히 하지 않았기 때문
    - 객체를 역직렬화할 때는 클라이언트가 소유해서는 안되는 객체 참조를 갖는 필드를 모두 반드시 방어적으로 복사해야한다. 
    - 따라서 readObject에서는 불변 클래스 안의 모든 private 가변 요소를 방어적으로 복사해야 한다.
    (final 필드는 방어적 복사가 불가능)
    
### 기본 readObject 메서드
- transient 필드를 제외한 모든 필드의 값을 매개변수로 받아 유효성 검사 없이 필드에 대입하는 public 생성자를 추가해도 괜찮은가?  
(답이 아니오 라면 커스텀 readObject 메서드를 만들거나 직렬화 프록시 패턴을 이용하자)
- final이 아닌 직렬화 가능 클래스라면 readObject 메서드도 재정의 가능한 메서드를 호출하면 안된다!
    - 위 규칙을 어기면, 해당 메서드가 재정의되면 하위 클래스의 상태가 완전히 역직렬화 되기 전에 하위 클래스에서 재정의된 메서드가 실행되고 이는 오동작이다.
    
### 정리
1. readObject 메서드를 작성할때는 public 생성자를 작성하는 자세로 임해야 한다.  
 **바이트 스트림이 진짜 직렬화된 인스턴스라고 가정하면 안된다**   
 (기본 직렬화 뿐 아니라 커스텀 직렬화를 사용한 경우도 문제가 발생할 수 있음)
2. 안전한 readObject 작성 지침
    1. private 이어야 하는 객체 참조 필드는 각 필드가 가리키는 객체를 방어적으로 복사하라. 불변 클래스 내의 가변 요소가 여기 속한다.
    2. 모든 불변식을 검사하여 어긋나는게 발견되면 InvalidObjectException을 던진다. 방어적 복사 다음에는 반드시 불변식 검사가 뒤따라야 한다.
    3. 역직렬화 후 객체 그래프 전체의 유효성을 검사해야 한다면 ObjectInputValidation 인터페이스를 사용하라.(책에서 다루지 않음)
    4. 직접적이든 간접적이든, 재정의할 수 있는 메서드를 호출하지 말자.