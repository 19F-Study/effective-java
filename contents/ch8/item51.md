# 메서드 시그니처를 신중히 설계하라
## 1. 메서드 이름을 신중히 짓자  
- 표준 명명 규칙을 따라야 한다. (item68)  
  - 예 ) camelCase, 동사나 동사구사용 (boolean의 경우 is,has로 시작)  
- 같은 패키지에 속한 다른 이름들과 일관되게 짓는다.  
- 개발자 커뮤니티에서 널리 받아들여지는 이름을 사용한다.  
- 긴 이름은 피한다.  
- 자바 라이브러리의 API 가이드를 참조한다.
  
## 2. 편의 메서드를 너무 많이 만들지 말자  
- 모든 메서드는 각각 자신의 소임을 다해야 한다.  
- 메서드가 너무 많으면 사용하고, 문서화하고, 테스트하고, 유지보수 하기 어렵다.  
- 확신이 서지 않으면 편의 메서드를 만들지 말자.

(isEmpty()같은 메서드가 편의메서드이지 않을까?)  

>[편의 메서드 (convenience method) 참고1](http://wiki.c2.com/?ConvenienceMethods)  
[편의 메서드 (convenience method) 참고2](https://en.wiktionary.org/wiki/convenience_method)


## 3. 매개변수 목록은 짧게 유지하자  
- 4개 이하가 좋다.  
- 같은 타입의 매개변수 여러개가 연달아 나오는 경우 특히 해롭다. 사용자가 순서를 기억하기 어렵고, 실수하더라도 그대로 컴파일된다.  

#### 긴 매개변수 목록을 짧게 줄여주는 세가지 방법  
1 . 여러 메서드로 쪼갠다. 
- 직교성을 높여 메서드 수를 줄여주는 효과    
예) List 인터페이스의 subList, indexOf  
- 직교성 (p.309)  
직교성이 높다는 것은 "공통점이 없는 기능들이 잘 분리되어있다", "기능을 원자적으로 쪼개 제공한다"는 의미.  
기능을 원자적으로 쪼개다 보면, 자연스럽게 중복이 줄고 결합성이 낮아져 코드를 수정하기 수월해진다.  

2 . 매개변수 여러 개를 묶어주는 도우미 클래스를 만든다.  
- 도우미 클래스는 정적 멤버 클래스로 둔다.  

3 . 앞서의 두 기법을 혼합한 것으로, 객체 생성에 사용한 빌더 패턴을 메서드 호출에 응용한다.  
- 매개변수가 많을 때, 특히 그 중 일부는 생략해도 괜찮을 때 도움이 된다.  

## 4. 매개변수의 타입으로는 클래스보다 인터페이스가 낫다  
* 인터페이스가 매개변수 타입이면 어떤 구현체도 인수로 건넬 수 있다.  

## 5. boolean 보다는 우너소 2개ㅈ짜리 열거 타입이 낫다  
* 메서드 이름상 boolean을 받아야 의미가 더 명확할 때는 예외다.  
* 열거타입을 사용하면 코드를 읽고 쓰기가 쉬워지며, 나중에 선택지를 추가하기도 쉽다.  