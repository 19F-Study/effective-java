# 아이템 40. @Override 애너테이션을 일관되게 사용하라.

> @Override : 상위 타입의 메서드를 재정의.

```java
  public class Bigram {
    private final char first;
    private final char second;
  
    public Bigram(char first, char second) {
      this.first = first;
      this.second = second;
    }
  
    public boolean equals(Bigram b) {
      return b.first == first && b.second == second;
    }
  
    public int hashCode() {
      return 31 * first + second;
    }
  
    public String toString() {
      return "first : " + first + ", second : " + second;
    }
  
    public static void main(String[] args) {
      Set<Bigram> s = new HashSet<>();
      // "a ~ z" 넣기를 10번 반복
      for (int i = 0; i < 10; i++) {
        //a ~ z 넣기
        for (char ch = 'a' ; ch <= 'z' ; ch++)
          s.add(new Bigram(ch, ch));
      }
      System.out.println(s.size());
      s.forEach((value) -> {
        System.out.println(value);
      });
    }
  }
```

 - set.size() 가 260인 이유는?
 - HashSet.add() 에서 equals() 가 호출되지 않은 이유는? << 예전에 얘기했던 부분 찾아보자!
 - toString() 은 Override 하지 않았음에도 불구하고 System.out.println() 에서 호출된 이유는?
 - 상위 클래스의 method(Object o) 를 @Override 할때 method(ExampleClass o) 가 안되는 이유는?
 
 [참고] https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html
 >You cannot declare more than one method with the same name and the same number and type of arguments, because the compiler cannot tell them apart.
  The compiler does not consider return type when differentiating methods, so you cannot declare two methods with the same signature even if they have a different return type.
 
### 상위 클래스의 메서드를 재정의하려는 모든 메서드에 @Override 애너테이션을 달자. 
 - 내생각 : (컴파일러의 도움으로)실수를 줄이기위해! @Override 가 달려있는 클래스를 다시 구현, 상속받는 클래스에게 정보를 전달하기 위해!
 - 한가지 예외 : 구체 클래스(?)에서 상위 클래스의 추상 메서드를 재정의할때는 굳이 @Override 를 달지 않아도 된다. 구현해야할 추상 메서드가 남아 있다면 컴파일러가 알려줌.
 - 인터페이스의 디폴트 메서드를 정의할때도 @Override 를 달아준다면 시그니처가 올바른지 확인할 수 있다.
 - 추상 클래스나 인터페이스 에서는 상위 클래스나 상위 인터페이스의 메서드를 재정의하는 모든 메서드에 @Override 달아주는게 좋다. (Set 에 애너테이션 안달려있는데;;)