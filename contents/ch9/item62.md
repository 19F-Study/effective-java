# item62. 다른 타입이 적절하다면 문자열 사용을 피하라  
- 문자열은 텍스트 표현과 처리에 걸맞도록 설계되었다.  
- 데이터의 자료형에 맞는 타입을 사용해야한다. 적당한 자료형이 없다면 만들어야 한다.  
예) 숫자면 int, float, BigInteger 같은 수 자료형 (numeric type), 예/아니오의 답이면 boolean 등.

## 문자열의 부적합한 사용
1. 문자열은 enum 자료형을 대신하기에는 부족하다. enum은 문자열보다 훨씬 좋은 열거 자료 상수들을 만들어 낸다.  
2. 문자열은 혼합 자료형(aggregate type)을 대신하기엔 부족하다.  
3. 문자열은 권한 (capability)을 표현하기엔 부족하다.  
```java
// 문자열을 혼합 자료형으로 사용한 부적절한 사례. 혼합 자료형을 표현할 클래스를 만들자.
String compoundKey = className + "#" + i.next();
```

## 요약
1. 더 좋은 자료형이 있거나 만들 수 있을 때는 만들어서 사용하자.  
2. 문자열이 적합하지 못한 자료형으로는 기본 자료형, enum, 혼합 자료형 등이 있다.  