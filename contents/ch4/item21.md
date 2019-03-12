# item21) 인터페이스는 구현하는 쪽을 생각해 설계하라  
## Default method 란? 
- 자바8부터 지원  
- 기존 구현체를 깨뜨리지 않고 인터페이스에 메서드 추가 가능  
- 디폴트 메서드가 선언된 인터페이스를 구현 한 후 디폴트 메서드를 재정의 하지 않은 모든 클래스에서 디폴트 구현이 사용됨   
- 자바 8에서 핵심 컬렉션 인터페이스들에 다수의 디폴트 메서드가 추가 되었다. (주로 람다를 활용하기 위함)   
예) 자바 8 Collection 인터페이스의 removeIf   
```
default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
```
    
## Default method 사용 시 주의할 점  
- 새로운 인터페이스를 만드는 경우 표준적인 메서드 구현을 제공하는 유용한 수단이지만,  
기존 인터페이스에 디폴트 메서드로 새 메서드를 추가하는 일은 꼭 필요한 경우가 아니면 피해야 한다.
- 새로운 인터페이스 설계시 반드시 테스트를 거쳐야하며,  
서로 다른 3가지 구현체와 클라이언트를 만ㄷ르어 테스트 해봐야 한다.  
- 자바7까지는 모든 클래스가 현재의 인터페이스에 새로운 메서드가 추가될 일을 가정하지 않았기 때문에,  
기존 인터페이스에 디폴트 메서드를 추가한 경우 매끄럽게 연동되지 않을 수 있다.  
컴파일에 성공하더라고 런타임 오류를 일으킬 수 있다.  
예) 자바 8 Collection 인터페이스의 removeIf  
``org.apache.commons.collections4.collection.SyncronizedCollection``