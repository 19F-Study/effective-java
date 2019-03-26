 # 아이템32. 제네릭과 가변인수를 함께 쓸 때는 신중하라

- 가변인수 메서드를 호출하면 가변인수를 담기 위한 배열이 자동으로 하나 만들어진다.
- 내부로 감춰야 했을 이 배열을 그만 클라이언트에 노출하는 문제가 생김
  - 클라이언트에서 어떤 타입을 배열에 담을지에 따라 달라지니까??!
  - 클라이언트가 가변인수를 쓰면 배열이 생성된다는 것을 아니까??!
- 그 결과 varagrs 매개변수에 제네릭이나 매개변수화 타입이 포함되면 알기 어러운 컴파일 경고가 발생한다.

(Varargs.java)
```java
//코드 28-3
static void dangerous() {
	List<String>[] stringLists = new List<String>[1];
	List<Integer> intList = List.of(42);
	Object[] objects = stringLists;
	objects[0] = intList;
	String s = stringLists[0].get(0);
}
```

```java
//코드 32-1
static void dangerous(List<String>... stringLists) {
	//형변환은 어디에?
	//1
	List<Integer> intList = List.of(42);
	//2
	Object[] objects = stringLists;
	//3
	objects[0] = intList;
	//4
	String s = stringLists[0].get(0);
}
```

- 보이지 않는 형 변환이 숨어있기 때문에 varargs 배열 배개변수에 값을 저장하는 것은 안전하지 않다! (ClassCastException 발생) 
- 28-3 은 컴파일 에러가 나지만 32-1 은 경고만 발생하는 이유는?
  - 제네릭이나 매개변수화 타입의 varagrs 매개변수를 받는 메서드는 매우 유용!
  - Arrays.asList(T... a), Colletions.addAll(Collection<? super T> c, T... elemets), EnumSet.of(E first, E...rest)
  
 ## @SafeVarargs
 - 메서드 작성자가 그 메서드가 타입 안전함을 보장하는 장치.
 - 클라이언트 측에서 발생하는 경고를 숨길 수 있다.
 - 타입 안전할때만 사용해야 한다!
 
 ## 메서드가 타입 안전한지 판단하는 방법
 - 가변인수 메서드를 호출할 때 varargs 매개변수를 담는 제네릭 배열이 만들어진다.
 - 이 배열에 아무것도 저장하지 않는다.
 - 배열의 참조가 밖으로 노출되지 않는다.
 - 즉, varargs 매개변수 배열이 호출자로부터 그 메서드로 순수하게 인수들을 전달만 할 때!
 
 ## 제네릭 매개변수 참조를 노출한 예제
 ```java
  static <T> T[] toArray(T... args) {
	  return args;
  }
  ```
  - 메서드가 반환하는 배열의 타입은 이 메서드에 인수를 넘기는 컴파일 타임에 결정.
  - 컴파일러에게 충분한 정보가 주어지지 않아 타입을 잘못 판단할 수 있다.
  
  ```java
    static <T> T[] pickTwo(T a, T b, T c) {
	      switch(ThreadLocalRandom.current().nextInt(3)) {
	      	case 0: return toArray(a, b);
	      	case 1: return toArray(a, c);
	      	case 2: return toArray(b, c);
	      }
    }
  ```
  ```java
    	public static void main(String[] args) throws Exception {
    		String[] result = pickTwo("GOOD", "FAST", "CHEAP");
    	}
  ```
  - pickTwo 메서드의 반환값은 Object[]
  - pickTwo 의 반환값을 String[] 로 변환하기 위해 자동 형변환이 일어난다.
  - Object[] 는 String[] 의 하위 타입이 아니므로 ClassCastException 발생.
  
  ## 제네릭 매개변수 참조를 노출해도 안전한 두가지 예외
  - @SafeVarargs 로 제대로 애노테이트왼 또 다른 varargs 메서드에 넘기는 것.
  - 배열 내용의 일부 함수를 호출하는 (varargs를 받지 않는)일반 메서드에 넘기는 것.
    - 일부를 넘길때는 리턴 과정에서 이미 타입 변환이 일어나기 때문에? (PickTwo.returnOnlyOne())
    - 일부를 넘기기 위해서 sub array 를 만들기 때문에? (PickTwo.toArrayReturnSubArray())
  
  ## 제네릭 varargs 매개변수를 안전하게 사용한 예제
  ```java
    @SafeVarargs
    static <T> List<T> flatten(List<? extends T>... lists) {
	      List<T> result = new ArrayList<>();
	      for (List<? extends T> list : lists)
	      	result.addAll(list);
	      return result;
    }
  ```
  - 임의 개수의 리스트를 인수로 받아, 받은 순서대로 모든 원소를 하나의 리스트로 옮겨 담아 반환.
  
  ## @SafeVarargs 를 사용하지 않은 다른 예제
  ```java
    static <T> List<T> flatten(List<List<? extends T>> lists) {
	      List<T> result = new ArrayList<>();
	      for (List<? extends T> list : lists)
	      	result.addAll(list);
	      return result;
    }    
  ```
  - varargs 매개변수를 List 매개변수로 바꿀 수 있다.
  - 정적 팩터리 메서드인 List.of 를 활용하면 임의개수의 인수를 넘길 수 있다. (List.of 에도 @SafeVarargs 가 달려있다.)
  - 컴파일러가 이 메서드의 타입 안전성을 검증할 수 있다.
  
  ## pickTwo 메서드 수정
  ```java
    static <T> List<T> pickTwo(T a, T b, T c) {
	      switch(ThreadLocalRandom.current().nextInt(3)) {
	      	case 0: return List.of(a, b);
	      	case 1: return List.of(a, c);
	      	case 2: return List.of(b, c);
	      }
	      throw new Exception();
    }
    
    public static void main(String[] args) {
	      List<String> attributes = pickTwo("좋은", "빠른", "저렴한");
    }
  ```