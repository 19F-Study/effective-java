# 아이템7. 다쓴 객체 참조를 해제하라

### 코드 7-1 의 메모리 누수는 어느 부분에서 발생할까?
  - ?
  
### 객체 참조를 null 처리하는 일은 예외적인 경유여야 한다.
  - 다 쓴 참조를 해제하는 가장 좋은 방법은 참조를 담은 변수를 유효 범위(scope) 밖으로 밀어내는 것.
  
### Stack 클래스가 메무리 누수에 취약한 이유?
  - 자기 메모리를 직접 관리 (elements 배열)
  - 가비지 컬렉터는 elements 배열의 확성영역/비활성영역을 구분할 수 없기 때문에 모두 GC 대상이 아니다.
  - 때문에 자기 메모리를 직접 관리하는 클래스는 메모리 누수에 주의.
  
### 캐시 역시 메모리 누수를 일으키는 주범
  - 객체 참조를 캐시에 넣고 객체를 모두 사용한 후에도 캐시를 만료시키지 않는다면..
  - WeakHashMap
  - LinkedHashMap
  
### 메모리 누수의 세번째 주범! Listener , Callback
  - CallBack?
  - 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면..
  - 콜백을 weak reference 로 저장하면 가비지 컬렉터가 즉시 수거.

    > 녹색으로 표시한 중간의 두 객체는 WeakReference로만 참조된 weakly reachable 객체이고, 파란색 객체는 strongly reachable 객체이다. GC가 동작할 때, unreachable 객체뿐만 아니라 weakly reachable 객체도 가비지 객체로 간주되어 메모리에서 회수된다. root set으로부터 시작된 참조 사슬에 포함되어 있음에도 불구하고 GC가 동작할 때 회수되므로, 참조는 가능하지만 반드시 항상 유효할 필요는 없는 LRU 캐시와 같은 임시 객체들을 저장하는 구조를 쉽게 만들 수 있다.
    - https://d2.naver.com/helloworld/329631
    - https://itmining.tistory.com/10
    


### 메모리 누수가 일어나면 어떤 현상이?
  - 잦은 GC 발생
  - Stop the world
  - 결국엔 OutOfMemory?