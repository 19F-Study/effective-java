# Item 23, 태그 달린 클래스보다는 클래스 계층구조를 활용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

### 태그 달린 클래스

- 두 가지 이상의 의미를 표현할 수 있으며, 그 중 현재 표현하는 의미를 태그 값으로 알려주는 클래스를 태그 달린 클래스라고 한다.
```java
    class Figure {
        enum Shape { RECTANGLE, CIRCLE };
    
    ...
    
        Figure(double radius) {
            this.shape = Shape.CIRCLE;
            this.radius = radius;
        }
    
        Figure(double length, double width) {
            this.shape = Shape.RECTANGLE;
            this.length = length;
            this.width = width;
        }
    ...
```
### 태그 달린 클래스의 단점

- 열거 타입 선언, 태그 필드, switch 문 등 쓸데없는 코드가 많이 들어간다.
- 여러 구현이 한 클래스에 혼합돼 있어서 가독성도 나쁘다.
- 다른 의미를 위한 코드도 언제나 함께 하니 메모리도 많이 사용한다.
- 필드들을 final로 선언하려면 해당 의미에 쓰이지 않는 필드들까지 생성자에서 초기화해야 한다.
- 또 다른 의미를 추가하려면 코드를 수정해야 한다.
- 인스턴스의 타입만으로는 현재 나타내는 의미를 알 길이 전혀 없다.
- 한 마디로, 태그 달린 클래스는 장황하고, 오류를 내기 쉽고, 비효율적이다.

### 태그 달린 클래스의 대안

- 태그 달린 클래스는 클래스 계층 구조를 어설프게 흉내낸 아류일 뿐이다.
- 더 나은 수단은 계층구조를 활용하는 서브타이핑(subtyping)이다.
- 태그 달린 클래스를 클래스 계층 구조로 바꾸는 방법
    - 계층구조의 루트(root)가 될 추상 클래스를 정의한다.
    - 태그 값에 따라 동작이 달라지는 메서드들을 루트 클래스의 추상 메서드로 선언한다.
    - 태그 값에 상관없이 동작하는 일정한 메서드들을 루트 클래스에 일반 메서드로 추가한다.
    - 모든 하위 클래스에서 공통으로 사용하는 데이터 필드들도 전부 루트 클래스로 올린다.
```java
    abstract class Figure {
        abstract double area();
    }
    
    class Circle extends Figure {
        final double radius;
    
    ...
    
        @Override double area {
            return Math.PI * (radius * radius);
        }
    }
    
    class Rectangle extends Figure {
        final double length;
        final double width;
    
        @Override double aread() {
            return this.length * this.width;
        }
    }
```
- 태그 달린 클래스를 없애고 계층 구조를 사용해서 생긴 장점
    - 쓸데없는 코드(switch 등)이 사라졌다.
    - 각 의미를 독립된 클래스에 담아 관련 없던 데이터 필드를 모두 제거했다.
    - 살아 남은 필드들은 모두 final이다.
    - 각 클래스의 생성자가 모든 필드를 남김없이 초기화하고 추상 메서드를 모두 구현했는지 컴파일러가 확인해준다. 덕분에 실수로 빼먹은 case 문 때문에 런타임 오류가 발생할 일도 없다.
    - 루트 클래스의 코드를 건드리지 않고도 다른 프로그래머들이 독립적으로 계층구조를 확장하고 함께 사용할 수 있다.