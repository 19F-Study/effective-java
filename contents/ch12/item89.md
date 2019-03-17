# item89) 인스턴스 수를 통제해야 한다면 readResolve보다는 열거 타입을 사용하라

## readResolve  
readObject가 만들어낸 인스턴스를 다른 것으로 대체할 수 있다.  
이를 사용하여 Serializable을 구현한 클래스가 싱글턴을 유지할 수 있다.  
ObjectInputStream가 스트림으로부터 객체를 읽어서 리턴할 때,   
readResolve 메서드가 정의되어있다면 readResolve의 반환값을 리턴한다.  
[참고자료](https://www.javapedia.net/Object,-Class-and-Package/1566)

readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야한다.  
싱글턴이 transient가 아닌 참조 필드를 가지고 있다면, 그 필드의 내용은 readResolve 메서드가 실행되지 전에 역직렬화 된다.  
역직렬화 되는 시점에 인스턴스의 참조를 훔쳐올 수 있으므로 transient로 선언해야한다.  

- readResolve의 접근성  
final 클래스라면 private으로 지정해야한다. final 클래스가 아닌 경우,  
  - private로 선언 : 하위 클래스에서 사용 불가
  - package-private : 같은 패키지에 속한 하위 클래스에서만 사용 가능
  - protected 나 public : 모든 하위 클래스에서 사용 가능.  
  하위클래스에서 재정의 하지 않았다면, 하위 클래스의 인스턴스를 역직렬화하면  
  상위 클래스의 인스턴스를 생성하여 ClassCastException을 일으킬 수 있다. 

## 열거 타입 싱글턴
직렬화 가능한 인스턴스 통제 클래스를 열거 타입을 이용해 구현하면,  
선언한 상수 외의 다른 객체가 존재하지 않음을 자바가 보장해준다.
불변식을 지키기 위해 인스턴스를 통제해야 한다면 가능한 한 열거 타입을 사용하자.  

> 출처  
Effective java 3/E (조슈아 블로크)