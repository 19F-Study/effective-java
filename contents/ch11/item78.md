 # Item78. 공유 중인 가변 데이터는 동기화해 사용하라
 
 ## synchronized
  - 해당 메서드나 블록을 한번에 한 스레드씩 수행하도록 보장.
  - 하나의 스레드가 변경하는 중의 상태라서 다른 스레드가 일관되지 않은 순간의 객체를 보지 못하도록 막는다. 
  - 동기화된 메서드나 블록에 들어간 스레드가 같은 락의 보호하에 수행된 모든 이전 수정의 최종 결과를 보게 해준다.
  
 ## long 과 double 외의 변수를 읽고 쓰는 동작은 원자적이다.
  - 동기화 없이 여러 쓰레드가 같은 변수를 수정한다 하더라도 어떤 스레드가 정상적으로 저장한 값을 온전히 읽어옴을 보장.
  - '수정이 완전히 반영된 값'을 얻는다고 보장하지만, 스레드가 저장한 값이 다른 스레드에게 보이는가는 보장하지 않는다??
  
 ## 공유 중인 가변 데이터를 비록 원자적으로 읽고 쓸 수 있을지라도 동기화에 실패하면 처참한 결과로 이어질 수 있다.
  - Thread.stop 예제가 끝나지 않는 이유
    - 동기화하지 않으면 메인 스레드가 수전한 값을 백그라운드 스레드가 언제쯤에나 보게 될지 보증할 수 없다.
    - 동기화가 빠지면 가상머신이 다음과 같은 최적화를 수행할 수도 있다.
    ```java
      //BEFORE
      while (!stopRequested)
  	     i++;
  
      //AFTER
      if (!stopRequested)
  	     while(true)
    	      i++;
      //최적화되어버린 코드에서는 stopRequested 를 최초에만 확인하고 다시는 확인하지 않는다.
    ```
    - 쓰레드가 멈추지 않은 이유가 최적화 때문?
  - 정상 동작하도록 수정
      ```java
        private static boolean stopRequested;
      
        private static synchronized void requestStop() {
          stopRequested = true;
        }
      
        private static synchronized boolean stopRequested() {
          return stopRequested;
        }
      
        public static void main(String[] args) throws InterruptedException {
      
          Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested()) {
              i++;
            }
            System.out.println("EXITED!");
          });
          backgroundThread.start();
      
          TimeUnit.SECONDS.sleep(1);
      
          requestStop();
        }
      ```
    - 위 코드가 1초만에 종료될 수 있었던 이유는?
    - 쓰기와 읽기 모두가 동기화되지 않으면 동작을 보장하지 않는다.
    - 동기화는 배타적 수행과 스레드 간 통신이라는 두 가지 기능을 수행하는데, 이 코드에서는 그중 통신 목적으로만 사용된 것이다.
  
  - volatile 를 이용한 예제
    ```java
      	private static volatile boolean stopRequested;
      
      
      	public static void main(String[] args) throws InterruptedException {
      		Thread backgroundThread = new Thread(() -> {
      			int i = 0;
      			while (!stopRequested) {
      				i++;
      				//system out 을 찍으면 종료된다. 왜?
      				//System.out.println(i);
      			}
      			System.out.println("EXITED!");
      		});
      		backgroundThread.start();
      
      		TimeUnit.SECONDS.sleep(1);
      
      		stopRequested = true;
      
      	}
    ```  
    - volatile 를 선언하면 동기화를 생략
    - https://mygumi.tistory.com/112 : CPU 메모리와 메인 메모리 구조.
    - CPU 메모리에서 값을 읽어드리면 최신의 데이터르 얻지 못한다.
    - volatile을 사용한다면 변수를 읽고 쓰는 과정은 모든 읽기 쓰기 연산을 메인 메모리에서만 처리된다.
    - volatile은 변수에 지정할 수 있는 키워드이다. 변수에 행해지는 입출력관련 행위에 대해 컴파일러는 최적화되도록 순서를 재배치하는데, 이런 재배치 로직을 강제적으로 불가하게 함으로써 변수 자체에 원자성을 부여한다.
      ```java
      private static volatile int nextSerialNumber = 0;
  
      public static int generateSerialNumber() {
          return nextSerialNumber++;
        }
      ```
    - nextSerialNumber 가 매번 고유한 값을 반환하지 않는다.
    - ++ 연산자는 실제로는 nextSerialNumber 필드에 두번 접근한다. 
      1. 값을 읽는다.
      2. 1을 증가시켜서 저장.
    
    - a 와 b 사이에 또 다른 스레드가 접근한다면 고유한 값을 돌려받지 않게 된다.
    - generateSerialNumber 에 synchronized 를 붙이면 해결.
    
  - AtomicLong 을 이용한 예제
     ```java
      private static final AtomicLong nextSerialNum = new AtomicLong();
  
      public static long generateSerialNumber() {
        return nextSerialNum.getAndIncrement();
      }
     ```

 ### 위 문제들을 피하는 가장 좋은 방법은 가변 데이터를 쓰레드들이 공유하지 않는 것.
  - 불변 데이터만 공유하거나 아무것도 공유하지 않는다.
  - 가변 데이터는 단일 쓰레드에서만 쓰도록 하자.
  
 ### 한 스레드가 데이터를 다 수정한 후 다른 스레드에 공유할 때는 해당 객체의 공유하는 부분만 동기화 해도 된다.
  - 해당 부분이 수정되기 전까지는 동기화 없이 사용 가능
  - 이런 객체를 사실상 불변(effectively immutable)이라고 한다.
  - 다른 스레드에게 이런 객체를 건네는 행위를 안전 발행(safe publication)이라고 한다.
  - 객체를 안전하게 발행하는 방법들
    - 정적 필드
    - volatile 필드
    - final필드
    - 락을 통해 접근하는 필드
    - 동시성 컬렉션에 저장 
   
    
    
    
    
 ### volatile의 경우는 하나의 스레드가 쓰기 연산을 하고, 다른 스레드에서는 읽기 연산을 통해 최신 값을 가져올 경우. 즉 다른 스레드에서는 업데이트를 행하지 않을 경우 이용할 수 있다.
 ### Atomic*** 클래스의 경우는 여러 스레드에서 읽기 쓰기 모두 이용할 수 있다. (CAS)
 ### synchronized 경우도 여러 스레드에서 읽기 쓰기 모두 이용할 수 있다. (Lock)
    