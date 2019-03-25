# Item 30, 이왕이면 제네릭 메서드로 만들라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

### 제네릭 메서드의 간단한 예

- 로 타입을 이용해서 경고가 발생하는 경우
```Java
        // unchecked 관련 warning이 발생
        public static Set union(Set s1, Set s2) {
            Set result = new HashSet(s1);
            result.addAll(s2);
            return result;
        }
```

- 로 타입을 이용한 메서드에서 unchecked 경고를 없애려면 타입 안전하게 만들어야 한다. 메서드 선언에서의 로타입 컬렉션의 원소 타입을 타입 매개변수로 명시하고, 메서드 안에서도 이 타입 매개변수만 사용하게 수정하면 된다.
    - 타입 매개변수 목록은 메서드의 접근 제한자와 반환 타입 사이에 온다.

### 제네릭 메서드의 예 - 불변 객체를 여러 타입으로 활용할 수 있게 만들어야 할 때

- 제네릭은 런타임에 타입 정보가 소거되므로 하나의 객체를 어떤 타입으로든 매개변수화 할 수 있다. 하지만 이렇게 하려면 요청한 타입 매개변수에 맞게 매번 그 객체의 타입을 바꿔주는 정적 팩터리를 만들어야 한다. 이 패턴을 제네릭 싱글턴 팩터리라고 한다.
    - ex) Collections.emptySet
```Java
            // from java.util.Collections
            
            @SuppressWarnings("unchecked")
            public static final <T> Set<T> emptySet() {
                return (Set<T>) EMPTY_SET;
            }
```

- 예제) 항등 함수 만들기
    - 항등함수란 입력 값을 수정 없이 그대로 반환하는 특별한 함수.
    - 예제 코드 참고
- 재귀적 타입 한정(recursive type bound)
    - 자기 자신이 들어간 표현식을 사용하여 타입 매개변수의 허용 범위를 한정하는 방법
```Java
    public interface Comparable<T> {
        int compareTo(T o);
    }
```

- 설명
    - 여기서 타입 매개변수 T는 Comparable<T>를 구현한 타입이 비교할 수 있는 원소의 타입을 정의 한다.
    - Comparable을 구현한 원소의 컬렉션을 입력받는 메서드들은 주로 그 원소들을 정렬 혹은 검색하거나, 최솟값이나 최댓값을 구하는 식으로 사용된다.
        - ex) public static <E extends Comparable<E>> E max(Collection<E> c);
            - 모든 타입 E는 자신과 비교할 수 있다고 읽을 수 있다.
