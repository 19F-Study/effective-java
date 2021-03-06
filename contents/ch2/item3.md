# item3) private 생성자나 열거 타입으로 싱글턴임을 보증하라

## 싱글턴(singleton)이란?
인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.  

* 싱글턴을 만드는 방법
1) private 생성자 & 인스턴스에 접근 할 수 있는 public static 멤버  
2) private 생성자 & 정적 팩터리 메서드를 public static 멤버로 제공  
3) 원소가 하나인 열거 타입을 선언  

방법1의 장점 :  
해당 클래스가 싱글턴임이 API에 명백히 드러나며 간결함.  

방법2의 장점 :   
API를 변경하지 않고도 싱글턴이 아니게 변경 할 수 있다,  
정적 팰터리를 제네릭 싱글턴 팩터리로 만들 수 있다,  
정적 팩터리의 메서드 참조를 공급자로 사용할 수 있다.  

방법3의 장점 :  
더 간결하고 추가 노력 없이 직렬화 할 수 있다.  
복잡한 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 막아준다.  

* 싱글턴 클래스 직렬화  
방법1, 방법2로 만든 싱글턴 클래스를 직렬화 하는법
  1) 모든 인스튼스 필드를 transient로 선언
  2) readResolve 메서드를 제공  
-> 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화 할 때마다 새로운 인스턴스가 만들어진다.

> 출처  
Effective java 3/E (조슈아 블로크)
