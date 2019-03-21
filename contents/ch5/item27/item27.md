# Item27. 비검사 경고를 제거하라

```java
Set<Lark> exaltaion = new HashSet();

Set<Lark> exaltaion = new HashSet<>(); //Lark 로 추론한다.
```



### 할수 있는 한 모든 경고를 제거하라!

- 경고를 제거할 수는 없지만 타입 안전하다고 확신할 수 있다면 @SuppressWarnings("unchecked")를 달아 경고를 숨겨!
- @SuppressWarings("unchecked") 는 가능한 한 좁은 범위에 적용. 절대로 클래스 전체에 적용해서는 안된다.
- 한 줄이 넘는 메서드나 생성자에 달린  @SupressWarings("unchecked") 는 지역변수 선언 쪽으로 옮긴다.

```java
public <T> T[] toArray(T[] a) {
  if (a.length < size)
    return (T[]) Arrays.copyOf(elements, size, a.getClass());
  System.arraycopy(elements, 0, a, 0, size);
  if (a.length > size)
    a[size] = null;
  return a;
}
```

