# item80. 스레드보다는 실행자, 태스크, 스트림을 애용하라  
  
* java.util.concurrent 패키지는 실행자 프레임워크 (Executor Framework)라고 하는 인터페이스 기반의 유연한 태스크 실행기능을 담고 있다.
```java
ExecutorService exec = Executors.newSingleThreadExecutor();
exe.execute(runnable); // 실행자에 실행할 태스크를 넘김
...
exe.shutdown(); // 실행자 종료. 이 작업이 실패하면 VM 자체가 종료되지 않음.
```  

## 실행자 서비스의 주요 기능  
1. 특정 태스크가 완료되기를 기다린다. (get 메서드)  
2. 태스크 모음 중 아무것 하나(invokeAny 메서드) 혹은 모든 태스크(invokeAll 메서드)가 완료되기를 기다린다.  
3. 실행자 서비스가 종료하기를 기다린다. (awaitTermination 메서드)  
4. 완료된 태스크들의 결과를 차례로 받는다. (ExecutorCompletionService 이용)  
5. 태스크를 특정 시간에 혹은 주기적으로 실행하게 한다. (ScheduledThreadPollExecutor 사용)  

## 실행자 서비스  
* java.util.concurrent.Executors의 정적 팩터리를 이용하여 다른 종류의 실행자 서비스(스레드 풀)을 생성할 수 있다.  
* ThreadPollExecutor 클래스를 직접 사용할 수도 있으며, 스레드풀 동작을 결정하는 거의 모든 속성을 설정할 수 있다.
* 스레드 풀의 스레드 개수는 고정할 수도 있고, 필요에 따라 늘어나거나 줄어들게 설정할 수 있다.  
* Executors.newCachedThreadPool : 요청받은 태스크들이 큐에 쌓이지 않고 즉시 스레드에 위임되어 실행된다. 특별히 설정할 게 없을 때 사용하기 적합.  
* Executors.newFixedThreadPool : 스레드 개수를 고정.  
* 실행자 프레임워크에서는 작업 단위와 실행 메커니즘이 분리된다.  
* 작업단위를 나타내는 핵심 추상 개념이 태스크이다.
* 태스크를 수행하는 일반적인 매커니즘이 실행자 서비스이다.

## 태스크  
* Runnable과 Callable. (Callable은 Runnable과 비슷하지만 값을 반환하고 임의의 예외를 던질 수 있다.)  
* 태스크 수행을 실행자 서비스에 맡기면 원하는 태스크 수행 정책을 선택할 수 있고, 언제든 변경 가능하다.  
* 실행자 프레임워크가 작업 수행을 담당한다!!
* 자바7부터 실행자 프레임워크는 포크-조인(fork-join) 태스크를 지원하도록 확장됐다.  
* 포크-조인 태스크는 포크-조인 풀이라는 특별한 실행자 서비스가 실행해준다.  
* ForkJoinTask의 인스턴스는 작은 하위 태스크로 나뉠 수 있고, ForkJoinPool을 구성하는 스레드들이 이 태스크들을 처리하며,  
일을 먼저 끝낸 스레드는 다른 스레드의 남은 태스크를 가져와 대신 처리하여 높은 처리량과 낮은 지연시간을 달성한다.  


> 실행자 프레임워크 기능 참고 서적  
자바 병렬 프로그래밍 (에이콘 출판사,2008)