# Item 58. 전통적인 for 문보다는 for-each 문을 사용하라

## 비교해보자
```java
// for loop for collection
for (Iterator<Element> i = c.iterator(); i.hasNext(); ) {
  Element e = i.next();
  // e로 무언가를 한다.
}
```

```java
// for loop for array
for (int i = 0; i < a.length; i++) {
  // a[i]로 무언가를 한다.
}
```
- for 문은 while 문보다는 낫지만 가장 좋은 방법은 아니다. 반복자(iterator)와 인덱스 변수는 모두 코드를 지저분하게 할 뿐 우리에게 진짜 필요한건 원소들 뿐이다.
- 쓰이는 요소 종류가 늘어나면 오류가 생길 가능성이 높아진다. 혹시라도 잘못된 변수를 사용했을 때 컴파일러가 잡아주지라는 보장도 없다.
- 컬렉션이냐 배열이냐에 따라 코드 형태가 상당히 달라지므로 주의해야 한다.

```java
// for-each loop
for (Element e : elements) {  // 여기서 : 은 in (안의) 라고 읽으면 된다.
  // e로 무언가를 한다.
}
```
- 위의 모든 문제는 for-each 문을 사용해서 해결할 수 있다.
- 정식 이름은 향상된 for 문 (enhanced for statement) 이다.
- 반복자와 인덱스 변수를 사용하지 않으니 코드가 깔끔해지고 오류가 날 일도 없다.
- 하나의 관용구로 collection 과 array를 모두 처리할 수 있어서 어떤 컨테이너를 다루는지는 신경쓰지 않아도 된다. 속도도 동일하다.

<br>

## 반복문을 중첩해서 사용해야 한다면 for-each문의 이점이 더욱 커진다

```java
enum Suit {CLUB, DIAMOND, HEART, SPADE}
enum Rank {ACE, DEUCE, THREE, FOUR, FIVE, SIX …}

…

static Collection<Suit> suits = Arrays.asList(Suit.values());
static Collection<Rank> ranks = Arrays.asList(Rank.values());

List<Card> deck = new ArrayList<>();
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
  for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); ) {
    deck.add(new Card(i.next(), j.next()));
  }
}
```
- 위 코드는 NoSuchElementException() 을 던진다.

```java
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
  Suit suit = i.next();
  for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); ) {
    deck.add(new Card(suit, j.next()));
  }
}
* 문제는 고쳤지만 보기 좋진 않다.

for (Suit suit : suits) {
  for (Rank rank : ranks) {
    deck.add(new Card(suit, rank));
  }
}
```
- for-each 문을 중첩하는 것으로 이 문제는 간단히 해결된다. 코드도 놀랄만큼 간결해진다.

<br>

## 하지만 for-each문을 사용할 수 없는 상황이 세 가지 존재한다
**파괴적인 필터링 ( destructive filtering )**
- 컬렉션을 순회하면서 선택된 원소를 제거해야 한다면 반복자의 remove 메서드를 호출해야 한다.
- 자바 8부터는 Collection의 removeIf 메서드를 사용해 컬렉션을 명시적으로 순회하는 일을 피할 수 있다.

**변형 ( transforming )**
- 리스트나 배열을 순회하면서 그 원소의 값 일부 혹은 전체를 교체해야 한다면 리스트의 반복자나 배열의 인덱스를 사용해야 한다.

**병렬 반복 ( parallel iteration )**
- 여러 컬렉션을 병렬로 순회해야 한다면 각각의 반복자와 인덱스 변수를 사용해 엄격하고 명시적으로 제어해야 한다.

<br>

## 핵심 정리
- 전통적인 for 문과 비교했을 때 for-each 문은 명료하고, 유연하고, 버그를 예방해준다.
- 성능 저하도 없다. 가능한 모든 곳에서 for 문이 아닌 for-each 문을 사용하자.

