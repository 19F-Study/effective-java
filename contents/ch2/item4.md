## Item4. 인스턴스화를 막으려거든 private 생성자를 사용하라.

<br>

### 인스턴스화를 막는다
인스턴스화를 막는다는건 클래스의 인스턴스 생성을 불가능하게 한다는 의미이다.  
객체 지향적인 관점에서 봤을 때, 이 말에 의문이 들 수 있을 것이다. 객체를 생성하기 위해 클래스를 정의하고 클래스의 인스턴스를 생성하게 되는데, 이를 하지 못하게 할거면 클래스가 존재할 이유가 없지 않은가.  
하지만, 분명 나름의 쓰임새가 있다. 그 쓰임새를 생각해보자.
1. java.lang.Math와 java.util.Arrays 처럼 기본 타입 값이나 배열 관련 메서드들을 모아놓을 수 있다.
2. java.util.Collections 처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드들을 모아놓을 수 있다.
3. final 클래스와 관련한 메서드들을 모아놓을 떄도 사용한다.

책에서는 위와 같이 예를 들어주고 있다. 즉, 정적 메서드와 정적 필드만을 담은 클래스를 구현해야하는 경우가 있을 수 있다는 것이다.    
조금 더 이해를 돕기 위해 아래에 AwsS3Utils라는 간단한 클래스를 예시로 만들었다. 이는 s3에 접근하여 CRUD를 제공해주는 유틸리티 클래스이다. 이런 클래스가 aws s3를 사용하는 프로젝트에 있다면, 여러 모듈에서 사용할 수 있으므로 매우 편리하게 많은 곳에서 재사용할 수 있을 것이다. 
```java

public class AwsS3Utils {

  public static boolean isValidUri(String s3uri)
  
  public static boolean createFile(String s3uri, File file)
  
  public static boolean readFile(String s3uri)
  
  public static boolean updateFile(String s3uri, File file)
  
  public static boolean deleteFile(String s3uri)
  
  ...
}
```

_정적 멤버만 담은 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 게 아니다._   
_단지 정적 메서드와 정적 필드로 구성된 유틸성 클래스일 뿐이다._ 

<br>

### private 생성자를 사용하라

위의 클래스들의 경우, 생성자는 명시하지 않으면 컴파일러가 자동으로 기본 생성자를 만들어준다. 즉, 매개변수를 받지 않는 public 생성가자 만들어지며, 사용자는 이 생성자가 자동 생성된 것인지 구분할 수 없다.  
아주 간단한 해결책은 private 생성자를 추가하여 클래스의 인스턴스화를 막는 것이다.

```java

public class AwsS3Utils {
  
  // 기본 생성자가 만들어지는 것을 막는다 (인스턴스화 방지용)
  private AwsS3Utils() {
      throw new AssertionError();
  }

  public static boolean isValidUri(String s3uri)
  
  public static boolean createFile(String s3uri, File file)
  
  public static boolean readFile(String s3uri)
  
  public static boolean updateFile(String s3uri, File file)
  
  public static boolean deleteFile(String s3uri)
  
  ...
}
```


