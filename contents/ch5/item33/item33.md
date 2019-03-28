# item33. 타입 안전 이종 컨테이너를 고려하라

## 요약  
* 일반적인 제네릭 형태에서는 컨테이너를 매개변수화 하므로, 한 컨테이너가 다룰 수 있는 타입 매개변수의 수가 고정되어 있다.  
예)  
Set : 원소의 타입을 뜻하는 하나의 타입 매개변수,   
Map : 키와 값의 타입을 뜻하는 두개의 타입 매개변수

* 컨테이너 대신 키를 타입 매개변수로 바꾸면, 더 유연한 타입 안전 이종 컨테이너를 만들 수 있다.  

## 타입 안전 이종 컨테이너 패턴  
* 컨테이너 대신 키를 매개변수화한 다음, 컨테이너에 값을 넣거나 뺄 때 매개변수화한 키를 함께 제공한다.  
* 이렇게 하면 제네릭 타입 시스템이 값의 타입이 키와 같음을 보장해줄 것이다.  
* 타입 안전 이종컨테이너는 Class를 키로 쓴다.
* 타입 토큰 : 컴파일타임 타입 정보와 런타임 타입 정보를 알아내기 위해 메서드들이 주고받는 것 (예제에서는 class 리터럴)  
* 직접 구현한 키 타입도 쓸 수 있다.  

예제) kr._19fstudy.effective_java.ch5.item33.Favorites  

```java
public class Favorites {
	public <T> void putFavorite(Class<T> type, T instance);
	public <T> T getFavorit(Class<T> type);
}
```  

* Favorites 는 타입 안전 이종 컨테이너이다.  
  - 모든 키의 타입이 제각각이라, 일반적인 맵과 달리 여러 가지 타입의 원소를 담을 수 있다.
  - 타입 안전하다. 즉, String을 요청했는데 Integer를 반환하는 일은 절대 없다.  

  
```java
private Map<Class<?>, Object> favorites = new HashMap<>();
```
- Map이 아닌 키가 와일드 카드 타입이다.  
- 모든 키가 서로 다른 매개변수화 타입일 수 있으므로 다양한 타입을 지원할 수 있다.  

Favorites의 제약사항  
1. 악의적인 클라이언트가 Class객체를 raw 타입으로 넘기면 타입 안전성이 쉽게 깨진다.  
-> 동적 형변환으로 런타임 타입 안전성 확보 (p.202)  
-> java.util.Collections 의 checkedSet, checkedList, checkedMap  
  : 이런 컬렉션 래퍼들은 제네릭과 raw 타입을 섞어서 사용하는 애플리케이션에서 클라이언트 코드가 컬렉션에  
  잘못된 타입의 원소를 넣지 못하게 추적하는데 도움을 준다.    
2. 실체화 불가 타입에는 사용할 수 없다.  
-> String이나 String[]은 저장할 수 있지만 List<String>은 저장할 수 없다.  
-> List<String> 용 Class 객체를 얻을 수 없기 때문. List<String>.class 는 문법 오류.
-> List<String> 과 List<Integer> 는 List.class 라는 같은 Class 객체를 공유.  

## 슈퍼 타입 토큰
Favorites의 두번째 제약사항을 슈퍼 타입 토큰(super type token)으로 해결하려는 시도도 있다.  
(Spring에서 제공 : org.springframework.core.ParameterizedTypeReference)  

[슈퍼 타입 토큰 참조](http://gafter.blogspot.com/2006/12/super-type-tokens.html)  
슈퍼 타입 토큰에도 한계가 존재하며, 책의 저자가 언급하지 않고 넘어간 이유도 이 한계 때문일 것이다.


## 한정적 타입 토큰  
* Favorites이 사용하는 타입 토큰은 비한정적이다.  
 -> getFavorite과 putFavorite은 어떤 Class객체든 받아들인다.  
* 이 메서드들이 허용하는 타입을 제한하고 싶을 때는 한정적 타입 토큰을 활용하자.  
* 한정적 타입 토큰이란 한정적 타입 매개변수나 한정적 와일드카드를 사용하여 표현 가능한 타입을 제한하는 타입 토큰이다.  
* 애너테이션 API(item 39)는 한정적 타입 토큰을 적극적으로 사용한다.  

```java
// AnnotationElement.java
public final <A> A getAnnotation(Class<? extends Annotation> annotationType) {
    Objects.requireNonNull(annotationType);
    return type.getAnnotation(annotationType);
}

// p. 204 - annotationType 인수는 애너테이션 타입을 뜻하는 한정적 타입 토큰이다.
// 토큰으로 명시한 타입의 애너테이션이 대상 요소에 달려있다면 그 애너테이션을 반환하고 없다면 null을 반환.
// 애너테이션된 요소는 그 키가 애너테이션 타입인, 타입 안전 이종 컨테이너이다.
public <T extends Annotation> T getAnnotation(Class<T> annotationType);
```

* Class<?> 타입의 객체가 있고, 이를 (getAnnotation 처럼)한정적 타입 토큰을 받는 메서드에 넘기려면 ?  
-> 객체를 Class<? extends Annotation>으로 형변환 : 이는 비검사이므로 컴파일 경고가 뜬다.  
-> Class 클래스가 이런 형변환을 동적으로 안전하게 해주는 인스턴스 메서드를 제공 : asSubclass

* asSubclass : 호출된 인스턴스 자신의 Class 객체를 인수가 명시한 클래스로 형변환.  
형변환이 된다는 것은 이 클래스가 인수로 명시한 크래스의 하위 클래스라는 뜻이다. 


> 참고자료  
[typesafe_heterogeneous_container](https://gerardnico.com/code/design_pattern/typesafe_heterogeneous_container)  

http://idlebrains.org/tutorials/java-tutorials/effective-java-typesafe-heterogeneous-container-pattern/