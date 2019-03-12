# Item 15. 클래스와 멤버의 접근 권한을 최소화하라

## 정보 은닉의 장점
  - 시스템 개발 속도를 높인다.
  - 시스템 관리 비용을 낮춘다.
  - 성능 최적화에 도움을 준다.
  - 소프트웨어 재사용성을 높인다.
  - 큰 시스템을 제작하는 난이도를 낮춘다.
  
## 모든 클래스와 멤버의 접근성을 가능한 한 좁혀야 한다!
  - Tob level (It's simply any class that's not a nested class) 클래스와 인터페이스에 부여할 수 있는 package-private, public
  - 패키지 내에서만 사용한다면 package-private 으로 선언. 다음 릴리스에서 클라이언트의 하위호환성을 고려하지 않아도 된다.
  - 한 클래스에서만 사용하는 package-private 톱 레벨 클래스나 인터페이스는 이를 사용하는 클래스 안에 private static 으로 중첩 (NestedPackagePrivateExample)
  
## 멤버에 부여할 수 있는 접근 수준
  - private : 멤버를 선언한 톱레벨 클래스에서만 접근 가능.
  - package-private : 멤버가 소속된 패키지 안의 모든 클래스에서 접근 가능. 접근 제한자를 명시하지 않을때 적용되는 패키지 접근 수준.
  - protected : package-private 의 접근 범위를 포함하며, 이 멤버를 선언한 클래스의 하위 클래스에서도 접근 할 수 있다.
  - public : 모든 곳에서 접근할 수 있다.
  - 외부에서 접근이 필요한 멤버들에게 public 접근 제한자 설정 -> 나머지는 private -> package-private 설정.
  - 단, Serializable 을 구현한 클래스에서는 그 필드(private, package-private)도 의도치 않게 공개 API 가 될 수 있다.
  - 상위 클래스의 메서드를 재정의할 떄는 그 접근 수준을 상위 클래스에서보다 좁게 설정할 수 없다. (ExtendExample)
  - 테스트를 위해서 접븐 범위를 넓히는건 private -> package-private 까지만!
  - public 클래스의 인스턴스 필드는 되도록 public 이 아니여야 한다.
    - 가변 객체를 참조하거나, final 이 아닌 인스턴스 필드를 public 으로 선언하면 그 필드에 담을 수 있는 값을 제한할 힘을 잃게 된다.
    - 쓰레드 안전하지 않다. getter, setter 메서드에 synchronized 를 걸 수 있지만 직접 접근할때는 불가능해서??
  - 추상개념을 완성하는데 꼭 필요한 상수라면 public static final 필드로 공개해도 좋다.
    - 반드시 기본 타입 값이나 불변 객체를 참조.
    - public static final 배열 필드를 두거나 이 필드를 반환하는 접근자 메서드를 제공해서는 안된다. 클라이언트가 수정 가능.
      - Immutable List 로 생성
      - 복사본을 반환하는 getter
 
## 자바 9의 모듈 시스템
  - 모듈은 패키지들의 묶음.
  - 모듈은 자신에 속한 패키지 중 공개할 것을 선언.
  - public, protected 라 하더라도 공개 패키지가 아니라면 접근 불가능. << 예제 코드
  