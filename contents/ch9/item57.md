# Item 57. 지역변수의 범위를 최소화하라

## 지역변수 (Local Variable)
- Local variables are declared in methods, constructors, or blocks.
- Local variables are created when the method, constructor or block is entered and the variable will be destroyed once it exits the method, constructor, or block.
- Access modifiers cannot be used for local variables.
- Local variables are visible only within the declared method, constructor, or block.
- Local variables are implemented at stack level internally.
- There is no default value for local variables, so local variables should be declared and an initial value should be assigned before the first use.
- ref: [https://www.tutorialspoint.com/java/java_variable_types.htm](https://www.tutorialspoint.com/java/java_variable_types.htm)

<br>

## 지역변수의 범위를 최소화하라
지역변수의 유효 범위를 최소로 줄이면 코드 가독성과 유지보수성이 높아지고 오류 가능성은 낮아진다.

**가장 처음 쓰일 때 선언하라**
- 사용하려면 멀었는데, 미리 선언부터 해두면 코드가 어수선해져 가독성이 떨어진다. 변수를 실제로 사용하는 시점엔 타입과 초깃값이 기억나지 않을 수도 있다.
- 지역변수의 범위는 선언된 지점부터 그 지점을 포함한 블록이 끝날 때 까지이므로, 실제 사용하는 블록 바깥에 선언된 변수는 그 블록이 끝난 뒤까지 살아 있게 된다.

**거의 모든 지역변수는 선언과 동시에 초기화하라**
- 만약 선언을 하고, 초기화를 뒤로 미룬다면, 지역변수의 범위가 쓸데없이 늘어나게 될 것이다.
- 초기화에 필요한 정보가 충분하지 않다면 충분해질 때까지 선언을 미뤄야 한다.

**메서드를 작게 유지하고 한 가지 기능에 집중하라**
- 한 메서드에서 여러 가지 기능을 처리한다면 그중 한 기능과만 관련된 지역변수라도 다른 기능을 수행하는 코드에서 접근할 수 있을 것이다. 이럴 땐 메서드를 기능별로 쪼개라.

<br>

## for 문은 독특한 방식으로 변수 범위를 최소화해준다
- 반복문에서는 반복 변수 (loop variable)의 범위가 반복문의 몸체, 그리고 for 키워드와 몸체 사이의 괄호 안으로 제한된다. 따라서 반복 변수의 값을 반복문이 종료된 뒤에도 써야 하는 상황이 아니라면 while문 보다는 for 문을 쓰는 편이 낫다.
- 변수 유효 범위가 for 문 범위와 일치하여 똑같은 이름의 변수를 여러 반복문에서 써도 서로 아무런 영향을 주지 않는다.
- for 문은 while문보다 짧아서 가독성이 좋다.

```java
for (Element e: c) {
  // e로 무언가를 한다.
}
```

```java
for (Iterator<Element> i = c.iterator(); i.hasNext(); ) {
  Element e = i.next();
  // e와 i로 무언가를 한다.
}
```

```java
// while보다 for가 더 나은 이유를 알 수 있는 예제 코드
// while
Iterator<Element> i = c.iterator();
while (i.hasNext()) {
  doSomething(i.next());
}

…

Iterator<Element> i2 = c2.iterator();
while (i.hasNext()) { // bug!
  doSomethingElse(i2.next());
}

// for
for (Iterator<Element> i = c.iterator(); i.hasNext();) {
  Element e = i.next();
  // e와 i로 무언가를 한다.
}

…

for (Iterator<Element> i2 = c2.iterator(); i.hasNext();) { // i를 찾을 수 없다는 컴파일 오류를 낸다.
  Element e2 = i2.next();
  // e2와 i2로 무언가를 한다.
}
```

```java
// 같은 값을 반환하는 메서드를 매번 호출한다면, 이 관용구를 사용하기 바란다.
for (int i=0, n=expensiveComputation(); i<n; i++) {
  // i로 무언가를 한다.
}
```
