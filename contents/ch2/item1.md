# item1) 생성자 대신 정적 팩터리 메서드를 고려하라  

## 클라이언트가 클래스의 인스턴스를 얻는 방법  
1) public 생성자  
2) 정적 팩터리 메서드 (static factory method)  
    (클래스의 인스턴스를 반환하는 단순한 정적 메서드)  
    
## 정적 팩터리 메서드의 장점  
1) 이름을 가질 수 있다.  
2) 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.  
  인스턴스 통제 (instance-controlled) 클래스  
  싱글턴, 인스턴스화 불가, 불변 값 클래스에서 동치인 인스턴스가 단 하나임을 보장  
  -> 플라이웨이 패턴의 근간.  
  열거타입은 인스턴스가 하나만 만들어짐을 보장.
3) 반환 티입의 하위 타입 객체를 반환할 수 있다.  
  자바 8부터는 인스페이스에 정적 메서드를 선언 가능  
  예) java.util.Collections
4) 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환가능.  
  반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관 없다.  
  성능 개선 시 장점.  
5) 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.  

## 정적 팩터리 메서드의 단점  
1) 상속을 하려면 public이나 protected 생성자가 필요하므로, 정적 팩터리 메서드만 제공하면  
하위 클래스를 만들 수 없다.
2) 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.

## 정적 팩터리 메서드에서 사용되는 명명 방식  
  * from : 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드  
  예) Date d = Date.from(instant);
  
  * of : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드  
  예) Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);  
  
  * valueOf : from과 of의 더 자세한 버전  
  예) BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);  
  
  * instance 혹은 getInstance : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만,  
  같은 인스턴스임을 보장하지는 않는다.  
  예) StackWalker luke = StackWalker.getInstance(options);  
  
  * create 혹은 newInstance : instance 혹은 getInstance와 같지만,  
  매번 새로운 인스턴스를 생성해 반환함을 보장한다.  
  예) Object newArray = Array.newInstance(classObject, arrayLen);  
  com.google.common.collect.Lists.newArrayList();  
  
  * getType : getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다.  
  "Type"은 팩터리 메서드가 반환할 객체의 타입이다.  
  예) FileStore fs = Files.getFileStore(path);  
  
  * newType : newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다.  
  "Type"은 팩터리 메서드가 반환할 객체의 타입이다.  
  예) BufferedReader br = Files.newBufferedReader(path);  
  
  * type : getType과 newType의 간결한 버전  
  예) List<Complaint> litany = Collections.list(legacyLitany);
  
* 요약  
public 생성자와 정적 팩터리 메서드의 장단점을 이해하고 사용하는 것이 좋다.  
정적 팩터리를 사용하는게 유리한 경우가 많으므로, public 생성자를 제공하던 습관이 있다면 고치자.

> 출처  
Effective java 3/E (조슈아 블로크)

> 참고자료  
[플라이웨이트 패턴 위키피디아](https://ko.wikipedia.org/wiki/%ED%94%8C%EB%9D%BC%EC%9D%B4%EC%9B%A8%EC%9D%B4%ED%8A%B8_%ED%8C%A8%ED%84%B4)  
[플라이웨이트 패턴 참고](https://programmingfbf7290.tistory.com/entry/%ED%94%8C%EB%9D%BC%EC%9D%B4%EC%9B%A8%EC%9D%B4%ED%8A%B8-%ED%8C%A8%ED%84%B4)  
[Java9 docs](https://docs.oracle.com/javase/9/docs/api/java/util/List.html)  
[Java9 new features](https://www.oracle.com/corporate/features/jdk9-new-developer-features.html)